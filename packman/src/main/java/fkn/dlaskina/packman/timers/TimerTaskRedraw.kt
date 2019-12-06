package fkn.dlaskina.packman.timers

import fkn.dlaskina.packman.panels.MapPanel
import org.apache.log4j.LogManager
import java.util.*

/**
 * Этот класс отвечает за движение.
 * @author Vladimir Laskin
 * @version 1.0
 */
class TimerTaskRedraw(private val mapPanel: MapPanel) : TimerTask() {

    companion object {
        private val log = LogManager.getLogger(this::class.java)
    }

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