package fkn.dlaskina.packman.panels;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;

import fkn.dlaskina.component.StatusBar;
import fkn.dlaskina.packman.map.GameOverException;
import fkn.dlaskina.packman.map.Matrix;
import fkn.dlaskina.packman.timers.TimersControl;
import fkn.dlaskina.util.SettingMenuItem;
import fkn.dlaskina.util.SettingToolBarItem;
import fkn.dlaskina.util.Util;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Created by IntelliJ IDEA.
 *
 * @author VLaskin
 * @version 1.0
 */
public class MainFrame extends JFrame implements ComponentListener {

    private static final Logger LOG = LogManager.getLogger(MainFrame.class);

    /** размеры строки состояния. */
    private static final int[] STATUS_BAR_SIZES = new int[] {0, 100, 200};
    /** Строка состояния. */
    private StatusBar jStatusBar = new StatusBar(STATUS_BAR_SIZES);
    /** Панель с картой. */
    private MapPanel mapPanel = new MapPanel();
    /** панель конфигурации. */
    private ConfigPanel configPanel;

    /** ширина кнопки на главной панели инструментов. */
    private static final int TOOL_BAR_WIDTH = 27;
    /** высота кнопки на главной панели инструментов. */
    private static final int TOOL_BAR_HEIGHT = 27;

    /** Настройка главного меню. */
    private final SettingMenuItem[] menuSetting = {
        new SettingMenuItem(
            "File", "openFile.gif", "", null,
            new SettingMenuItem[] {
                new SettingMenuItem(
                    "Exit", null, "Закрываем приложение", MainFrame.this::jMenuFileExitAction, null
                ),
            }
        ),
        new SettingMenuItem(
            "Help", "help.gif", "Всякого рода вспоможение", null,
            new SettingMenuItem[] {
                new SettingMenuItem(
                    "About", "help.gif", "Кто ЭТО сделал!", MainFrame.this::jMenuHelpAboutAction, null
                )
            }
        )
    };

    /** Настройка главной панели инструментов. */
    private final SettingToolBarItem[] toolBarSetting = {
        new SettingToolBarItem(
            "Помощь", "help.gif", "Help",
            TOOL_BAR_WIDTH, TOOL_BAR_HEIGHT, MainFrame.this::jMenuHelpAboutAction
        ),
/*
        new SettingToolBarItem(
            "Game Over Dialog", null, "Game Over Dialog",
            TOOL_BAR_WIDTH, TOOL_BAR_HEIGHT, MainFrame.this::jMenuGameOverAction
        ),
*/
        new SettingToolBarItem(
            "Exit", null, "Exit from programm",
            TOOL_BAR_WIDTH, TOOL_BAR_HEIGHT, MainFrame.this::jMenuFileExitAction
        )
    };

    /**
     * Construct the frame.
     */
    public MainFrame() {
        enableEvents(AWTEvent.WINDOW_EVENT_MASK);
        try {
            final Dimension size = new Dimension(800, 600);
            final JPanel contentPane = (JPanel) this.getContentPane();
            contentPane.setLayout(new BorderLayout());
            this.setSize(size);
            this.setTitle("Run First Blood");

            /* настраиваем главное меню */
            final JMenuBar menuBar = new JMenuBar();
            for (SettingMenuItem aSetMenu : menuSetting) {
                menuBar.add(Util.createImageMenuItem(aSetMenu));
            }
            /* настраиваем главный ToolBar */
            final JToolBar toolBar = new JToolBar();
            for (SettingToolBarItem aSetToolBar : toolBarSetting) {
                toolBar.add(Util.createImageButton(aSetToolBar));
            }
            /* разделительная панелька */
            final JSplitPane splitPane = new JSplitPane();
            splitPane.setContinuousLayout(true);
            mapPanel.setMainFrame(this);
            mapPanel.addComponentListener(this);
            this.setJMenuBar(menuBar);
            contentPane.add(toolBar, BorderLayout.NORTH);
            contentPane.add(jStatusBar, BorderLayout.SOUTH);
            contentPane.add(splitPane, BorderLayout.CENTER);
            configPanel = new ConfigPanel();

            splitPane.add(configPanel, JSplitPane.RIGHT);
            splitPane.add(mapPanel, JSplitPane.LEFT);
            //splitPane.setLastDividerLocation(size.width - 300);
            //splitPane.setDividerLocation(size.width - 300);
            splitPane.setResizeWeight(1);

            // создаём матрицу и запускаем
            Matrix.INSTANCE.createMatrix(false);
            TimersControl.setMainFrame(this);
            TimersControl.startTimers();

            mapPanel.requestFocusInWindow();
        } catch (final Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

    /**
     * File | Exit action performed.
     *
     * @param event Action Event
     */
    public void jMenuFileExitAction(final ActionEvent event) {
        // сохраняем локальную конфигурацию
        System.exit(0);
    }

    /**
     * Help | About action performed.
     * @param event Action Event
     */
    public void jMenuHelpAboutAction(final ActionEvent event) {
        final MainFrameAboutBox dlg = new MainFrameAboutBox(this);
        final Dimension dlgSize = dlg.getPreferredSize();
        final Dimension frmSize = getSize();
        final Point loc = getLocation();
        dlg.setLocation(
            (frmSize.width - dlgSize.width) / 2 + loc.x,
            (frmSize.height - dlgSize.height) / 2 + loc.y
        );
        dlg.setModal(true);
        dlg.pack();
        dlg.setVisible(true);
    }

    /**
     * Help | About action performed.
     * @param event Action Event
     */
    public void jMenuGameOverAction(final ActionEvent event) {
        final GameOverDialog dlg = new GameOverDialog(new GameOverException(false, "from menu"));
        final Dimension dlgSize = dlg.getPreferredSize();
        final Dimension frmSize = getSize();
        final Point loc = getLocation();
        dlg.setLocation(
            (frmSize.width - dlgSize.width) / 2 + loc.x,
            (frmSize.height - dlgSize.height) / 2 + loc.y
        );
        dlg.setModal(true);
        dlg.pack();
        dlg.setVisible(true);
    }

    /**
     * Overridden so we can exit when window is closed.
     * @param e оконное событие
     */
    protected void processWindowEvent(final WindowEvent e) {
        super.processWindowEvent(e);
    }

    /**
     * Вывод сообщений на statusBar.
     * @param out строка сообщения
     * @param numItem номер элемента статусной строки
     */
    public void outStatus(final String out, final int numItem) {
        jStatusBar.setText(out, numItem);
    }

    /**
     * Вернуть панель с картой.
     * @return панель с картой
     */
    public MapPanel getMapPanel() {
        return mapPanel;
    }

    /**
     * Вернуть панель конфигурации.
     * @return панель конфигурации
     */
    public ConfigPanel getConfigPanel() {
        return configPanel;
    }

    @Override
    public void componentResized(final ComponentEvent e) {
        if (e.getComponent().equals(mapPanel)) {
            mapPanel.setRedrawMap(true);
        }
    }

    @Override
    public void componentMoved(final ComponentEvent e) {}

    @Override
    public void componentShown(final ComponentEvent e) {}

    @Override
    public void componentHidden(final ComponentEvent e) {}
}