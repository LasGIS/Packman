package fkn.dlaskina.packman.element;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import fkn.dlaskina.packman.map.Cell;
import fkn.dlaskina.packman.map.GameOverException;
import fkn.dlaskina.packman.panels.ConfigPanel;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Definition of the PackMan class
 * @author VLaskin
 * @since 26.03.2016.
 */
public class PackMan extends ActiveElemental {

    private static final Logger LOG = LogManager.getLogger(PackMan.class);

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
        final int angView;
        final int angMouth = (frame < 20 ? frame : (40 - frame)) * 3;
        switch (moveType) {
            case DOWN: angView = 270; break;
            case UP: angView = 90; break;
            case LEFT: angView = 180; break;
            default:
            case RIGHT: angView = 0; break;
        }

        gr.setColor(FILL_COLOR);
        gr.fillArc(x, y, width, height, angView + angMouth, 360 - (angMouth * 2));
        gr.setColor(BOUND_COLOR);
        gr.drawArc(x, y, width, height, angView + angMouth, 360 - (angMouth * 2));
    }

    @Override
    public void act() throws GameOverException {
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
        if (newCell != null && !newCell.isStone()) {
            cell.removeElement(this);
            newCell.addElement(this);
            cell = newCell;
            // забираем призы и проверяем на злодея
            for (Elemental elm : newCell.getElements()) {
                switch (elm.getType()) {
                    case Surprise:
                        newCell.removeElement(elm);
                        if (ConfigPanel.addBonus()) {
                            throw new GameOverException(true, "Победа!");
                        }
                        break;
                    case Enemy: {
                        throw new GameOverException(false, "Сам наехал на врага");
                    }
                }
            }
        } else {
            moveType = MoveType.NONE;
        }
    }
}
