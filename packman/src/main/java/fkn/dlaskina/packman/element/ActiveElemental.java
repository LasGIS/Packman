package fkn.dlaskina.packman.element;

import fkn.dlaskina.packman.map.Cell;

/**
 * Definition of the ActiveElemental class
 * @author VLaskin
 * @since 27.03.2016.
 */
public abstract class ActiveElemental extends Elemental {

    protected ActiveElemental(ElementalType type) {
        super(type);
    }

    /**
     * Перемещение и взаимодействие
     * @param cell текущая ячеёка
     */
    public abstract void act(final Cell cell);
}
