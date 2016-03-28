package fkn.dlaskina.packman.element;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import fkn.dlaskina.packman.map.Cell;

/**
 * Definition of the PackMan class
 * @author VLaskin
 * @since 26.03.2016.
 */
public class PackMan extends ActiveElemental {

    private static final Color FILL_COLOR = new Color(0, 255, 0);
    private static final Color BOUND_COLOR = new Color(0, 125, 0);
    private static final int BORDER = 2;

    public PackMan(final Cell cell) {
        super(ElementalType.PackMan, cell);
    }

    @Override
    public void paint(Graphics gr, Rectangle rect, final int frame) {
        final int x = rect.x + BORDER;
        final int y = rect.y + BORDER;
        final int width =  rect.width - BORDER * 2;
        final int height = rect.height - BORDER * 2;

        gr.setColor(FILL_COLOR);
        gr.fillArc(x, y, width, height, 60, 300);
        gr.setColor(BOUND_COLOR);
        gr.drawArc(x, y, width, height, 60, 300);
    }

    @Override
    public void act() {
        Cell newCell = null;
        switch (moveType) {
            case DOWN:
                newCell = cell.getCell(0, 1);
                break;
            case UP:
                newCell = cell.getCell(0, -1);
                break;
            case RIGHT:
                newCell = cell.getCell(1, 0);
                break;
            case LEFT:
                newCell = cell.getCell(-1, 0);
                break;
        }
        if (newCell != null) {
            cell.removeAnimal(this);
            newCell.addAnimal(this);
            cell = newCell;
        }
    }
}
