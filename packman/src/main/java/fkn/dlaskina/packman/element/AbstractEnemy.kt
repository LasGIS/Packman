package fkn.dlaskina.packman.element

import fkn.dlaskina.packman.map.Cell
import fkn.dlaskina.packman.map.Matrix.packMan
import java.awt.Color
import java.awt.Graphics
import java.awt.Polygon
import java.awt.Rectangle

/**
 * The Class AbstractEnemy.
 * @author Vladimir Laskin
 * @version 1.0
 */
abstract class AbstractEnemy internal constructor(cell: Cell) : ActiveElemental(ElementalType.Enemy, cell) {

    companion object {
        private val FILL_COLOR = Color(255, 0, 0)
        private val BOUND_COLOR = Color(125, 0, 0)
        private const val BORDER = 2
    }

    var dummy = false
    private var xText = 0
    private var yText = 0
    var deleted = false

    override fun paint(gr: Graphics, rect: Rectangle, frame: Int) {
        val polygon = createPolygon(rect, frame)
        gr.color = FILL_COLOR
        gr.fillPolygon(polygon)
        gr.color = BOUND_COLOR
        gr.drawPolygon(polygon)
        gr.drawString(if (dummy) "D" else "E", xText, yText)
    }

    private fun createPolygon(rect: Rectangle, frame: Int): Polygon {
        val x = (rect.x + cellX).toInt()
        val y = (rect.y + cellY).toInt()
        val x0 = x + rect.width / 2
        val y0 = y + rect.height / 2
        val x1 = x + BORDER
        val y1 = y + BORDER
        val x2 = x + rect.width - BORDER
        val y2 = y + rect.height - BORDER
        val factor = (if (frame < 20) frame else 40 - frame) / 20.0
        val dx = ((rect.width / 2 - BORDER) * factor).toInt()
        val dy = ((rect.height / 2 - BORDER) * factor).toInt()
        return when (cellMoveType) {
            MoveType.DOWN -> {
                xText = x + 12
                yText = y + 14
                Polygon(intArrayOf(x1, x2, x2 - dx, x0, x1 + dx), intArrayOf(y1, y1, y2, y0, y2), 5
                )
            }
            MoveType.UP -> {
                xText = x + 12
                yText = y + 26
                Polygon(intArrayOf(x1 + dx, x0, x2 - dx, x2, x1), intArrayOf(y1, y0, y1, y2, y2), 5
                )
            }
            MoveType.LEFT -> {
                xText = x + 18
                yText = y + 20
                Polygon(intArrayOf(x2, x2, x1, x0, x1), intArrayOf(y1, y2, y2 - dy, y0, y1 + dy), 5
                )
            }
            MoveType.RIGHT -> {
                xText = x + 6
                yText = y + 20
                Polygon(intArrayOf(x1, x2, x0, x2, x1), intArrayOf(y1, y1 + dy, y0, y2 - dy, y2), 5
                )
            }
            else -> {
                xText = x + 12
                yText = y + 16
                Polygon(intArrayOf(x1, x2, x2, x1), intArrayOf(y1, y1, y2, y2), 4)
            }
        }
    }

    fun findPackMan(alterCell: List<AlterCellMove>): AlterCellMove? {
        val cell = packMan.cell
        val isAggressive = packMan.prizeType == SurpriseType.aggressive
        var distance = if (isAggressive) Double.MIN_VALUE else Double.MAX_VALUE
        var ret: AlterCellMove? = null
        for (acm in alterCell) {
            val distanceCell = acm.cell.distance(cell)
            if (if (isAggressive) distanceCell > distance else distanceCell < distance) {
                distance = distanceCell
                ret = acm
            }
        }
        return ret
    }

    fun findAlternativeCells(alterCell: MutableList<AlterCellMove>): Int {
        val tempCell = arrayOfNulls<AlterCellMove>(4)
        when (moveType) {
            MoveType.DOWN -> {
                tempCell[0] = cell.getCell(0, 1)?.let { AlterCellMove(it, MoveType.DOWN) }
                tempCell[1] = cell.getCell(1, 0)?.let { AlterCellMove(it, MoveType.RIGHT) }
                tempCell[2] = cell.getCell(-1, 0)?.let { AlterCellMove(it, MoveType.LEFT) }
                tempCell[3] = cell.getCell(0, -1)?.let { AlterCellMove(it, MoveType.UP) }
            }
            MoveType.NONE, MoveType.UP -> {
                tempCell[0] = cell.getCell(0, -1)?.let { AlterCellMove(it, MoveType.UP) }
                tempCell[1] = cell.getCell(-1, 0)?.let { AlterCellMove(it, MoveType.LEFT) }
                tempCell[2] = cell.getCell(1, 0)?.let { AlterCellMove(it, MoveType.RIGHT) }
                tempCell[3] = cell.getCell(0, 1)?.let { AlterCellMove(it, MoveType.DOWN) }
            }
            MoveType.RIGHT -> {
                tempCell[0] = cell.getCell(1, 0)?.let { AlterCellMove(it, MoveType.RIGHT) }
                tempCell[1] = cell.getCell(0, 1)?.let { AlterCellMove(it, MoveType.DOWN) }
                tempCell[2] = cell.getCell(0, -1)?.let { AlterCellMove(it, MoveType.UP) }
                tempCell[3] = cell.getCell(-1, 0)?.let { AlterCellMove(it, MoveType.LEFT) }
            }
            MoveType.LEFT -> {
                tempCell[0] = cell.getCell(-1, 0)?.let { AlterCellMove(it, MoveType.LEFT) }
                tempCell[1] = cell.getCell(0, -1)?.let { AlterCellMove(it, MoveType.UP) }
                tempCell[2] = cell.getCell(0, 1)?.let { AlterCellMove(it, MoveType.DOWN) }
                tempCell[3] = cell.getCell(1, 0)?.let { AlterCellMove(it, MoveType.RIGHT) }
            }
        }
        for (alter in tempCell) {
            alter?.let {
                val testCell = it.cell
                if (!testCell.isStone && !testCell.contains(ElementalType.Enemy)) {
                    alterCell.add(alter)
                }
            }
        }
        return alterCell.size
    }
}