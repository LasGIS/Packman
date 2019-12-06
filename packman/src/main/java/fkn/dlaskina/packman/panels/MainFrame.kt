package fkn.dlaskina.packman.panels

import fkn.dlaskina.component.StatusBar
import fkn.dlaskina.packman.map.GameOverException
import fkn.dlaskina.packman.map.Matrix.createMatrix
import fkn.dlaskina.packman.timers.TimersControl
import fkn.dlaskina.util.SettingMenuItem
import fkn.dlaskina.util.SettingToolBarItem
import fkn.dlaskina.util.Util
import org.apache.log4j.LogManager
import java.awt.AWTEvent
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.event.*
import javax.swing.*

/**
 * Created by IntelliJ IDEA.
 *
 * @author VLaskin
 * @version 1.0
 */
object MainFrame : JFrame(), ComponentListener {

    private val log = LogManager.getLogger(MainFrame::class.java)
    /** размеры строки состояния.  */
    private val STATUS_BAR_SIZES = intArrayOf(0, 100, 200)
    /** ширина кнопки на главной панели инструментов.  */
    private const val TOOL_BAR_WIDTH = 27
    /** высота кнопки на главной панели инструментов.  */
    private const val TOOL_BAR_HEIGHT = 27

    /** Строка состояния.  */
    private val jStatusBar = StatusBar(STATUS_BAR_SIZES)
    /**
     * Вернуть панель с картой.
     * @return панель с картой
     */
    /** Панель с картой.  */
    val mapPanel = MapPanel()
    /**
     * Вернуть панель конфигурации.
     * @return панель конфигурации
     */
    /** панель конфигурации.  */
    var configPanel: ConfigPanel? = null
    /** Настройка главного меню.  */
    private val menuSetting = arrayOf(
            SettingMenuItem(
                    "File", "openFile.gif", "", null, arrayOf(
                    SettingMenuItem(
                            "Exit", null, "Закрываем приложение", ActionListener { event: ActionEvent? -> jMenuFileExitAction(event) }, null
                    ))),
            SettingMenuItem(
                    "Help", "help.gif", "Всякого рода вспоможение", null, arrayOf(
                    SettingMenuItem(
                            "About", "help.gif", "Кто ЭТО сделал!", ActionListener { event: ActionEvent? -> jMenuHelpAboutAction(event) }, null
                    )
            ))
    )
    /** Настройка главной панели инструментов.  */
    private val toolBarSetting = arrayOf(
            SettingToolBarItem(
                    "Помощь", "help.gif", "Help",
                    TOOL_BAR_WIDTH, TOOL_BAR_HEIGHT, ActionListener { event: ActionEvent? -> jMenuHelpAboutAction(event) }),
/*
        new SettingToolBarItem(
            "Game Over Dialog", null, "Game Over Dialog",
            TOOL_BAR_WIDTH, TOOL_BAR_HEIGHT, MainFrame.this::jMenuGameOverAction
        ),
*/
            SettingToolBarItem(
                    "Exit", null, "Exit from programm",
                    TOOL_BAR_WIDTH, TOOL_BAR_HEIGHT, ActionListener { event: ActionEvent? -> jMenuFileExitAction(event) })
    )

    /**
     * File | Exit action performed.
     *
     * @param event Action Event
     */
    private fun jMenuFileExitAction(event: ActionEvent?) { // сохраняем локальную конфигурацию
        System.exit(0)
    }

    /**
     * Help | About action performed.
     * @param event Action Event
     */
    private fun jMenuHelpAboutAction(event: ActionEvent?) {
        val dlg = MainFrameAboutBox(this)
        val dlgSize = dlg.preferredSize
        val frmSize = size
        val loc = location
        dlg.setLocation(
                (frmSize.width - dlgSize.width) / 2 + loc.x,
                (frmSize.height - dlgSize.height) / 2 + loc.y
        )
        dlg.isModal = true
        dlg.pack()
        dlg.isVisible = true
    }

    /**
     * Help | About action performed.
     * @param event Action Event
     */
    private fun jMenuGameOverAction(event: ActionEvent?) {
        val dlg = GameOverDialog(GameOverException(false, "from menu"))
        val dlgSize = dlg.preferredSize
        val frmSize = size
        val loc = location
        dlg.setLocation(
                (frmSize.width - dlgSize.width) / 2 + loc.x,
                (frmSize.height - dlgSize.height) / 2 + loc.y
        )
        dlg.isModal = true
        dlg.pack()
        dlg.isVisible = true
    }

    /**
     * Overridden so we can exit when window is closed.
     * @param e оконное событие
     */
    override fun processWindowEvent(e: WindowEvent) {
        super.processWindowEvent(e)
    }

    /**
     * Вывод сообщений на statusBar.
     * @param out строка сообщения
     * @param numItem номер элемента статусной строки
     */
    fun outStatus(out: String?, numItem: Int) {
        jStatusBar.setText(out, numItem)
    }

    override fun componentResized(e: ComponentEvent) {
        if (e.component == mapPanel) {
            mapPanel.setRedrawMap(true)
        }
    }

    override fun componentMoved(e: ComponentEvent) {}
    override fun componentShown(e: ComponentEvent) {}
    override fun componentHidden(e: ComponentEvent) {}

    /**
     * Construct the frame.
     */
    init {
        enableEvents(AWTEvent.WINDOW_EVENT_MASK)
        try {
            size = Dimension(800, 600)
            val contentPane = this.contentPane as JPanel
            contentPane.layout = BorderLayout()
            title = "Run First Blood"
            /* настраиваем главное меню */
            val menuBar = JMenuBar()
            for (aSetMenu in menuSetting) {
                menuBar.add(Util.createImageMenuItem(aSetMenu))
            }
            /* настраиваем главный ToolBar */
            val toolBar = JToolBar()
            for (aSetToolBar in toolBarSetting) {
                toolBar.add(Util.createImageButton(aSetToolBar))
            }
            /* разделительная панелька */
            val splitPane = JSplitPane()
            splitPane.isContinuousLayout = true
            mapPanel.setMainFrame(this)
            mapPanel.addComponentListener(this)
            this.jMenuBar = menuBar
            contentPane.add(toolBar, BorderLayout.NORTH)
            contentPane.add(jStatusBar, BorderLayout.SOUTH)
            contentPane.add(splitPane, BorderLayout.CENTER)
            configPanel = ConfigPanel()
            splitPane.add(configPanel, JSplitPane.RIGHT)
            splitPane.add(mapPanel, JSplitPane.LEFT)
//splitPane.setLastDividerLocation(size.width - 300);
//splitPane.setDividerLocation(size.width - 300);
            splitPane.resizeWeight = 1.0
            // создаём матрицу и запускаем
            createMatrix(false)
            TimersControl.startTimers()
            mapPanel.requestFocusInWindow()
        } catch (ex: Exception) {
            log.error(ex.message, ex)
        }
    }
}