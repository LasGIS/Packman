package fkn.dlaskina.packman.element;

import fkn.dlaskina.packman.map.Cell;
import fkn.dlaskina.packman.map.GameOverException;

import static fkn.dlaskina.packman.element.MoveType.NONE;
import static fkn.dlaskina.packman.map.Matrix.CELL_SIZE;

/**
 * Definition of the ActiveElemental class
 * @author VLaskin
 * @since 27.03.2016.
 */
public abstract class ActiveElemental extends Elemental {

    /** ячейка, в которой находится существо */
    protected Cell cell;
    /** ячейка, в которой будет находится существо на следующем шаге */
    protected Cell newCell = null;
    /** координаты существа в ячейке */
    protected double cellX = 0.0;
    protected double cellY = 0.0;
    /** шаг животного внутри ячейки. */
    protected double cellStep;
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
    protected void cellMove() {
        switch (cellMoveType) {
            case DOWN:
                cellY += cellStep; cellX = 0;
                break;
            case UP:
                cellY -= cellStep; cellX = 0;
                break;
            case RIGHT:
                cellX += cellStep; cellY = 0;
                break;
            case LEFT:
                cellX -= cellStep; cellY = 0;
                break;
        }
    }

    protected boolean isCenterCell() {
        return (Math.abs(cellX) < cellStep && Math.abs(cellY) < cellStep);
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
