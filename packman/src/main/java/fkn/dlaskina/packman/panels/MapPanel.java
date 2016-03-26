/**
 * @(#)MapPanel.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package fkn.dlaskina.packman.panels;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.concurrent.atomic.AtomicBoolean;

import fkn.dlaskina.packman.map.Matrix;
import org.apache.log4j.Logger;
import org.apache.log4j.LogManager;


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

    /** Ловим нажатие кнопочек. */
    private final KeyAdapter keyAdapter = new KeyAdapter() {
        @Override
        public void keyReleased(KeyEvent e) {
            boolean isRedraw = false;
            switch (e.getKeyCode()) {
                case KeyEvent.VK_NUMPAD4 :
                case KeyEvent.VK_LEFT :
                    // влево
                    isRedraw = true;
                    break;
                case KeyEvent.VK_NUMPAD6:
                case KeyEvent.VK_RIGHT:
                    // вправо
                    isRedraw = true;
                    break;
                case KeyEvent.VK_NUMPAD8:
                case KeyEvent.VK_UP:
                    // вверх
                    isRedraw = true;
                    break;
                case KeyEvent.VK_NUMPAD2:
                case KeyEvent.VK_DOWN:
                    // вниз
                    isRedraw = true;
                    break;
                default:
                    break;
            }
            if (isRedraw) {
                setRedrawMap(true);
            }
            LOG.info("keyReleased = " + e.getKeyCode());
        }
    };

    /**
     * Конструктор.
     */
    public MapPanel() {
        try {
            setBackground(Color.white);
            setBorder(BorderFactory.createLineBorder(Color.blue));
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
        //gr.setColor(MapPanel.PANEL_GRAY_COLOR);
        //gr.fillRect(0, 0, dim.width, dim.height);

        if (isRedrawMap || grBackgroundImage == null) {
            grBackgroundImage = new BufferedImage(
                mDim.width, mDim.height, BufferedImage.TYPE_INT_RGB
            );
            final Graphics bckGr = grBackgroundImage.getGraphics();
            bckGr.setColor(MapPanel.PANEL_GRAY_COLOR);
            bckGr.fillRect(0, 0, mDim.width, mDim.height);
            Matrix.getMatrix().paint(bckGr);
            isRedrawMap = false;
            requestFocusInWindow();
        }
        gr.drawImage(
            grBackgroundImage,
            (dim.width - mDim.width) / 2,
            (dim.height - mDim.height) / 2,
            mDim.width, mDim.height, null
        );
        //drawing.grid();
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
}