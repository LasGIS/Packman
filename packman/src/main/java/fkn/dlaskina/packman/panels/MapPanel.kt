package fkn.dlaskina.packman.panels

import fkn.dlaskina.packman.element.MoveType
import fkn.dlaskina.packman.map.Matrix
import fkn.dlaskina.packman.map.Matrix.packMan
import fkn.dlaskina.packman.map.Matrix.paint
import fkn.dlaskina.packman.map.Matrix.paintGrid
import org.apache.log4j.LogManager
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Graphics
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import java.awt.image.BufferedImage
import javax.swing.JPanel

/**
 * Окно вывода карты.
 * @author VLaskin
 * @version 1.0
 */
class MapPanel : JPanel() {

    companion object {
        private val log = LogManager.getLogger(this::class.java)
        /** серый цвет фона.  */
        val PANEL_GRAY_COLOR = Color(220, 220, 220)
    }

    /** ссылка на MainFrame.  */
    private var mainFrame: MainFrame? = null
    /** Если true, то будем перегружать.  */
    private var isRedrawMap = true
    /** сохраненное изображение.  */
    private var grBackgroundImage: BufferedImage? = null
    /** номер кадра.  */
    private var frame = 0
    /** Стереть фон.  */
    private var isClear = true
    /** Ловим нажатие кнопочек.  */
    private val keyAdapter: KeyAdapter = object : KeyAdapter() {
        override fun keyPressed(e: KeyEvent) {
            val packMan = packMan
            when (e.keyCode) {
                KeyEvent.VK_NUMPAD4, KeyEvent.VK_LEFT ->   // влево
                    packMan.setMove(MoveType.LEFT)
                KeyEvent.VK_NUMPAD6, KeyEvent.VK_RIGHT ->  // вправо
                    packMan.setMove(MoveType.RIGHT)
                KeyEvent.VK_NUMPAD8, KeyEvent.VK_UP ->     // вверх
                    packMan.setMove(MoveType.UP)
                KeyEvent.VK_NUMPAD2, KeyEvent.VK_DOWN ->   // вниз
                    packMan.setMove(MoveType.DOWN)
                else -> {/*nothing*/}
            }
            //log.info("keyPressed = " + e.getKeyCode());
        }
    }

    /**
     * Конструктор.
     */
    init {
        try {
            layout = BorderLayout()
            addKeyListener(keyAdapter)
            isFocusable = true
        } catch (ex: Exception) {
            log.error(ex.message, ex)
        }
    }

    /**
     * стандартный вход для рисования.
     * @param gr контекст вывода
     */
    override fun paint(gr: Graphics) {
        val dim = size
        val mDim = Matrix.size
        if (isClear) {
            gr.color = background
            gr.fillRect(0, 0, dim.width, dim.height)
            isClear = false
        } else {
            if (isRedrawMap || grBackgroundImage == null) {
                grBackgroundImage = BufferedImage(
                        mDim.width, mDim.height, BufferedImage.TYPE_INT_RGB
                )
                val bckGr = grBackgroundImage!!.graphics
                bckGr.color = PANEL_GRAY_COLOR
                bckGr.fillRect(0, 0, mDim.width, mDim.height)
                paintGrid(bckGr)
                paint(bckGr, frame)
                isRedrawMap = false
                requestFocusInWindow()
            }
            gr.drawImage(
                    grBackgroundImage,
                    (dim.width - mDim.width) / 2,
                    (dim.height - mDim.height) / 2,
                    mDim.width, mDim.height, null
            )
        }
    }

    /**
     * Print some message for out in status box.
     * @param out string for message
     * @param numItem номер элемента статусной строки
     */
    private fun outStatus(out: String, numItem: Int) {
        if (mainFrame != null) {
            mainFrame!!.outStatus(out, numItem)
        }
    }

    /**
     * Установить добавить ссылку на главное окно.
     * @param mainFrame главное окно
     */
    fun setMainFrame(mainFrame: MainFrame?) {
        this.mainFrame = mainFrame
    }

    /**
     * Устанавливаем признак перерисовки.
     * @param redrawMap if true, then redraw component
     */
    fun setRedrawMap(redrawMap: Boolean) {
        isRedrawMap = redrawMap
    }

    fun setFrame(frame: Int) {
        this.frame = frame
    }

    fun clearBackground() {
        isClear = true
    }
}