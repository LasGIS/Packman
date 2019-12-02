package fkn.dlaskina.packman.element;

import java.awt.Graphics;
import java.awt.Rectangle;

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
     * @param frame номер кадра (один из 40)
     */
    public abstract void paint(final Graphics gr, final Rectangle rect, final int frame);
}
