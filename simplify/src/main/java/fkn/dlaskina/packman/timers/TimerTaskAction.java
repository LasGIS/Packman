package fkn.dlaskina.packman.timers;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.TimerTask;

import fkn.dlaskina.packman.element.ActiveElemental;
import fkn.dlaskina.packman.map.GameOverException;
import fkn.dlaskina.packman.map.Matrix;
import fkn.dlaskina.packman.panels.GameOverDialog;
import fkn.dlaskina.packman.panels.MainFrame;
import fkn.dlaskina.packman.panels.MapPanel;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Этот класс отвечает за движение.
 * @author Vladimir Laskin
 * @version 1.0
 */
public class TimerTaskAction extends TimerTask {

    private static final Logger LOG = LogManager.getLogger(TimerTaskAction.class);

    final MainFrame frame;

    public TimerTaskAction(final MainFrame frame) {
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
            redrawMap();
        } catch (final GameOverException ex) {
            redrawMap();
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

    private void redrawMap() {
        final MapPanel mapPanel = frame.getMapPanel();
        final Graphics gr = mapPanel.getGraphics();
        if (gr != null) {
            mapPanel.setRedrawMap(true);
            mapPanel.update(gr);
        }
    }
}
