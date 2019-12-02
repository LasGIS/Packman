package fkn.dlaskina.packman.element

import java.awt.Graphics
import java.awt.Rectangle

/**
 * Элементарная сущьность, которая может находится в ячейке
 * @param type тип сущьности
 */
abstract class Elemental(val type: ElementalType) {
    /**
     * Нарисовать изображение сущьности.
     * @param gr graphics contexts
     * @param rect рамка
     * @param frame номер кадра (один из 40)
     */
    abstract fun paint(gr: Graphics, rect: Rectangle, frame: Int)
}