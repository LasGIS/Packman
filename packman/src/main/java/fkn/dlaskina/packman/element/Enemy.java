package fkn.dlaskina.packman.element;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import fkn.dlaskina.packman.map.Cell;
import fkn.dlaskina.packman.map.GameOverException;
import fkn.dlaskina.packman.map.Matrix;

import static fkn.dlaskina.packman.element.ElementalType.PackMan;
import static fkn.dlaskina.packman.element.SurpriseType.aggressive;

/**
 * Враг.
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
            final List<AlterCellMove> alterCell = new ArrayList<>();
            final int alterCount = findAlternativeCells(alterCell);
            if (alterCount > 2) {
                // есть много путей
                AlterCellMove alterMoveType = findPackMan(alterCell);
                newCell = alterMoveType.getCell();
                cellMoveType = alterMoveType.getMoveType();
            } else if (alterCount > 0) {
                // только один путь - вперёд!
                AlterCellMove alterMoveType = alterCell.get(0);
                newCell = alterMoveType.getCell();
                cellMoveType = alterMoveType.getMoveType();
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
                if (newCell.contains(PackMan)) {
                    final Matrix matrix = Matrix.getMatrix();
                    final PackMan packMan = Matrix.getMatrix().getPackMan();
                    if (packMan.getPrizeType() == aggressive) {
                        matrix.removeEnemy(this);
                    } else {
                        throw new GameOverException(false, "Враг наехал на рокемона");
                    }
                }
            }
        }
        cellMove();
    }

    private AlterCellMove findPackMan(final List<AlterCellMove> alterCell) {
        final PackMan packMan = Matrix.getMatrix().getPackMan();
        final Cell cell = packMan.getCell();
        final boolean isAggressive = packMan.getPrizeType() == SurpriseType.aggressive;
        double distance = isAggressive ? Double.MIN_VALUE : Double.MAX_VALUE;
        AlterCellMove ret = null;
        for (final AlterCellMove acm : alterCell) {
            final double distanceCell = acm.getCell().distance(cell);
            if (isAggressive ? distanceCell > distance : distanceCell < distance) {
                distance = distanceCell;
                ret = acm;
            }

        }
        return ret;
    }

    private int findAlternativeCells(final List<AlterCellMove> alterCell) {
        final AlterCellMove[] tempCell = new AlterCellMove[4];
        switch (moveType) {
            case DOWN:
                tempCell[0] = new AlterCellMove(cell.getCell( 0,  1), MoveType.DOWN);
                tempCell[1] = new AlterCellMove(cell.getCell( 1,  0), MoveType.RIGHT);
                tempCell[2] = new AlterCellMove(cell.getCell(-1,  0), MoveType.LEFT);
                tempCell[3] = new AlterCellMove(cell.getCell( 0, -1), MoveType.UP);
                break;
            case NONE:
            case UP:
                tempCell[0] = new AlterCellMove(cell.getCell( 0, -1), MoveType.UP);
                tempCell[1] = new AlterCellMove(cell.getCell(-1,  0), MoveType.LEFT);
                tempCell[2] = new AlterCellMove(cell.getCell( 1,  0), MoveType.RIGHT);
                tempCell[3] = new AlterCellMove(cell.getCell( 0,  1), MoveType.DOWN);
                break;
            case RIGHT:
                tempCell[0] = new AlterCellMove(cell.getCell( 1,  0), MoveType.RIGHT);
                tempCell[1] = new AlterCellMove(cell.getCell( 0,  1), MoveType.DOWN);
                tempCell[2] = new AlterCellMove(cell.getCell( 0, -1), MoveType.UP);
                tempCell[3] = new AlterCellMove(cell.getCell(-1,  0), MoveType.LEFT);
                break;
            case LEFT:
                tempCell[0] = new AlterCellMove(cell.getCell(-1,  0), MoveType.LEFT);
                tempCell[1] = new AlterCellMove(cell.getCell( 0, -1), MoveType.UP);
                tempCell[2] = new AlterCellMove(cell.getCell( 0,  1), MoveType.DOWN);
                tempCell[3] = new AlterCellMove(cell.getCell( 1,  0), MoveType.RIGHT);
                break;
        }
        for (final AlterCellMove alter : tempCell) {
            final Cell testCell = alter.getCell();
            if (testCell != null && !testCell.isStone() && !testCell.contains(ElementalType.Enemy)) {
                alterCell.add(alter);
            }
        }
        return alterCell.size();
    }
}
