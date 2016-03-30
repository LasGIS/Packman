package fkn.dlaskina.packman.element;

import java.awt.*;

import fkn.dlaskina.packman.map.Cell;
import fkn.dlaskina.packman.map.GameOverException;

/**
 * Definition of the Enemy class
 * @author VLaskin
 * @since 26.03.2016.
 */
public class Enemy extends ActiveElemental {

    private static final Color FILL_COLOR = new Color(255, 0, 0);
    private static final Color BOUND_COLOR = new Color(125, 0, 0);
    private static final int BORDER = 2;

    public Enemy(final Cell cell) {
        super(ElementalType.Enemy, cell);
    }

    @Override
    public void paint(Graphics gr, Rectangle rect) {
        final Polygon polygon = createPolygon(rect);
        gr.setColor(FILL_COLOR);
        gr.fillPolygon(polygon);
        gr.setColor(BOUND_COLOR);
        gr.drawPolygon(polygon);;
    }

    private Polygon createPolygon(Rectangle rect) {
        final int x0 = rect.x + rect.width / 2;
        final int y0 = rect.y + rect.height / 2;
        final int x1 = rect.x + BORDER;
        final int y1 = rect.y + BORDER;
        final int x2 = rect.x + rect.width - BORDER;
        final int y2 = rect.y + rect.height - BORDER;
        return new Polygon(
            new int[] {x1, x2, x0, x2, x1},
            new int[] {y1, y1, y0, y2, y2}, 5
        );
    }

    @Override
    public void act() throws GameOverException {
        // альтернативные перемещения
        Cell alterCell[] = new Cell[4];
        MoveType alterMoveType[] = new MoveType[4];
        final int alterCount = findAlternativeCells(alterCell, alterMoveType);
        Cell newCell;
        if (alterCount > 2) {
            // есть много путей
            int ind = (int) Math.floor(Math.random() * alterCount);
            newCell = alterCell[ind];
            moveType = alterMoveType[ind];
        } else if (alterCount > 0) {
            // только один путь - вперёд!
            newCell = alterCell[0];
            moveType = alterMoveType[0];
        } else {
            // нет выхода
            newCell = null;
        }
        if (newCell != null) {
            cell.removeElement(this);
            newCell.addElement(this);
            cell = newCell;

            // проверяем на packman`a
            if (newCell.contains(ElementalType.PackMan)) {
                throw new GameOverException(false, "Враг наехал на рокемона");
            }
        }
    }

    private int findAlternativeCells(final Cell[] alterCell, final MoveType[] alterMoveType) {
        int count = 0;
        final Cell[] tempCell = new Cell[4];
        final MoveType[] tempMoveType = new MoveType[4];
        switch (moveType) {
            case DOWN:
                tempCell[0] = cell.getCell( 0,  1); tempMoveType[0] = MoveType.DOWN;
                tempCell[1] = cell.getCell( 1,  0); tempMoveType[1] = MoveType.RIGHT;
                tempCell[2] = cell.getCell(-1,  0); tempMoveType[2] = MoveType.LEFT;
                tempCell[3] = cell.getCell( 0, -1); tempMoveType[3] = MoveType.UP;
                break;
            case NONE:
            case UP:
                tempCell[0] = cell.getCell( 0, -1); tempMoveType[0] = MoveType.UP;
                tempCell[1] = cell.getCell(-1,  0); tempMoveType[1] = MoveType.LEFT;
                tempCell[2] = cell.getCell( 1,  0); tempMoveType[2] = MoveType.RIGHT;
                tempCell[3] = cell.getCell( 0,  1); tempMoveType[3] = MoveType.DOWN;
                break;
            case RIGHT:
                tempCell[0] = cell.getCell( 1,  0); tempMoveType[0] = MoveType.RIGHT;
                tempCell[1] = cell.getCell( 0,  1); tempMoveType[1] = MoveType.DOWN;
                tempCell[2] = cell.getCell( 0, -1); tempMoveType[2] = MoveType.UP;
                tempCell[3] = cell.getCell(-1,  0); tempMoveType[3] = MoveType.LEFT;
                break;
            case LEFT:
                tempCell[0] = cell.getCell(-1,  0); tempMoveType[0] = MoveType.LEFT;
                tempCell[1] = cell.getCell( 0, -1); tempMoveType[1] = MoveType.UP;
                tempCell[2] = cell.getCell( 0,  1); tempMoveType[2] = MoveType.DOWN;
                tempCell[3] = cell.getCell( 1,  0); tempMoveType[3] = MoveType.RIGHT;
                break;
        }
        for (int i = 0; i < 4; i++) {
            final Cell testCell = tempCell[i];
            if (testCell != null && !testCell.isStone() && !testCell.contains(ElementalType.Enemy)) {
                alterCell[count] = tempCell[i];
                alterMoveType[count] = tempMoveType[i];
                count++;
            }
        }
        return count;
    }
}
