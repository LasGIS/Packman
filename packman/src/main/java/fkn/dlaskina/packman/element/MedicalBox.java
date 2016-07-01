package fkn.dlaskina.packman.element;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * Аптечка для врагов.
 * @author Vladimir Laskin
 * @version 1.0
 */
public class MedicalBox extends Elemental {

    private static final Color FILL_COLOR = new Color(255, 255, 0);
    private static final Color BOUND_COLOR = new Color(125, 128, 0);
    private static final int BORDER = 5;

    /**
     * Конструктор
     */
    public MedicalBox() {
        super(ElementalType.MedBox);
    }

    @Override
    public void paint(final Graphics gr, final Rectangle rect, final int frame) {
        final int x = rect.x + BORDER;
        final int y = rect.y + BORDER;
        final int width = rect.width - BORDER * 2;
        final int height = rect.height - BORDER * 2;
        gr.setColor(FILL_COLOR);
        gr.fillRect(x, y, width, height);
        gr.setColor(BOUND_COLOR);
        gr.drawRect(x, y, width, height);
    }
}
