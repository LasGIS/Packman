package fkn.dlaskina.packman.element;

import java.awt.*;

/**
 * Definition of the Surprise class
 * @author VLaskin
 * @since 26.03.2016.
 */
public class Stone extends Elemental {

    private static final Color FILL_COLOR = new Color(125, 128, 128);

    public Stone() {
        super(ElementalType.Stone);
    }

    @Override
    public void paint(Graphics gr, Rectangle rect, final int frame) {
        gr.setColor(FILL_COLOR);
        gr.fillRect(rect.x, rect.y, rect.width, rect.height);
    }
}
