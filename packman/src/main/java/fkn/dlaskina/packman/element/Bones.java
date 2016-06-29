package fkn.dlaskina.packman.element;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Collection;
import java.util.List;

import fkn.dlaskina.packman.map.Cell;
import fkn.dlaskina.packman.map.GameOverException;
import fkn.dlaskina.packman.map.Matrix;

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
        cellStep = 1.0;
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
        if (isCenterCell()) {
            AlterCellMove finalCellMove = null;
            int finalBoneRate = Integer.MAX_VALUE;
            for (final AlterCellMove cellMove : cell.getAroundCells()) {
                final int boneRate = cellMove.getCell().getBoneRate();
                if (boneRate < finalBoneRate) {
                    finalBoneRate = boneRate;
                    finalCellMove = cellMove;
                }
            }
            if (finalCellMove != null) {
                newCell = finalCellMove.getCell();
                cellMoveType = finalCellMove.getMoveType();
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
                if (newCell.contains(ElementalType.MedBox)) {
                    Collection<Elemental> elements = newCell.getElements();
                    for (final Elemental elm : elements) {
                        if (elm.getType() == ElementalType.MedBox) {
                            elements.remove(elm);
                            break;
                        }
                    }
                    final Enemy enm = new Enemy(newCell);
                    final Matrix matrix = Matrix.getMatrix();
                    newCell.addElement(enm);
                    final List<ActiveElemental> matrixElements = matrix.getElements();
                    matrixElements.add(enm);
                    newCell.removeElement(this);
                    matrixElements.remove(this);
                    matrix.createBoneRate();
                }
            }
        }
        cellMove();
    }
}
