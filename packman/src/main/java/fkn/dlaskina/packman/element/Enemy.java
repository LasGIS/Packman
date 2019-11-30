package fkn.dlaskina.packman.element;

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
public class Enemy extends AbstractEnemy {

    public Enemy(final Cell cell) {
        super(cell);
        isDummy = false;
    }

    @Override
    public void act() throws GameOverException {
        if (isCenterCell()) {
            final PackMan packMan = Matrix.getMatrix().packMan;
            final boolean isAggressive = packMan.getPrizeType() == SurpriseType.aggressive;
            AlterCellMove finalCellMove = null;
            int finalPackManRate = isAggressive ? Integer.MIN_VALUE : Integer.MAX_VALUE;
            for (final AlterCellMove cellMove : cell.getAroundCells()) {
                final int packManRate = cellMove.getCell().getPackManRate();
                if (isAggressive ? packManRate > finalPackManRate : packManRate < finalPackManRate) {
                    finalPackManRate = packManRate;
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
                if (newCell.contains(PackMan)) {
                    final Matrix matrix = Matrix.getMatrix();
                    final PackMan packMan = Matrix.getMatrix().packMan;
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
}
