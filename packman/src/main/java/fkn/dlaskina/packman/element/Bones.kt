package fkn.dlaskina.packman.element

import fkn.dlaskina.packman.map.Cell
import fkn.dlaskina.packman.map.GameOverException
import fkn.dlaskina.packman.map.Matrix
import fkn.dlaskina.packman.map.Matrix.createBoneRate
import java.awt.Color
import java.awt.Graphics
import java.awt.Rectangle

/**
 * Это то, что осталось от врага, когда его съели :(.
 * @author Vladimir Laskin
 * @version 1.0
 */
class Bones(cell: Cell?, protected var isDummy: Boolean) : ActiveElemental(ElementalType.Bones, cell!!) {
    override fun paint(gr: Graphics, rect: Rectangle, frame: Int) {
        val x = (rect.x + cellX + rect.width / 2).toInt()
        val y = (rect.y + cellY + rect.height / 2).toInt()
        val arm = rect.height / 2 - BORDER
        gr.color = BOUND_COLOR
        paintBone(gr, arm, x - arm, y, frame)
        paintBone(gr, arm, x, y + arm, frame)
        paintBone(gr, arm, x + arm, y, frame)
        paintBone(gr, arm, x, y - arm, frame)
    }

    private fun paintBone(
            gr: Graphics, arm: Int, x: Int, y: Int, frame: Int
    ) {
        val sinFrame = Math.sin(frame * Math.PI / 20).toInt() * arm
        val cosFrame = Math.cos(frame * Math.PI / 20).toInt() * arm
        val x1 = x + sinFrame
        val y1 = y + cosFrame
        val x2 = x - sinFrame
        val y2 = y - cosFrame
        gr.drawLine(x1, y1, x2, y2)
    }

    @Throws(GameOverException::class)
    override fun act() {
        if (isCenterCell) {
            var finalCellMove: AlterCellMove? = null
            var finalBoneRate = Int.MAX_VALUE
            for (cellMove in cell.aroundCells) {
                val boneRate = cellMove.cell.boneRate
                if (boneRate < finalBoneRate) {
                    finalBoneRate = boneRate
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
                if (newCell!!.contains(ElementalType.MedBox)) {
                    val elements: MutableCollection<Elemental> = newCell!!.elements
                    for (elm in elements) {
                        if (elm.type == ElementalType.MedBox) {
                            elements.remove(elm)
                            break
                        }
                    }
                    val enm = if (isDummy) EnemyDummy(newCell) else Enemy(newCell)
                    newCell!!.addElement(enm)
                    val matrixElements = Matrix.elements
                    matrixElements.add(enm)
                    newCell!!.removeElement(this)
                    matrixElements.remove(this)
                    createBoneRate()
                }
            }
        }
        cellMove()
    }

    companion object {
        private val BOUND_COLOR = Color(125, 0, 0)
        private const val BORDER = 2
    }

    init {
        cellStep = 1.0
    }
}