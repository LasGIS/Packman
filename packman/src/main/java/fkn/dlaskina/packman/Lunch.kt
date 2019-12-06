package fkn.dlaskina.packman

import fkn.dlaskina.packman.panels.MainFrame
import org.apache.log4j.LogManager
import org.apache.log4j.Logger
import java.awt.Dimension
import java.awt.Toolkit
import javax.swing.JFrame
import javax.swing.UIManager
import javax.swing.WindowConstants

private val log: Logger = LogManager.getLogger(Lunch::class.java)

/**
 * <description>
 *
 * @author VLaskin
 * @since <pre>27.11.2019</pre>
 */
class Lunch() {

    init {
        val frame = MainFrame
        frame.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE

        // Validate panels that have preset sizes
        // Pack panels that have useful preferred size info,
        // e.g. from their layout
        frame.validate()

        //Center the window
        val screenSize: Dimension = Toolkit.getDefaultToolkit().screenSize
        val frameSize = frame.size
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width
        }
        frame.setLocation(
                (screenSize.width - frameSize.width) / 2,
                (screenSize.height - frameSize.height) / 2
        )
        frame.isVisible = true
    }
}

/**
 * Главный запуск программы.
 * @param args аргументы командной строки
 */
fun main(args: Array<String>) {
    try {
        JFrame.setDefaultLookAndFeelDecorated(true)
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
    } catch (ex: Exception) {
        log.error(ex.message, ex)
    }
    Lunch()
}

