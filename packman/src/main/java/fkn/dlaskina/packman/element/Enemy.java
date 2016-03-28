package fkn.dlaskina.packman.element;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import fkn.dlaskina.packman.map.Cell;

/**
 * Definition of the Enemy class
 * @author VLaskin
 * @since 26.03.2016.
 */
public class Enemy extends ActiveElemental {

    private static final Color FILL_COLOR = new Color(255, 0, 0);
    private static final Color BOUND_COLOR = new Color(125, 0, 0);

    public Enemy(final Cell cell) {
        super(ElementalType.Enemy, cell);
    }

    @Override
    public void paint(Graphics gr, Rectangle rect, final int frame) {
        gr.setColor(FILL_COLOR);
        gr.fillArc(rect.x, rect.y, rect.width, rect.height, 30, 330);
        gr.setColor(BOUND_COLOR);
        gr.drawArc(rect.x, rect.y, rect.width, rect.height, 30, 330);
    }

    @Override
    public void act() {

    }
}
