package fkn.dlaskina.packman.element;

import java.awt.*;

/**
 * Definition of the Enemy class
 * @author VLaskin
 * @since 26.03.2016.
 */
public class Enemy extends Elemental {

    private static final Color FILL_COLOR = new Color(255, 0, 0);
    private static final Color BOUND_COLOR = new Color(125, 0, 0);

    public Enemy() {
        super(ElementalType.Enemy);
    }

    @Override
    public void paint(Graphics gr, Rectangle rect) {
        gr.setColor(FILL_COLOR);
        gr.fillArc(rect.x, rect.y, rect.width, rect.height, 30, 330);
        gr.setColor(BOUND_COLOR);
        gr.drawArc(rect.x, rect.y, rect.width, rect.height, 30, 330);
    }
}
