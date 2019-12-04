package fkn.dlaskina.packman.element;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * Definition of the Surprise class
 * @author VLaskin
 * @since 26.03.2016.
 */
public class Surprise extends Elemental {

    private static final Color FILL_COLOR = new Color(255, 255, 0);
    private static final Color BOUND_COLOR = new Color(125, 128, 0);
    private static final Color SPEED_FILL_COLOR = new Color(0, 128, 255);
    private static final Color SPEED_BOUND_COLOR = new Color(0, 0, 128);
    private static final Color AGGRESSIVE_FILL_COLOR = new Color(255, 128, 0);
    private static final Color AGGRESSIVE_BOUND_COLOR = new Color(200, 0, 0);
    private static final int BORDER = 5;
    private SurpriseType prizeType;

    public Surprise(final SurpriseType type) {
        super(ElementalType.Surprise);
        this.prizeType = type;
    }

    public SurpriseType getPrizeType() {
        return prizeType;
    }

    @Override
    public void paint(final Graphics gr, final Rectangle rect, final int frame) {
        final int width = (int) ((rect.width - BORDER * 2) * Math.abs(Math.cos((frame * Math.PI) / 20)));
        final int x = rect.x + (rect.width - width) / 2;
        final int y = rect.y + BORDER;
        final int height = rect.height - BORDER * 2;
        if (width > 0) {
            gr.setColor(getFillColor());
            gr.fillOval(x, y, width, height);
            gr.setColor(getBoundColor());
            gr.drawOval(x, y, width, height);
        } else {
            gr.setColor(getFillColor());
            gr.drawLine(x, y, x, y + height);
        }
    }

    private Color getBoundColor() {
        switch (prizeType) {
            case aggressive: return AGGRESSIVE_BOUND_COLOR;
            case speed: return SPEED_BOUND_COLOR;
            default: return BOUND_COLOR;
        }
    }

    private Color getFillColor() {
        switch (prizeType) {
            case aggressive: return AGGRESSIVE_FILL_COLOR;
            case speed: return SPEED_FILL_COLOR;
            default: return FILL_COLOR;
        }
    }
}
