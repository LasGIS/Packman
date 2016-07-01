package fkn.dlaskina.packman.element;

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
public class EnemyDummy extends AbstractEnemy {

    public EnemyDummy(final Cell cell) {
        super(cell);
        isDummy = true;
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
}
