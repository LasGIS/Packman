package fkn.dlaskina.packman.panels;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import fkn.dlaskina.packman.element.MoveType;
import fkn.dlaskina.packman.element.PackMan;
import fkn.dlaskina.packman.map.Matrix;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;


/**
 * Окно вывода карты.
 * @author VLaskin
 * @version 1.0
 */
public class MapPanel extends JPanel {

    private static final Logger LOG = LogManager.getLogger(MapPanel.class);

    /** серый цвет фона. */
    public static final Color PANEL_GRAY_COLOR = new Color(220, 220, 220);
    /** ссылка на MainFrame. */
    private MainFrame mainFrame = null;

    /** Если true, то будем перегружать. */
    private boolean isRedrawMap = true;
    /** сохраненное изображение. */
    private BufferedImage grBackgroundImage = null;
    /** номер кадра. */
    private int frame = 0;
    /** Стереть фон. */
    private boolean isClear = true;

    /** Ловим нажатие кнопочек. */
    private final KeyAdapter keyAdapter = new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
            final PackMan packMan = Matrix.getMatrix().getPackMan();
            switch (e.getKeyCode()) {
                case KeyEvent.VK_NUMPAD4 :
                case KeyEvent.VK_LEFT :
                    // влево
                    packMan.setMove(MoveType.LEFT);
                    break;
                case KeyEvent.VK_NUMPAD6:
                case KeyEvent.VK_RIGHT:
                    // вправо
                    packMan.setMove(MoveType.RIGHT);
                    break;
                case KeyEvent.VK_NUMPAD8:
                case KeyEvent.VK_UP:
                    // вверх
                    packMan.setMove(MoveType.UP);
                    break;
                case KeyEvent.VK_NUMPAD2:
                case KeyEvent.VK_DOWN:
                    // вниз
                    packMan.setMove(MoveType.DOWN);
                    break;
                default:
                    break;
            }
            //LOG.info("keyPressed = " + e.getKeyCode());
        }
    };

    /**
     * Конструктор.
     */
    public MapPanel() {
        try {
            setLayout(new BorderLayout());
            addKeyListener(keyAdapter);
            setFocusable(true);
        } catch (final Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

    /**
     * стандартный вход для рисования.
     * @param gr контекст вывода
     */
    public void paint(final Graphics gr) {
        final Dimension dim = getSize();
        final Dimension mDim = Matrix.getMatrix().getSize();
        if (isClear) {
            gr.setColor(this.getBackground());
            gr.fillRect(0, 0, dim.width, dim.height);
            isClear = false;
        } else {
            if (isRedrawMap || grBackgroundImage == null) {
                grBackgroundImage = new BufferedImage(
                    mDim.width, mDim.height, BufferedImage.TYPE_INT_RGB
                );
                final Graphics bckGr = grBackgroundImage.getGraphics();
                bckGr.setColor(MapPanel.PANEL_GRAY_COLOR);
                bckGr.fillRect(0, 0, mDim.width, mDim.height);
                Matrix.getMatrix().paintGrid(bckGr);
                Matrix.getMatrix().paint(bckGr, frame);
                isRedrawMap = false;
                requestFocusInWindow();
            }
            gr.drawImage(
                grBackgroundImage,
                (dim.width - mDim.width) / 2,
                (dim.height - mDim.height) / 2,
                mDim.width, mDim.height, null
            );
        }
    }

    /**
     * Print some message for out in status box.
     * @param out string for message
     * @param numItem номер элемента статусной строки
     */
    private void outStatus(final String out, final int numItem) {
        if (mainFrame != null) {
            mainFrame.outStatus(out, numItem);
        }
    }

    /**
     * Установить добавить ссылку на главное окно.
     * @param mainFrame главное окно
     */
    public void setMainFrame(final MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    /**
     * Устанавливаем признак перерисовки.
     * @param redrawMap if true, then redraw component
     */
    public void setRedrawMap(final boolean redrawMap) {
        this.isRedrawMap = redrawMap;
    }

    public void setFrame(final int frame) {
        this.frame = frame;
    }

    public void clearBackground() {
        isClear = true;
    }
}