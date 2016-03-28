package fkn.dlaskina.packman.element;

import fkn.dlaskina.packman.map.Cell;

/**
 * Definition of the ActiveElemental class
 * @author VLaskin
 * @since 27.03.2016.
 */
public abstract class ActiveElemental extends Elemental {

    /** ячейка, в которой находится существо */
    protected Cell cell;
    protected MoveType moveType = MoveType.NONE;

    protected ActiveElemental(ElementalType type, final Cell cell) {
        super(type);
        this.cell = cell;
    }

    /**
     * Перемещение и взаимодействие
     */
    public abstract void act();

    public void setMove(MoveType moveType) {
        this.moveType = moveType;
    }
}
