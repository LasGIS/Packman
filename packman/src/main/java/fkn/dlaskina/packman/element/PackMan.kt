package fkn.dlaskina.packman.element

import fkn.dlaskina.packman.map.Cell
import fkn.dlaskina.packman.map.GameOverException
import fkn.dlaskina.packman.map.Matrix.createPackManRate
import fkn.dlaskina.packman.map.Matrix.removeEnemy
import fkn.dlaskina.packman.panels.ConfigPanel
import fkn.dlaskina.packman.util.Alog
import org.apache.log4j.LogManager
import java.awt.Color
import java.awt.Graphics
import java.awt.Rectangle

/**
 * Definition of the PackMan class
 * @author VLaskin
 * @since 26.03.2016.
 */
class PackMan(cell: Cell?) : ActiveElemental(ElementalType.PackMan, cell!!) {

    companion object {
        private val log = LogManager.getLogger(this::class.java)
        private val FILL_COLOR = Color(0, 255, 0)
        private val BOUND_COLOR = Color(0, 125, 0)
        private val SPEED_FILL_COLOR = Color(0, 128, 255)
        private val SPEED_BOUND_COLOR = Color(0, 0, 200)
        private val AGGRESSIVE_FILL_COLOR = Color(255, 128, 0)
        private val AGGRESSIVE_BOUND_COLOR = Color(200, 0, 0)
        private const val SPEED_CELL_STEP = 5.0
        private const val SIMPLE_CELL_STEP = 3.0
        private const val BORDER = 2
    }

    var prizeType = SurpriseType.simple
        private set
    private var prizeTime: Long = 0

    init {
        cellStep = SIMPLE_CELL_STEP
    }

    override fun paint(gr: Graphics, rect: Rectangle, frame: Int) {
        val x = (rect.x + cellX + BORDER).toInt()
        val y = (rect.y + cellY + BORDER).toInt()
        val width = rect.width - BORDER * 2
        val height = rect.height - BORDER * 2
        val angView: Int
        val angMouth = (if (frame < 20) frame else 40 - frame) * 3
        angView = when (cellMoveType) {
            MoveType.DOWN -> 270
            MoveType.UP -> 90
            MoveType.LEFT -> 180
            MoveType.RIGHT -> 0
            else -> 0
        }
        gr.color = fillColor
        gr.fillArc(x, y, width, height, angView + angMouth, 360 - angMouth * 2)
        gr.color = boundColor
        gr.drawArc(x, y, width, height, angView + angMouth, 360 - angMouth * 2)
    }

    private val boundColor: Color
        get() = when (prizeType) {
            SurpriseType.aggressive -> AGGRESSIVE_BOUND_COLOR
            SurpriseType.speed -> SPEED_BOUND_COLOR
            else -> BOUND_COLOR
        }

    private val fillColor: Color
        get() = when (prizeType) {
            SurpriseType.aggressive -> AGGRESSIVE_FILL_COLOR
            SurpriseType.speed -> SPEED_FILL_COLOR
            else -> FILL_COLOR
        }

    @Throws(GameOverException::class)
    override fun act() {
        if (isCenterCell) {
            newCell = when (moveType) {
                MoveType.DOWN -> cell.getCell(0, 1)
                MoveType.UP -> cell.getCell(0, -1)
                MoveType.RIGHT -> cell.getCell(1, 0)
                MoveType.LEFT -> cell.getCell(-1, 0)
                else -> cell
            }
            if (newCell == null || newCell!!.isStone) {
                cellX = 0.0
                cellY = 0.0
                moveType = MoveType.NONE
                cellMoveType = moveType
            } else {
                cellMoveType = moveType
            }
            createPackManRate()
        }
        if (isBorderCell) {
            val nCell = newCell
            if (nCell != null) {
                cell.removeElement(this)
                nCell.addElement(this)
                cell = nCell
                startCellMove()
                // забираем призы и проверяем на злодея
                for (elm in nCell.elements) {
                    when (elm.type) {
                        ElementalType.Surprise -> {
                            val surpriseType = (elm as Surprise).prizeType
                            var isRemove = false
                            if (prizeType === SurpriseType.simple && surpriseType !== SurpriseType.simple) {
                                prizeType = surpriseType
                                when (prizeType) {
                                    SurpriseType.aggressive -> {
                                        prizeTime = System.currentTimeMillis() + 10000
                                        isRemove = true
                                    }
                                    SurpriseType.speed -> {
                                        prizeTime = System.currentTimeMillis() + 20000
                                        cellStep = SPEED_CELL_STEP
                                        isRemove = true
                                    }
                                    else -> {/*nothing*/}
                                }
                            }
                            if (isRemove || !(prizeType !== SurpriseType.simple && surpriseType !== SurpriseType.simple)) {
                                nCell.removeElement(elm)
                                if (ConfigPanel.addBonus()) {
                                    throw GameOverException(true, "Победа!")
                                }
                            }
                        }
                        ElementalType.Enemy -> {
                            if (prizeType === SurpriseType.aggressive) {
                                removeEnemy((elm as AbstractEnemy))
                            } else {
                                throw GameOverException(false, "Сам наехал на врага")
                            }
                        }
                        else -> {
                            log.info("проверка")
                        }
                    }
                }
                createPackManRate()
            } else {
                cellX = 0.0
                cellY = 0.0
                moveType = MoveType.NONE
                cellMoveType = moveType
            }
        }
        if (prizeType !== SurpriseType.simple && prizeTime < System.currentTimeMillis()) {
            cellStep = SIMPLE_CELL_STEP
            prizeType = SurpriseType.simple
        }
        cellMove()
    }

}