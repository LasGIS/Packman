/**
 * @(#)TimerTaskRedraw.java Title: DA CME Web Application Description: Программная система DA "Ассистент Доктора". Входит в
 * состав  КМЭ - Комплекс медицинский экспертный. Copyright (c) 2016 CME CWISS AG Company. All Rights Reserved.
 */

package fkn.dlaskina.packman.map;

import java.util.TimerTask;

import fkn.dlaskina.packman.element.ActiveElemental;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Этот класс отвечает за движение.
 * @author Vladimir Laskin
 * @version 1.0
 */
public class TimerTaskAction extends TimerTask {

    private static final Logger LOG = LogManager.getLogger(TimerTaskAction.class);

    @Override
    public void run() {
        final Matrix matrix = Matrix.getMatrix();
        if (matrix != null) {
            for (final ActiveElemental elm : matrix.getAnimals()) {
                elm.act();
            }
        }
    }
}
