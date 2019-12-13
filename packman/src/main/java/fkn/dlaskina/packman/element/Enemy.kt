package fkn.dlaskina.packman.element

import fkn.dlaskina.packman.map.Cell
import fkn.dlaskina.packman.map.GameOverException
import fkn.dlaskina.packman.map.Matrix.packMan
import fkn.dlaskina.packman.map.Matrix.removeEnemy

/**
 * Враг.
 * @author VLaskin
 * @since 26.03.2016.
 */
class Enemy(cell: Cell?) : AbstractEnemy(cell!!) {

    @Throws(GameOverException::class)
    override fun act() {
        if (isCenterCell) {
            val packMan = packMan
            val isAggressive = packMan.prizeType == SurpriseType.aggressive
            var finalCellMove: AlterCellMove? = null
            var finalPackManRate = if (isAggressive) Int.MIN_VALUE else Int.MAX_VALUE
            for (cellMove in cell.aroundCells) {
                val packManRate = cellMove.cell.packManRate
                if (if (isAggressive) packManRate > finalPackManRate else packManRate < finalPackManRate) {
                    finalPackManRate = packManRate
                    finalCellMove = cellMove
                }
            }
            if (finalCellMove != null) {
                newCell = finalCellMove.cell
                cellMoveType = finalCellMove.moveType
            } else { // нет выхода
                newCell = null
            }
        }
        if (isBorderCell) {
            if (newCell != null) {
                cell.removeElement(this)
                newCell!!.addElement(this)
                cell = newCell as Cell
                moveType = cellMoveType
                startCellMove()
                // проверяем на packman`a
                if (newCell!!.contains(ElementalType.PackMan)) {
                    val packMan = packMan
                    if (packMan.prizeType == SurpriseType.aggressive) {
                        removeEnemy(this)
                    } else {
                        throw GameOverException(false, "Враг наехал на рокемона")
                    }
                }
            }
        }
        cellMove()
    }

    init {
        dummy = false
    }
}