package fkn.dlaskina.packman.element

import fkn.dlaskina.packman.map.Cell
import fkn.dlaskina.packman.map.GameOverException
import fkn.dlaskina.packman.map.Matrix.CELL_SIZE
import kotlin.math.abs

/**
 * Definition of the ActiveElemental class
 *
 * @author VLaskin
 * @since 27.03.2016.
 * @param cell ячейка, в которой находится существо
 */
abstract class ActiveElemental(
        type: ElementalType,
        var cell: Cell
) : Elemental(type) {

    /** ячейка, в которой будет находится существо на следующем шаге  */
    @JvmField
    var newCell: Cell? = null
    /** координаты существа в ячейке  */
    @JvmField
    var cellX = 0.0
    @JvmField
    var cellY = 0.0
    /** шаг животного внутри ячейки.  */
    @JvmField
    var cellStep = 0.0
    /**
     * установленное перемещение.
     */
    @JvmField
    var moveType = MoveType.NONE
    /**
     * перемещение внутри ячейки.
     */
    @JvmField
    var cellMoveType = MoveType.NONE

    /**
     * Перемещаемся внутри ячейки.
     *
     * @ param alternativeMoveType альтернативное направление (если нет основного)
     */
    fun cellMove() {
        when (cellMoveType) {
            MoveType.DOWN -> {
                cellY += cellStep
                cellX = 0.0
            }
            MoveType.UP -> {
                cellY -= cellStep
                cellX = 0.0
            }
            MoveType.RIGHT -> {
                cellX += cellStep
                cellY = 0.0
            }
            MoveType.LEFT -> {
                cellX -= cellStep
                cellY = 0.0
            }
            MoveType.NONE -> {
            }
        }
    }

    val isCenterCell: Boolean
        get() = abs(cellX) < cellStep && abs(cellY) < cellStep

    val isBorderCell: Boolean
        get() = abs(cellX) >= CELL_SIZE / 2 || abs(cellY) >= CELL_SIZE / 2

    /**
     * вступаем на новую ячейку, устанавливаем новые координаты
     */
    fun startCellMove() {
        when (cellMoveType) {
            MoveType.NONE -> {
            }
            MoveType.DOWN -> {
                cellY = -CELL_SIZE.toDouble() / 2
                cellX = 0.0
            }
            MoveType.UP -> {
                cellY = +CELL_SIZE / 2.toDouble()
                cellX = 0.0
            }
            MoveType.RIGHT -> {
                cellX = -CELL_SIZE / 2.toDouble()
                cellY = 0.0
            }
            MoveType.LEFT -> {
                cellX = +CELL_SIZE / 2.toDouble()
                cellY = 0.0
            }
        }
    }

    /**
     * Перемещение и взаимодействие
     */
    @Throws(GameOverException::class)
    abstract fun act()

    fun setMove(moveType: MoveType) {
        this.moveType = moveType
    }

}