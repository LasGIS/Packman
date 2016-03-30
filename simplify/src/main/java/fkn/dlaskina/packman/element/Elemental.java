package fkn.dlaskina.packman.element;

import java.awt.*;

/**
 * Элементарная сущьность, которая может находится в ячейке
 */
public abstract class Elemental {

    /** тип сущьности */
    private final ElementalType type;

    /**
     * Конструктор
     * @param type тип сущьности
     */
    protected Elemental(ElementalType type) {
        this.type = type;
    }

    /**
     * вернуть тип сущьности
     * @return тип сущьности
     */
    public ElementalType getType() {
        return type;
    }

    /**
     * Нарисовать изображение сущьности.
     * @param gr graphics contexts
     * @param rect рамка
     */
    public abstract void paint(final Graphics gr, final Rectangle rect);
}
