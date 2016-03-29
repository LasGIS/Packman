package fkn.dlaskina.packman.panels;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowEvent;

import fkn.dlaskina.packman.map.Matrix;
import fkn.dlaskina.packman.timers.TimersControl;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Created by IntelliJ IDEA.
 * @author VLaskin
 * @version 1.0
 */
public class GameOverDialog extends JDialog {

    private static final Logger LOG = LogManager.getLogger(GameOverDialog.class);

    /**
     * Create and show dialog.
     */
    public GameOverDialog() {
        super();
        enableEvents(AWTEvent.WINDOW_EVENT_MASK);
        Matrix.createMatrix("matrix.txt");
        TimersControl.stopTimers();
        try {
            final JPanel labelPanel = new JPanel(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            c.insets = new Insets(5, 5, 5, 5);
            c.fill = GridBagConstraints.BOTH;
            c.gridx = 0;
            c.gridy = 0;
            labelPanel.add(new JLabel("Game Over!"), c);

            final JPanel buttonPanel = new JPanel(new GridBagLayout());
            c.gridx = 0;
            c.gridy = 0;
            JButton startButton = new JButton("Start This");
            startButton.addActionListener(e -> {
                Matrix.createMatrix("matrix.txt");
                TimersControl.startTimers();
                dispose();
            });
            JButton newLevelButton = new JButton("New Level");
            newLevelButton.addActionListener(e -> {
                Matrix.createMatrix("matrix1.txt");
                TimersControl.startTimers();
                dispose();
            });
            buttonPanel.add(startButton, c);
            c.gridx = 1;
            buttonPanel.add(newLevelButton, c);

            Container contentPane = getContentPane();
            contentPane.setLayout(new BorderLayout());
            contentPane.add(labelPanel, BorderLayout.NORTH);
            contentPane.add(buttonPanel, BorderLayout.SOUTH);
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
            dispose();
        }
        super.processWindowEvent(e);
    }
}