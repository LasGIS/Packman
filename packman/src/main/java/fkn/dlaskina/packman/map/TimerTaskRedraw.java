package fkn.dlaskina.packman.map;

import java.awt.Graphics;
import java.util.TimerTask;

import fkn.dlaskina.packman.panels.MapPanel;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Этот класс отвечает за движение.
 * @author Vladimir Laskin
 * @version 1.0
 */
public class TimerTaskRedraw extends TimerTask {

    private static final Logger LOG = LogManager.getLogger(TimerTaskRedraw.class);

    private MapPanel mapPanel;
    private int frame = 0;

    public TimerTaskRedraw(final MapPanel mapPanel) {
        this.mapPanel = mapPanel;
    }

    @Override
    public void run() {
        final Graphics gr = mapPanel.getGraphics();
        if (gr != null) {
            mapPanel.setRedrawMap(true);
            mapPanel.setFrame(frame);
            if (++frame > 39) frame = 0;
            mapPanel.update(gr);
        }
    }
}
