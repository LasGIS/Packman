package fkn.dlaskina.packman.panels

import fkn.dlaskina.util.Util
import org.apache.log4j.LogManager
import java.awt.AWTEvent
import java.awt.BorderLayout
import java.awt.FlowLayout
import java.awt.Frame
import java.awt.GridLayout
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.WindowEvent
import javax.swing.BorderFactory
import javax.swing.JButton
import javax.swing.JDialog
import javax.swing.JLabel
import javax.swing.JPanel

/**
 * Created by IntelliJ IDEA.
 * @author VLaskin
 * @version 1.0
 */
class MainFrameAboutBox(parent: Frame?) : JDialog(parent), ActionListener {

    private val button1 = JButton()

    companion object {
        private val log = LogManager.getLogger(this::class.java)
    }

    /**
     * Component initialization.
     * @throws Exception some Exception
     */
    @Throws(Exception::class)
    private fun jbInit() {
    }

    /**
     * Overridden so we can exit when window is closed.
     * @param e Action Event
     */
    override fun processWindowEvent(e: WindowEvent) {
        if (e.id == WindowEvent.WINDOW_CLOSING) {
            cancel()
        }
        super.processWindowEvent(e)
    }

    /**
     * Close the dialog.
     */
    fun cancel() {
        dispose()
    }

    /**
     * Close the dialog on a button event.
     * @param e Action Event
     */
    override fun actionPerformed(e: ActionEvent) {
        if (e.source === button1) {
            cancel()
        }
    }

    /**
     * Create and show dialog.
     * @param parent parent frame
     */
    init {
        enableEvents(AWTEvent.WINDOW_EVENT_MASK)
        try {
            val imageLabel = JLabel()
            imageLabel.icon = Util.loadImageIcon("Evolution.GIF")
            title = "About"
            val panel1 = JPanel()
            panel1.layout = BorderLayout()
            val panel2 = JPanel()
            panel2.layout = BorderLayout()
            val insetsPanel1 = JPanel()
            val flowLayout1 = FlowLayout()
            insetsPanel1.layout = flowLayout1
            val insetsPanel2 = JPanel()
            insetsPanel2.layout = flowLayout1
            insetsPanel2.border = BorderFactory.createEmptyBorder(10, 10, 10, 10)
            val gridLayout1 = GridLayout()
            gridLayout1.rows = 4
            gridLayout1.columns = 1
            val label1 = JLabel()
            val product = "Hobby packman support"
            label1.text = product
            val label2 = JLabel()
            val version = "1.0"
            label2.text = version
            val label3 = JLabel()
            val copyright = "Copyright (c) 2016"
            label3.text = copyright
            val label4 = JLabel()
            val comments = "Control for packman throws Bluetooth"
            label4.text = comments
            val insetsPanel3 = JPanel()
            insetsPanel3.layout = gridLayout1
            insetsPanel3.border = BorderFactory.createEmptyBorder(10, 60, 10, 10)
            button1.text = "Ok"
            button1.addActionListener(this)
            insetsPanel2.add(imageLabel, null)
            panel2.add(insetsPanel2, BorderLayout.WEST)
            this.contentPane.add(panel1, null)
            insetsPanel3.add(label1, null)
            insetsPanel3.add(label2, null)
            insetsPanel3.add(label3, null)
            insetsPanel3.add(label4, null)
            panel2.add(insetsPanel3, BorderLayout.CENTER)
            insetsPanel1.add(button1, null)
            panel1.add(insetsPanel1, BorderLayout.SOUTH)
            panel1.add(panel2, BorderLayout.NORTH)
            isResizable = true
        } catch (ex: Exception) {
            log.error(ex.message, ex)
        }
    }
}