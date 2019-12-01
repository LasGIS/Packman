package fkn.dlaskina.packman.element;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import fkn.dlaskina.packman.map.Cell;
import fkn.dlaskina.packman.map.GameOverException;
import fkn.dlaskina.packman.map.Matrix;
import fkn.dlaskina.packman.panels.ConfigPanel;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import static fkn.dlaskina.packman.element.SurpriseType.aggressive;
import static fkn.dlaskina.packman.element.SurpriseType.simple;

/**
 * Definition of the PackMan class
 * @author VLaskin
 * @since 26.03.2016.
 */
public class PackMan extends ActiveElemental {

    private static final Logger LOG = LogManager.getLogger(PackMan.class);

    private static final Color FILL_COLOR = new Color(0, 255, 0);
    private static final Color BOUND_COLOR = new Color(0, 125, 0);
    private static final Color SPEED_FILL_COLOR = new Color(0, 128, 255);
    private static final Color SPEED_BOUND_COLOR = new Color(0, 0, 200);
    private static final Color AGGRESSIVE_FILL_COLOR = new Color(255, 128, 0);
    private static final Color AGGRESSIVE_BOUND_COLOR = new Color(200, 0, 0);
    private static final double SPEED_CELL_STEP = 5.0;
    private static final double SIMPLE_CELL_STEP = 3.0;
    private static final int BORDER = 2;

    private SurpriseType prizeType = simple;
    private long prizeTime = 0;

    public PackMan(final Cell cell) {
        super(ElementalType.PackMan, cell);
        cellStep = SIMPLE_CELL_STEP;
    }

    public SurpriseType getPrizeType() {
        return prizeType;
    }

    @Override
    public void paint(Graphics gr, Rectangle rect, final int frame) {
        final int x = (int) (rect.x + cellX + BORDER);
        final int y = (int) (rect.y + cellY + BORDER);
        final int width =  rect.width - BORDER * 2;
        final int height = rect.height - BORDER * 2;
        final int angView;
        final int angMouth = (frame < 20 ? frame : (40 - frame)) * 3;
        switch (cellMoveType) {
            case DOWN: angView = 270; break;
            case UP: angView = 90; break;
            case LEFT: angView = 180; break;
            default:
            case RIGHT: angView = 0; break;
        }

        gr.setColor(getFillColor());
        gr.fillArc(x, y, width, height, angView + angMouth, 360 - (angMouth * 2));
        gr.setColor(getBoundColor());
        gr.drawArc(x, y, width, height, angView + angMouth, 360 - (angMouth * 2));
    }

    private Color getBoundColor() {
        switch (prizeType) {
            case aggressive: return AGGRESSIVE_BOUND_COLOR;
            case speed: return SPEED_BOUND_COLOR;
            default: return BOUND_COLOR;
        }
    }

    private Color getFillColor() {
        switch (prizeType) {
            case aggressive: return AGGRESSIVE_FILL_COLOR;
            case speed: return SPEED_FILL_COLOR;
            default: return FILL_COLOR;
        }
    }

    @Override
    public void act() throws GameOverException {
        if (isCenterCell()) {
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
            if (newCell == null || newCell.isStone()) {
                cellX = 0;
                cellY = 0;
                cellMoveType = moveType = MoveType.NONE;
            } else {
                cellMoveType = moveType;
            }
            Matrix.INSTANCE.createPackManRate();
        }
        if (isBorderCell()) {
            if (newCell != null) {
                cell.removeElement(this);
                newCell.addElement(this);
                cell = newCell;
                startCellMove();
                // забираем призы и проверяем на злодея
                for (Elemental elm : newCell.getElements()) {
                    switch (elm.getType()) {
                        case Surprise:
                            final SurpriseType surpriseType = ((Surprise) elm).getPrizeType();
                            boolean isRemove = false;
                            if (prizeType == simple && surpriseType != simple) {
                                prizeType = surpriseType;
                                switch (prizeType) {
                                    case aggressive:
                                        prizeTime = System.currentTimeMillis() + 10000;
                                        isRemove = true;
                                        break;
                                    case speed:
                                        prizeTime = System.currentTimeMillis() + 20000;
                                        cellStep = SPEED_CELL_STEP;
                                        isRemove = true;
                                        break;
                                }
                            }
                            if (isRemove || !(prizeType != simple && surpriseType != simple)) {
                                newCell.removeElement(elm);
                                if (ConfigPanel.addBonus()) {
                                    throw new GameOverException(true, "Победа!");
                                }
                            }
                            break;
                        case Enemy: {
                            if (prizeType == aggressive) {
                                Matrix.INSTANCE.removeEnemy((AbstractEnemy) elm);
                            } else {
                                throw new GameOverException(false, "Сам наехал на врага");
                            }
                        }
                    }
                }
                Matrix.INSTANCE.createPackManRate();
            } else {
                cellX = 0;
                cellY = 0;
                cellMoveType = moveType = MoveType.NONE;
            }
        }
        if (prizeType != simple && prizeTime < System.currentTimeMillis()) {
            cellStep = SIMPLE_CELL_STEP;
            prizeType = simple;
        }
        cellMove();
    }
}
