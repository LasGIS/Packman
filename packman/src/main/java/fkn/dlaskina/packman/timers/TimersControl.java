package fkn.dlaskina.packman.timers;

import java.util.Timer;

import fkn.dlaskina.packman.panels.MainFrame;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * The Class TimersControl.
 * @author Vladimir Laskin
 * @version 1.0
 */
public class TimersControl {

    private static final Logger LOG = LogManager.getLogger(TimersControl.class);

    private static Timer redrawTimer;
    private static Timer actionTimer;
    private static MainFrame mainFrame;

    public static void startTimers() {
        redrawTimer = new Timer();
        redrawTimer.schedule(new TimerTaskRedraw(mainFrame.getMapPanel()), 33, 33);
        actionTimer = new Timer();
        actionTimer.schedule(new TimerTaskAction(mainFrame), 50, 50);
        mainFrame.getMapPanel().clearBackground();
    }

    public static void stopTimers() {
        if (redrawTimer != null) {
            redrawTimer.cancel();
            redrawTimer.purge();
        }
        if (actionTimer != null) {
            actionTimer.cancel();
            actionTimer.purge();
        }
    }

    public static void setMainFrame(final MainFrame mainFrame) {
        TimersControl.mainFrame = mainFrame;
    }
}
