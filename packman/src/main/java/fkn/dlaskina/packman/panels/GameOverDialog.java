package fkn.dlaskina.packman.panels;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import java.awt.AWTEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Created by IntelliJ IDEA.
 * @author VLaskin
 * @version 1.0
 */
public class GameOverDialog extends JDialog implements ActionListener {

    private static final Logger LOG = LogManager.getLogger(GameOverDialog.class);

    private JButton button1 = new JButton();

    /**
     * Create and show dialog.
     */
    public GameOverDialog() {
        super();
        enableEvents(AWTEvent.WINDOW_EVENT_MASK);
        try {
            getContentPane().add(new JLabel("Game Over!"));
            setResizable(true);
        } catch (final Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

    /**
     * Overridden so we can exit when window is closed.
     * @param e Action Event
     */
    protected void processWindowEvent(final WindowEvent e) {
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            cancel();
        }
        super.processWindowEvent(e);
    }

    /**
     * Close the dialog.
     */
    void cancel() {
        dispose();
    }

    /**
     * Close the dialog on a button event.
     * @param e Action Event
     */
    public void actionPerformed(final ActionEvent e) {
        if (e.getSource() == button1) {
            cancel();
        }
    }
}