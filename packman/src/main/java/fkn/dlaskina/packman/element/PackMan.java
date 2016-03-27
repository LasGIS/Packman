package fkn.dlaskina.packman.element;

import fkn.dlaskina.packman.map.Cell;

import java.awt.*;

/**
 * Definition of the PackMan class
 * @author VLaskin
 * @since 26.03.2016.
 */
public class PackMan extends ActiveElemental {

    private static final Color FILL_COLOR = new Color(0, 255, 0);
    private static final Color BOUND_COLOR = new Color(0, 125, 0);
    private static final int BORDER = 2;

    public PackMan() {
        super(ElementalType.PackMan);
    }

    @Override
    public void paint(Graphics gr, Rectangle rect) {
        gr.setColor(FILL_COLOR);
        gr.fillArc(rect.x + BORDER, rect.y + BORDER, rect.width - BORDER * 2, rect.height - BORDER * 2, 60, 300);
        gr.setColor(BOUND_COLOR);
        gr.drawArc(rect.x + BORDER, rect.y + BORDER, rect.width - BORDER * 2, rect.height - BORDER * 2, 60, 300);
    }

    @Override
    public void act(Cell cell) {

    }
}
