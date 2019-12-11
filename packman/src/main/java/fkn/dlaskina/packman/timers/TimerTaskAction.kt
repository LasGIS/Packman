package fkn.dlaskina.packman.timers

import fkn.dlaskina.packman.map.GameOverException
import fkn.dlaskina.packman.map.Matrix
import fkn.dlaskina.packman.panels.GameOverDialog
import mu.KotlinLogging
import java.util.*
import javax.swing.JFrame

private val log = KotlinLogging.logger{}

/**
 * Этот класс отвечает за движение.
 * @author Vladimir Laskin
 * @version 1.0
 */
class TimerTaskAction internal constructor(private val frame: JFrame) : TimerTask() {

    override fun run() {
        try {
            for (elm in Matrix.elements) {
                elm.act()
            }
        } catch (ex: GameOverException) {
            // делаем паузу и даём переместиться на следующу клетку
            try {
                Thread.sleep(100L);
            } catch (ex: InterruptedException) {
                log.error(ex.message, ex);
            }
            val dlg = GameOverDialog(ex)
            val dlgSize = dlg.preferredSize
            val frmSize = frame.size
            val loc = frame.location
            dlg.setLocation(
                (frmSize.width - dlgSize.width) / 2 + loc.x,
                (frmSize.height - dlgSize.height) / 2 + loc.y
            )
            dlg.isModal = true
            dlg.pack()
            dlg.isVisible = true
        }
    }
}