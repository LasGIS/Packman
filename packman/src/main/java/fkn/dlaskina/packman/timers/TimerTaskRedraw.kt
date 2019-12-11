package fkn.dlaskina.packman.timers

import fkn.dlaskina.packman.panels.MapPanel
import mu.KotlinLogging
import java.util.*

private val log = KotlinLogging.logger{}

/**
 * Этот класс отвечает за движение.
 * @author Vladimir Laskin
 * @version 1.0
 */
class TimerTaskRedraw(private val mapPanel: MapPanel) : TimerTask() {

    private var frame = 0
    override fun run() {
        val gr = mapPanel.graphics
        if (gr != null) {
            mapPanel.setRedrawMap(true)
            mapPanel.setFrame(frame)
            if (++frame > 39) frame = 0
            mapPanel.update(gr)
        }
    }

}