/**
 * @(#)MainFrame.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

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
import java.awt.event.*;

import fkn.dlaskina.component.StatusBar;
import fkn.dlaskina.util.SettingMenuItem;
import fkn.dlaskina.util.SettingToolBarItem;
import fkn.dlaskina.util.Util;
import org.apache.log4j.Logger;
import org.apache.log4j.LogManager;

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
            configPanel.setMainFrame(this);

            splitPane.add(configPanel, JSplitPane.RIGHT);
            splitPane.add(mapPanel, JSplitPane.LEFT);
            //splitPane.setLastDividerLocation(size.width - 300);
            //splitPane.setDividerLocation(size.width - 300);
            splitPane.setResizeWeight(1);
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

    /**
     * Invoked when the component's size changes.
     * @param e event which indicates that a component moved
     */
    public void componentResized(final ComponentEvent e) {
        if (e.getComponent().equals(mapPanel)) {
            mapPanel.setRedrawMap(true);
        }
    }

    /**
     * Invoked when the component's position changes.
     * @param e event which indicates that a component moved
     */
    public void componentMoved(final ComponentEvent e) {

    }

    /**
     * Invoked when the component has been made visible.
     * @param e event which indicates that a component moved
     */
    public void componentShown(final ComponentEvent e) {

    }

    /**
     * Invoked when the component has been made invisible.
     * @param e event which indicates that a component moved
     */
    public void componentHidden(final ComponentEvent e) {

    }
}