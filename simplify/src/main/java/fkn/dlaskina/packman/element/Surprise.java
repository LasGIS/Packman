package fkn.dlaskina.packman.element;

import java.awt.*;

/**
 * Definition of the Surprise class
 * @author VLaskin
 * @since 26.03.2016.
 */
public class Surprise extends Elemental {

    private static final Color FILL_COLOR = new Color(255, 255, 0);
    private static final Color BOUND_COLOR = new Color(125, 128, 0);
    private static final int BORDER = 5;

    public Surprise() {
        super(ElementalType.Surprise);
    }

    @Override
    public void paint(final Graphics gr, final Rectangle rect) {
        final int x = rect.x + BORDER;
        final int y = rect.y + BORDER;
        final int width = rect.width - BORDER * 2;
        final int height = rect.height - BORDER * 2;
        gr.setColor(FILL_COLOR);
        gr.fillOval(x, y, width, height);
        gr.setColor(BOUND_COLOR);
        gr.drawOval(x, y, width, height);
    }
}
