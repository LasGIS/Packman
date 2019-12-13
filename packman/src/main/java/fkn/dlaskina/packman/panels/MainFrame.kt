package fkn.dlaskina.packman.panels

import fkn.dlaskina.component.StatusBar
import fkn.dlaskina.packman.map.GameOverException
import fkn.dlaskina.packman.map.Matrix.createMatrix
import fkn.dlaskina.packman.timers.TimersControl
import fkn.dlaskina.util.SettingMenuItem
import fkn.dlaskina.util.SettingToolBarItem
import fkn.dlaskina.util.Util
import mu.KotlinLogging
import java.awt.AWTEvent
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.ComponentEvent
import java.awt.event.ComponentListener
import javax.swing.JDialog
import javax.swing.JFrame
import javax.swing.JMenuBar
import javax.swing.JSplitPane
import javax.swing.JToolBar

private val log = KotlinLogging.logger{}

/**
 * Created by IntelliJ IDEA.
 *
 * @author VLaskin
 * @version 1.0
 */
object MainFrame : JFrame(), ComponentListener {

    /** размеры строки состояния.  */
    private val STATUS_BAR_SIZES = intArrayOf(0, 100, 300)
    /** ширина кнопки на главной панели инструментов.  */
    private const val TOOL_BAR_WIDTH = 27
    /** высота кнопки на главной панели инструментов.  */
    private const val TOOL_BAR_HEIGHT = 27

    /** Строка состояния.  */
    private val jStatusBar = StatusBar(STATUS_BAR_SIZES)
    /** Панель с картой.  */
    val mapPanel = MapPanel()
    /** панель конфигурации.  */
    private val configPanel = ConfigPanel()
    /** Настройка главного меню.  */
    private val menuSetting = arrayOf(
        SettingMenuItem(
            "File", "openFile.gif", "", null, arrayOf(
            SettingMenuItem(
                "Exit", null, "Закрываем приложение", ActionListener { event: ActionEvent? -> jMenuFileExitAction() }, null
            ))),
        SettingMenuItem(
            "Help", "help.gif", "Всякого рода вспоможение", null, arrayOf(
            SettingMenuItem(
                "About", "help.gif", "Кто ЭТО сделал!", ActionListener { event: ActionEvent? -> jMenuHelpAboutAction() }, null
            )
        ))
    )
    /** Настройка главной панели инструментов.  */
    private val toolBarSetting = arrayOf(
        SettingToolBarItem(
            "Помощь", "help.gif", "Help",
            TOOL_BAR_WIDTH, TOOL_BAR_HEIGHT, ActionListener { jMenuHelpAboutAction() }),
        SettingToolBarItem(
            "Game Over Dialog", null, "Game Over Dialog",
            TOOL_BAR_WIDTH, TOOL_BAR_HEIGHT, ActionListener { jMenuGameOverAction() }),
        SettingToolBarItem(
            "Exit", null, "Exit from program",
            TOOL_BAR_WIDTH, TOOL_BAR_HEIGHT, ActionListener { jMenuFileExitAction() }))

    /**
     * File | Exit action performed.
     */
    private fun jMenuFileExitAction() {
        // сохраняем локальную конфигурацию
        System.exit(0)
    }

    /**
     * Help | About action performed.
     */
    private fun jMenuHelpAboutAction() {
        showDialog(MainFrameAboutBox(this))
    }

    /**
     * Help | About action performed.
     */
    private fun jMenuGameOverAction() {
        showDialog(GameOverDialog(GameOverException(false, "from menu")))
    }

    private fun showDialog(dlg: JDialog) {
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
     * Вывод сообщений на statusBar.
     * @param out строка сообщения
     * @param numItem номер элемента статусной строки
     */
    fun outStatus(out: String, numItem: Int) {
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
            contentPane.layout = BorderLayout()
            title = "Run First Blood"

            /* настраиваем главное меню */
            jMenuBar = JMenuBar()
            for (aSetMenu in menuSetting) {
                jMenuBar.add(Util.createImageMenuItem(aSetMenu))
            }

            /* настраиваем главный ToolBar */
            val toolBar = JToolBar()
            for (aSetToolBar in toolBarSetting) {
                toolBar.add(Util.createImageButton(aSetToolBar))
            }

            /* разделительная панелька */
            val splitPane = JSplitPane()
            splitPane.isContinuousLayout = true
//            mapPanel.setMainFrame(this)
            mapPanel.addComponentListener(this)
            contentPane.add(toolBar, BorderLayout.NORTH)
            contentPane.add(jStatusBar, BorderLayout.SOUTH)
            contentPane.add(splitPane, BorderLayout.CENTER)
            splitPane.add(configPanel, JSplitPane.RIGHT)
            splitPane.add(mapPanel, JSplitPane.LEFT)
//            splitPane.lastDividerLocation = size.width - 300;
//            splitPane.dividerLocation = size.width - 300;
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