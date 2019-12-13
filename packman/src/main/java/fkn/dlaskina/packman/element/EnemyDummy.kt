package fkn.dlaskina.packman.element

import fkn.dlaskina.packman.map.Cell
import fkn.dlaskina.packman.map.GameOverException
import fkn.dlaskina.packman.map.Matrix.packMan
import fkn.dlaskina.packman.map.Matrix.removeEnemy
import java.util.*

/**
 * Враг.
 * @author VLaskin
 * @since 26.03.2016.
 */
class EnemyDummy(cell: Cell?) : AbstractEnemy(cell!!) {
    @Throws(GameOverException::class)
    override fun act() {
        if (isCenterCell) { // альтернативные перемещения
            val alterCell: MutableList<AlterCellMove> = ArrayList()
            val alterCount = findAlternativeCells(alterCell)
            when {
                alterCount > 2 -> { // есть много путей
                    val alterMoveType = findPackMan(alterCell)
                    newCell = alterMoveType!!.cell
                    cellMoveType = alterMoveType.moveType
                }
                alterCount > 0 -> { // только один путь - вперёд!
                    val alterMoveType = alterCell[0]
                    newCell = alterMoveType.cell
                    cellMoveType = alterMoveType.moveType
                }
                else -> { // нет выхода
                    newCell = null
                }
            }
        }
        if (isBorderCell) {
            newCell?.let {
                cell.removeElement(this)
                it.addElement(this)
                cell = it
                moveType = cellMoveType
                startCellMove()
                // проверяем на packman`a
                if (it.contains(ElementalType.PackMan)) {
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
        dummy = true
    }
}