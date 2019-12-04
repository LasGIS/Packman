package fkn.dlaskina.packman.panels

import fkn.dlaskina.packman.map.GameOverException
import fkn.dlaskina.packman.map.Matrix
import fkn.dlaskina.packman.timers.TimersControl
import fkn.dlaskina.packman.util.Alog
import org.apache.log4j.LogManager
import java.awt.*
import java.awt.event.WindowEvent
import javax.swing.JButton
import javax.swing.JDialog
import javax.swing.JLabel
import javax.swing.JPanel
import kotlin.system.exitProcess

/**
 * Create and show dialog.
 * @param goExc Game Over Exception
 * @author VLaskin
 * @version 1.0
 */
class GameOverDialog(goExc: GameOverException) : JDialog() {

    companion object {
        private val log = LogManager.getLogger(this::class.java)
    }

    init {
        log.info("проверка log.info()")
        enableEvents(AWTEvent.WINDOW_EVENT_MASK)
        TimersControl.stopTimers()
        try {
            val labelPanel = JPanel(GridBagLayout())
            val c = GridBagConstraints()
            c.insets = Insets(5, 5, 5, 5)
            c.fill = GridBagConstraints.BOTH
            c.gridx = 0
            c.gridy = 0
            labelPanel.add(JLabel(if (goExc.isWin) "Win!!!" else "Game Over"), c)
            c.gridx = 0
            c.gridy = 1
            labelPanel.add(JLabel(goExc.message), c)

            val buttonPanel = JPanel(GridBagLayout())
            c.gridx = 0
            c.gridy = 0
            val startButton = JButton("Start This")
            startButton.addActionListener {
                Matrix.createMatrix(false)
                TimersControl.startTimers()
                dispose()
            }
            val newLevelButton = JButton("New Level")
            newLevelButton.isEnabled = goExc.isWin
            newLevelButton.addActionListener {
                Matrix.createMatrix(true)
                TimersControl.startTimers()
                dispose()
            }
            val stopButton = JButton("Stop")
            stopButton.addActionListener { exitProcess(0) }
            buttonPanel.add(startButton, c)
            c.gridx = 1
            buttonPanel.add(stopButton, c)
            c.gridx = 2
            buttonPanel.add(newLevelButton, c)

            val contentPane = contentPane
            contentPane.layout = BorderLayout()
            contentPane.add(labelPanel, BorderLayout.NORTH)
            contentPane.add(buttonPanel, BorderLayout.SOUTH)
            isResizable = true
        } catch (ex: Exception) {
            log.error(ex.message, ex)
        }

    }

    /**
     * Overridden so we can exit when window is closed.
     * @param e Action Event
     */
    override fun processWindowEvent(e: WindowEvent) {
        if (e.id == WindowEvent.WINDOW_CLOSING) {
            TimersControl.startTimers()
            dispose()
        }
        super.processWindowEvent(e)
    }
}