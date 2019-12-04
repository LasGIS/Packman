package fkn.dlaskina.packman.element

import java.awt.Color
import java.awt.Graphics
import java.awt.Rectangle
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos

/**
 * Definition of the Surprise class
 * @author VLaskin
 * @since 26.03.2016.
 */
class Surprise(val prizeType: SurpriseType) : Elemental(ElementalType.Surprise) {

    override fun paint(gr: Graphics, rect: Rectangle, frame: Int) {
        val width = ((rect.width - BORDER * 2) * abs(cos(frame * PI / 20))).toInt()
        val x = rect.x + (rect.width - width) / 2
        val y = rect.y + BORDER
        val height = rect.height - BORDER * 2
        if (width > 0) {
            gr.color = fillColor
            gr.fillOval(x, y, width, height)
            gr.color = boundColor
            gr.drawOval(x, y, width, height)
        } else {
            gr.color = fillColor
            gr.drawLine(x, y, x, y + height)
        }
    }

    private val boundColor: Color
        get() = when (prizeType) {
            SurpriseType.aggressive -> AGGRESSIVE_BOUND_COLOR
            SurpriseType.speed -> SPEED_BOUND_COLOR
            else -> BOUND_COLOR
        }

    private val fillColor: Color
        get() = when (prizeType) {
            SurpriseType.aggressive -> AGGRESSIVE_FILL_COLOR
            SurpriseType.speed -> SPEED_FILL_COLOR
            else -> FILL_COLOR
        }

    companion object {
        private val FILL_COLOR = Color(255, 255, 0)
        private val BOUND_COLOR = Color(125, 128, 0)
        private val SPEED_FILL_COLOR = Color(0, 128, 255)
        private val SPEED_BOUND_COLOR = Color(0, 0, 128)
        private val AGGRESSIVE_FILL_COLOR = Color(255, 128, 0)
        private val AGGRESSIVE_BOUND_COLOR = Color(200, 0, 0)
        private const val BORDER = 5
    }

}