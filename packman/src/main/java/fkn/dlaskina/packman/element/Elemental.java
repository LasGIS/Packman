package fkn.dlaskina.packman.element;

import java.awt.*;

/**
 * Элементарная сущьность, которая может находится в ячейке
 */
public abstract class Elemental {

    private final ElementalType type;

    protected Elemental(ElementalType type) {
        this.type = type;
    }

    public ElementalType getType() {
        return type;
    }

    public abstract void paint(final Graphics gr, final Rectangle rect);
}
