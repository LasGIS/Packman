package fkn.dlaskina.packman.element;

import java.awt.*;

import fkn.dlaskina.packman.map.Cell;
import fkn.dlaskina.packman.map.GameOverException;

/**
 * Это то, что осталось от врага, когда его съели :(.
 * @author Vladimir Laskin
 * @version 1.0
 */
public class Bones extends ActiveElemental {

    private static final Color BOUND_COLOR = new Color(125, 0, 0);
    private static final int BORDER = 2;

    public Bones(final Cell cell) {
        super(ElementalType.Bones, cell);
    }

    @Override
    public void paint(final Graphics gr, final Rectangle rect, final int frame) {
        final int x = (int) (rect.x + cellX + rect.width / 2);
        final int y = (int) (rect.y + cellY + rect.height / 2);
        final int arm = (rect.height / 2) - BORDER;
        gr.setColor(BOUND_COLOR);
        paintBone(gr, arm, x - arm, y, frame);
        paintBone(gr, arm, x, y + arm, frame);
        paintBone(gr, arm, x + arm, y, frame);
        paintBone(gr, arm, x, y - arm, frame);
    }

    private void paintBone(
        final Graphics gr, final int arm, final int x, final int y, final int frame
    ) {
        final int x1 = x + (int) (Math.sin(frame * Math.PI / 20) * arm);
        final int y1 = y + (int) (Math.cos(frame * Math.PI / 20) * arm);
        final int x2 = x - (int) (Math.sin(frame * Math.PI / 20) * arm);
        final int y2 = y - (int) (Math.cos(frame * Math.PI / 20) * arm);
        gr.drawLine(x1, y1, x2, y2);

    }

    @Override
    public void act() throws GameOverException {

    }
}
