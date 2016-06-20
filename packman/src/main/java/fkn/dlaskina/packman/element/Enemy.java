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
        cellStep = 3.0;
    }

    @Override
    public void paint(Graphics gr, Rectangle rect, final int frame) {
        final Polygon polygon = createPolygon(rect, frame);
        gr.setColor(FILL_COLOR);
        gr.fillPolygon(polygon);
        gr.setColor(BOUND_COLOR);
        gr.drawPolygon(polygon);;
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
                return new Polygon(
                    new int[] {x1, x2, x2 - dx, x0, x1 + dx},
                    new int[] {y1, y1, y2, y0, y2}, 5
                );
            case UP:
                return new Polygon(
                    new int[] {x1 + dx, x0, x2 - dx, x2, x1},
                    new int[] {y1, y0, y1, y2, y2}, 5
                );
            case LEFT:
                return new Polygon(
                    new int[] {x2, x2, x1, x0, x1},
                    new int[] {y1, y2, y2 - dy, y0, y1 + dy}, 5
                );
            case RIGHT:
                return new Polygon(
                    new int[] {x1, x2, x0, x2, x1},
                    new int[] {y1, y1 + dy, y0, y2 - dy, y2}, 5
                );
            default:
                return new Polygon(new int[] {x1, x2, x2, x1}, new  int[] {y1, y1, y2, y2}, 4);
        }
    }

    @Override
    public void act() throws GameOverException {
        if (isCenterCell()) {
            // альтернативные перемещения
            Cell alterCell[] = new Cell[4];
            MoveType alterMoveType[] = new MoveType[4];
            final int alterCount = findAlternativeCells(alterCell, alterMoveType);
            if (alterCount > 2) {
                // есть много путей
                int ind = (int) Math.floor(Math.random() * alterCount);
                newCell = alterCell[ind];
                cellMoveType = alterMoveType[ind];
            } else if (alterCount > 0) {
                // только один путь - вперёд!
                newCell = alterCell[0];
                cellMoveType = alterMoveType[0];
            } else {
                // нет выхода
                newCell = null;
            }
        }
        if (isBorderCell()) {
            if (newCell != null) {
                cell.removeElement(this);
                newCell.addElement(this);
                cell = newCell;
                moveType = cellMoveType;
                startCellMove();

                // проверяем на packman`a
                if (newCell.contains(ElementalType.PackMan)) {
                    throw new GameOverException(false, "Враг наехал на рокемона");
                }
            }
        }
        cellMove();
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
