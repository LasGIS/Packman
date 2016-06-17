package fkn.dlaskina.packman.element;

import fkn.dlaskina.packman.map.Cell;
import fkn.dlaskina.packman.map.GameOverException;

import static fkn.dlaskina.packman.element.MoveType.NONE;
import static fkn.dlaskina.packman.map.Matrix.CELL_SIZE;
import static fkn.dlaskina.packman.map.Matrix.CELL_STEP;

/**
 * Definition of the ActiveElemental class
 * @author VLaskin
 * @since 27.03.2016.
 */
public abstract class ActiveElemental extends Elemental {

    /** ячейка, в которой находится существо */
    protected Cell cell;
    /** координаты существа в ячейке */
    protected int cellX = 0;
    protected int cellY = 0;
    /** установленное перемещение. */
    protected MoveType moveType = NONE;
    /** перемещение внутри ячейки. */
    protected MoveType cellMoveType = NONE;

    protected ActiveElemental(ElementalType type, final Cell cell) {
        super(type);
        this.cell = cell;
    }

    /**
     * Перемещаемся внутри ячейки.
     * @ param alternativeMoveType альтернативное направление (если нет основного)
     */
    protected void cellMove(/*final MoveType alternativeMoveType*/) {
/*
        if (cellMoveType == NONE) {
            cellMoveType = alternativeMoveType;
        }
*/
        switch (cellMoveType) {
            case DOWN:
                cellY += CELL_STEP; cellX = 0;
                break;
            case UP:
                cellY -= CELL_STEP; cellX = 0;
                break;
            case RIGHT:
                cellX += CELL_STEP; cellY = 0;
                break;
            case LEFT:
                cellX -= CELL_STEP; cellY = 0;
                break;
        }
    }

    protected boolean isCenterCell() {
        return (Math.abs(cellX) < CELL_STEP && Math.abs(cellY) < CELL_STEP);
    }

    protected boolean isBorderCell() {
        return (Math.abs(cellX) >= CELL_SIZE / 2 || Math.abs(cellY) >= CELL_SIZE / 2);
    }
    /**
     * вступаем на новую ячейку, устанавливаем новые координаты
     */
    protected void startCellMove() {
        switch (cellMoveType) {
            case DOWN:  cellY = -CELL_SIZE / 2; cellX = 0; break;
            case UP:    cellY = +CELL_SIZE / 2; cellX = 0; break;
            case RIGHT: cellX = -CELL_SIZE / 2; cellY = 0; break;
            case LEFT:  cellX = +CELL_SIZE / 2; cellY = 0; break;
            default:    cellX = 0; cellY = 0; break;
        }
    }

    /**
     * Перемещение и взаимодействие
     */
    public abstract void act() throws GameOverException;

    public void setMove(MoveType moveType) {
        this.moveType = moveType;
    }
}
