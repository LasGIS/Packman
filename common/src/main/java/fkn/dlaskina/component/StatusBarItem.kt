/*
 * @(#)StatusBarItem.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2013 LasGIS Company. All Rights Reserved.
 */
package fkn.dlaskina.component

import java.awt.Dimension
import javax.swing.JLabel
import javax.swing.border.BevelBorder
import javax.swing.border.Border

/**
 * Элемент status bara.
 *
 * @author vlaskin
 * @version 1.0
 * @since 18.03.2008 : 19:33:57
 */
class StatusBarItem constructor(str: String, aSize: Int) : JLabel(stringDeNormalize(str)) {

    companion object {
        /**   */
        private val border: Border = BevelBorder(BevelBorder.LOWERED)

        /**
         * Добавляем пробел спереди для лучшей визуализации.
         * @param str входная строка
         * @return ненормализованная выходная строка
         */
        fun stringDeNormalize(str: String): String {
            return " $str"
        }
    }

    /**
     * Конструктор.
     * @param str начальное значение строки
     * @param aSize размер по вертикали
     */
    init {
        if (aSize > 0) {
            this.border = Companion.border
            size = Dimension(aSize, StatusBar.SIZE_HEIGHT_STATUSBAR)
        }
    }

    /**
     * Возвращаем предпочтительный размер ячейки (если он есть).
     * @return the value of the `preferredSize`
     * @see javax.swing.plaf.ComponentUI
     */
    override fun getPreferredSize(): Dimension {
        return if (size != null) {
            size
        } else {
            super.getPreferredSize()
        }
    }

}