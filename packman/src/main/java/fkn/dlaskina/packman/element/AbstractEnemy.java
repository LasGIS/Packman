package fkn.dlaskina.packman.element;

import java.awt.*;

import fkn.dlaskina.packman.map.Cell;

/**
 * The Class AbstractEnemy.
 * @author Vladimir Laskin
 * @version 1.0
 */
public abstract class AbstractEnemy extends ActiveElemental {

    private static final Color FILL_COLOR = new Color(255, 0, 0);
    private static final Color BOUND_COLOR = new Color(125, 0, 0);
    private static final int BORDER = 2;
    protected boolean isDummy;
    private int xText;
    private int yText;

    protected AbstractEnemy(final Cell cell) {
        super(ElementalType.Enemy, cell);
        cellStep = 2.8;
    }

    @Override
    public void paint(Graphics gr, Rectangle rect, final int frame) {
        final Polygon polygon = createPolygon(rect, frame);
        gr.setColor(FILL_COLOR);
        gr.fillPolygon(polygon);
        gr.setColor(BOUND_COLOR);
        gr.drawPolygon(polygon);
        gr.drawString(isDummy ? "D" : "E", xText, yText);
    }

    private Polygon createPolygon(Rectangle rect, int frame) {
        final int x = (int) (rect.x + cellX);
        final int y = (int) (rect.y + cellY);
        final int x0 = x + rect.width / 2;
        final int y0 = y + rect.height / 2;
        final int x1 = x + BORDER;
        final int y1 = y + BORDER;
        final int x2 = x + rect.width - BORDER;
        final int y2 = y + rect.height - BORDER;
        final double factor = (frame < 20 ? frame : 40 - frame) / 20.0;
        final int dx = (int) ((rect.width / 2 - BORDER) * factor);
        final int dy = (int) ((rect.height / 2 - BORDER) * factor);
        switch (cellMoveType) {
            case DOWN:
                xText = x + 12; yText = y + 14;
                return new Polygon(
                    new int[] {x1, x2, x2 - dx, x0, x1 + dx},
                    new int[] {y1, y1, y2, y0, y2}, 5
                );
            case UP:
                xText = x + 12; yText = y + 26;
                return new Polygon(
                    new int[] {x1 + dx, x0, x2 - dx, x2, x1},
                    new int[] {y1, y0, y1, y2, y2}, 5
                );
            case LEFT:
                xText = x + 18; yText = y + 20;
                return new Polygon(
                    new int[] {x2, x2, x1, x0, x1},
                    new int[] {y1, y2, y2 - dy, y0, y1 + dy}, 5
                );
            case RIGHT:
                xText = x + 6; yText = y + 20;
                return new Polygon(
                    new int[] {x1, x2, x0, x2, x1},
                    new int[] {y1, y1 + dy, y0, y2 - dy, y2}, 5
                );
            default:
                xText = x + 12; yText = y + 16;
                return new Polygon(new int[] {x1, x2, x2, x1}, new  int[] {y1, y1, y2, y2}, 4);
        }
    }

}
