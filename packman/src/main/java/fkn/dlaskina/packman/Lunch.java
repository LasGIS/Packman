package fkn.dlaskina.packman;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import java.awt.Dimension;
import java.awt.Toolkit;

import fkn.dlaskina.packman.panels.MainFrame;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * PackMan runs in cells of Peace, with a keyboard controls, eats prizes and dodging enemies.
 * @author Vladimir Laskin
 * @version 1.0
 */
public final class Lunch {

    private static final Logger LOG = LogManager.getLogger(Lunch.class);

    /**
     * Construct the application.
     */
    private Lunch() {

        final MainFrame frame = new MainFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Validate panels that have preset sizes
        // Pack panels that have useful preferred size info,
        // e.g. from their layout
        frame.validate();

        //Center the window
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        final Dimension frameSize = frame.getSize();
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        frame.setLocation(
            (screenSize.width - frameSize.width) / 2,
            (screenSize.height - frameSize.height) / 2
        );
        frame.setVisible(true);
    }

    /**
     * Главный запуск программы.
     * @param args аргументы командной строки
     */
    public static void main(final String[] args) {

        try {
            JFrame.setDefaultLookAndFeelDecorated(true);
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (final Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }

        new Lunch();
    }
}