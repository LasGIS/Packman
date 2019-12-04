package fkn.dlaskina.packman.element

import java.awt.Color
import java.awt.Graphics
import java.awt.Rectangle

/**
 * Definition of the Surprise class
 * @author VLaskin
 * @since 26.03.2016.
 */
class Stone : Elemental(ElementalType.Stone) {

    companion object {
        private val FILL_COLOR = Color(125, 128, 128)
    }

    override fun paint(gr: Graphics, rect: Rectangle, frame: Int) {
        gr.color = FILL_COLOR
        gr.fillRect(rect.x, rect.y, rect.width, rect.height)
    }
}