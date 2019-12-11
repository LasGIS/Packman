package fkn.dlaskina.packman.timers;

import fkn.dlaskina.packman.panels.MainFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Timer;

/**
 * The Class TimersControl.
 * @author Vladimir Laskin
 * @version 1.0
 */
public class TimersControl {

    private static final Logger LOG = LoggerFactory.getLogger(TimersControl.class);

    private static Timer actionTimer;
    private static MainFrame mainFrame;

    public static void startTimers() {
        actionTimer = new Timer();
        actionTimer.schedule(new TimerTaskAction(mainFrame), 500, 500);
        mainFrame.getMapPanel().clearBackground();
    }

    public static void stopTimers() {
        if (actionTimer != null) {
            actionTimer.cancel();
            actionTimer.purge();
        }
    }

    public static void setMainFrame(final MainFrame mainFrame) {
        TimersControl.mainFrame = mainFrame;
    }
}
