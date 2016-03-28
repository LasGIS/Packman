/**
 * @(#)TimeRun.java Title: DA CME Web Application Description: Программная система DA "Ассистент Доктора". Входит в
 * состав  КМЭ - Комплекс медицинский экспертный. Copyright (c) 2016 CME CWISS AG Company. All Rights Reserved.
 */

package fkn.dlaskina.packman.map;

import java.awt.Graphics;
import java.util.TimerTask;

import fkn.dlaskina.packman.element.ActiveElemental;
import fkn.dlaskina.packman.panels.MapPanel;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Этот класс отвечает за движение.
 * @author Vladimir Laskin
 * @version 1.0
 */
public class TimeRun extends TimerTask {

    private static final Logger LOG = LogManager.getLogger(Matrix.class);

    private MapPanel mapPanel;
    private int frame = 0;

    public TimeRun(final MapPanel mapPanel) {
        this.mapPanel = mapPanel;
    }

    @Override
    public void run() {
        if ((frame % 20) == 0) {
            LOG.info("Act = " + frame);
            final Matrix matrix = Matrix.getMatrix();
            if (matrix != null) {
                for (final ActiveElemental elm : matrix.getAnimals()) {
                    elm.act();
                }
            }
        }
        final Graphics gr = mapPanel.getGraphics();
        if (gr != null) {
            mapPanel.setRedrawMap(true);
            mapPanel.setFrame(frame);
            //frame++;
            if (++frame > 39) frame = 0;
            mapPanel.update(gr);
        }
    }
}
