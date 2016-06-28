package fkn.dlaskina.packman.element;

import fkn.dlaskina.packman.map.Cell;

/**
 * Элементарный объект для выбора альтернативы перемещения.
 * @author Vladimir Laskin
 * @version 1.0
 */
public class AlterCellMove {

    private MoveType moveType;
    private Cell cell;

    public AlterCellMove(final Cell cell, final MoveType moveType) {
        this.moveType = moveType;
        this.cell = cell;
    }

    public MoveType getMoveType() {
        return moveType;
    }

    public void setMoveType(final MoveType moveType) {
        this.moveType = moveType;
    }

    public Cell getCell() {
        return cell;
    }

    public void setCell(final Cell cell) {
        this.cell = cell;
    }
}
