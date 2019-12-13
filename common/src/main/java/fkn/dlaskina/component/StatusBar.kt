/*
 * @(#)StatusBar.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2013 LasGIS Company. All Rights Reserved.
 */
package fkn.dlaskina.component

import java.awt.Color
import java.awt.Dimension
import javax.swing.Box
import javax.swing.Box.Filler
import javax.swing.BoxLayout
import javax.swing.JComponent
import javax.swing.JLabel

/**
 * Строка состояния, состоящая из нескольких разделов.
 *
 * @author vlaskin
 * @version 1.0
 * @since 18.03.2008 : 14:15:04
 */
class StatusBar @JvmOverloads constructor(sizes: IntArray = intArrayOf(0, 100, 100, 100)) : JComponent() {

    companion object {
        /** размер разделителя между элементами.  */
        private const val SIZE_WEIGHT_DELEMITER = 5
        /** размер разделителя между элементами.  */
        const val SIZE_HEIGHT_STATUSBAR = 24
    }

    /**
     * Указать новое значение для конкретного раздела.
     * @param text значение
     * @param numItem номер элемента
     */
    fun setText(text: String, numItem: Int) {
        val label = getComponent(numItem * 2) as JLabel
        label.text = StatusBarItem.stringDeNormalize(text)
    }

    /**
     * конструктор строки.
     * @param sizes число разделов
     */
    init {
        layout = BoxLayout(this, BoxLayout.LINE_AXIS)
        add(StatusBarItem("", 0))
        add(Box.createHorizontalGlue())
        for (i in 1 until sizes.size) {
            add(StatusBarItem(" ", sizes[i]))
            val rigidArea = Box.createRigidArea(
                Dimension(
                    SIZE_WEIGHT_DELEMITER,
                    SIZE_HEIGHT_STATUSBAR
                )
            ) as Filler
            rigidArea.foreground = Color(0xff, 0xff, 0xff)
            add(rigidArea)
        }
    }
}