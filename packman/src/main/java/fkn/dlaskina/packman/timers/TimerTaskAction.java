package fkn.dlaskina.packman.timers;

import javax.swing.JFrame;
import java.awt.Dimension;
import java.awt.Point;
import java.util.TimerTask;

import fkn.dlaskina.packman.element.ActiveElemental;
import fkn.dlaskina.packman.map.GameOverException;
import fkn.dlaskina.packman.map.Matrix;
import fkn.dlaskina.packman.panels.GameOverDialog;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Этот класс отвечает за движение.
 * @author Vladimir Laskin
 * @version 1.0
 */
public class TimerTaskAction extends TimerTask {

    private static final Logger LOG = LogManager.getLogger(TimerTaskAction.class);

    final JFrame frame;

    public TimerTaskAction(final JFrame frame) {
        this.frame = frame;
    }

    @Override
    public void run() {
        try {
            final Matrix matrix = Matrix.getMatrix();
            if (matrix != null) {
                for (final ActiveElemental elm : matrix.getElements()) {
                    elm.act();
                }
            }
        } catch (final GameOverException ex) {
/*
            // делаем паузу и даём переместиться на следующу клетку
            try {
                Thread.sleep(100l);
            } catch (InterruptedException exc) {
                LOG.error(exc.getMessage(), exc);
            }
*/
            final GameOverDialog dlg = new GameOverDialog(ex);
            final Dimension dlgSize = dlg.getPreferredSize();
            final Dimension frmSize = frame.getSize();
            final Point loc = frame.getLocation();
            dlg.setLocation(
                (frmSize.width - dlgSize.width) / 2 + loc.x,
                (frmSize.height - dlgSize.height) / 2 + loc.y
            );
            dlg.setModal(true);
            dlg.pack();
            dlg.setVisible(true);
        }
    }
}
