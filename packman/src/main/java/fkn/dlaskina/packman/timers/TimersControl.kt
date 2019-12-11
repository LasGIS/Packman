package fkn.dlaskina.packman.timers

import fkn.dlaskina.packman.panels.MainFrame
import fkn.dlaskina.packman.panels.MainFrame.mapPanel
import mu.KotlinLogging
import java.util.*

private val log = KotlinLogging.logger{}

/**
 * The Class TimersControl.
 * @author Vladimir Laskin
 * @version 1.0
 */
object TimersControl {
    private var redrawTimer: Timer? = null
    private var actionTimer: Timer? = null
    //    private static MainFrame mainFrame;
    fun startTimers() {
        redrawTimer = Timer()
        redrawTimer!!.schedule(TimerTaskRedraw(mapPanel), 33, 33)
        actionTimer = Timer()
        actionTimer!!.schedule(TimerTaskAction(MainFrame), 50, 50)
        mapPanel.clearBackground()
    }

    fun stopTimers() {
        if (redrawTimer != null) {
            redrawTimer!!.cancel()
            redrawTimer!!.purge()
        }
        if (actionTimer != null) {
            actionTimer!!.cancel()
            actionTimer!!.purge()
        }
    }
}