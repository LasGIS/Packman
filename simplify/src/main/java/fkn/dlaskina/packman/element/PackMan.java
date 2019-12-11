package fkn.dlaskina.packman.element;

import fkn.dlaskina.packman.map.Cell;
import fkn.dlaskina.packman.map.GameOverException;
import fkn.dlaskina.packman.panels.ConfigPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;

/**
 * Definition of the PackMan class
 * @author VLaskin
 * @since 26.03.2016.
 */
public class PackMan extends ActiveElemental {

    private static final Logger LOG = LoggerFactory.getLogger(PackMan.class);

    private static final Color FILL_COLOR = new Color(0, 255, 0);
    private static final Color BOUND_COLOR = new Color(0, 125, 0);
    private static final int BORDER = 2;

    public PackMan(final Cell cell) {
        super(ElementalType.PackMan, cell);
    }

    @Override
    public void paint(Graphics gr, Rectangle rect) {
        final int x = rect.x + BORDER;
        final int y = rect.y + BORDER;
        final int width =  rect.width - BORDER * 2;
        final int height = rect.height - BORDER * 2;
        gr.setColor(FILL_COLOR);
        gr.fillArc(x, y, width, height, 45, 270);
        gr.setColor(BOUND_COLOR);
        gr.drawArc(x, y, width, height, 45, 270);
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
