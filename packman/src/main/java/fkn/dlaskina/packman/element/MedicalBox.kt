package fkn.dlaskina.packman.element

import java.awt.Color
import java.awt.Graphics
import java.awt.Rectangle

/**
 * Аптечка для врагов.
 * @author Vladimir Laskin
 * @version 1.0
 */
class MedicalBox: Elemental(ElementalType.MedBox) {

    companion object {
        private val FILL_COLOR = Color(255, 255, 0)
        private val BOUND_COLOR = Color(125, 128, 0)
        private const val BORDER = 5
    }

    override fun paint(gr: Graphics, rect: Rectangle, frame: Int) {
        val x = rect.x + BORDER
        val y = rect.y + BORDER
        val width = rect.width - BORDER * 2
        val height = rect.height - BORDER * 2
        gr.color = FILL_COLOR
        gr.fillRect(x, y, width, height)
        gr.color = BOUND_COLOR
        gr.drawRect(x, y, width, height)
    }
}