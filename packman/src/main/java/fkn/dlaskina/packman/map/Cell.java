package fkn.dlaskina.packman.map;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import fkn.dlaskina.packman.element.AlterCellMove;
import fkn.dlaskina.packman.element.Elemental;
import fkn.dlaskina.packman.element.ElementalType;
import fkn.dlaskina.packman.element.MoveType;

import static fkn.dlaskina.packman.map.Matrix.CELL_SIZE;

/**
 * Ячейка карты. Эта ячейка содержит список различных элементов
 *
 * @author Laskin
 * @version 1.0
 * @since 16.06.2010 21:47:16
 */
public class Cell {

    private static final int[] DXS = {0, 1, -1, 0};
    private static final int[] DYS = {1, 0, 0, -1};
    private static final MoveType[] D_MOVE_TYPES = {MoveType.DOWN, MoveType.RIGHT, MoveType.LEFT, MoveType.UP};

    private static final Color BONE_COLOR = new Color(125, 0, 0);

    /** индекс широты ячейки 0 - это юг, 100 - это север. */
    private int indX;
    /** индекс долгота ячейки 0 - это запад, 100 - это восток. */
    private int indY;
    /** рейтинг ячейки для костей*/
    private int boneRate = 0;
    /** список сущьностей, населяющих ячейку. */
    private CopyOnWriteArrayList<Elemental> elements = new CopyOnWriteArrayList<>();
    private final Rectangle rectangle;

    /**
     * Создание ячейки по нижнему левому углу.
     * @param indexX индекс ячейки (справа - налево)
     * @param indexY индекс ячейки (сверху - вниз)
     */
    public Cell(final int indexX, final int indexY) {
        this.indX = indexX;
        this.indY = indexY;
        rectangle = new Rectangle(indX * CELL_SIZE, indY * CELL_SIZE, CELL_SIZE, CELL_SIZE);
    }

    /**
     * @return индекс ячейки (справа - налево)
     */
    public int getIndX() {
        return indX;
    }

    /**
     * @return индекс ячейки (сверху - вниз)
     */
    public int getIndY() {
        return indY;
    }

    /**
     * Вернуть ячейку, по смещениям относительно данной ячейки.
     * @param delX смещение по оси X
     * @param delY смещение по оси Y
     * @return ячейка, смещенная от этой
     */
    public final Cell getCell(final int delX, final int delY) {
        final int nx = indX + delX;
        final int ny = indY + delY;
        return Matrix.getMatrix().getCell(nx, ny);
    }

    /**
     * Вернуть массив из окружения данной ячейки.
     * @return окружение данной ячейки
     */
    public final AlterCellMove[] getAroundCells() {
        final List<AlterCellMove> tmp = new ArrayList<>();
        for (int i = 0; i < DXS.length; i++) {
            final Cell cell = this.getCell(DXS[i], DYS[i]);
            if (cell != null && !cell.contains(ElementalType.Stone)) {
                tmp.add(new AlterCellMove(cell, D_MOVE_TYPES[i]));
            }
        }
        return tmp.toArray(new AlterCellMove[tmp.size()]);
    }

    public int getBoneRate() {
        return boneRate;
    }

    public void setBoneRate(final int boneRate) {
        this.boneRate = boneRate;
    }

    /**
     * расстояние от данной ячейки до предлагаемой.
     * @param to предлагаемая ячейка
     * @return расстояние
     */
    public final double distance(final Cell to) {
        double dx = Math.abs(to.indX - indX);
        double dy = Math.abs(to.indY - indY);
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Получить доступ к животным в данной ячейке.
     * Добавлять или удалять животных можно только через Cell.
     * @return список животных в этой ячейке
     */
    public final Collection<Elemental> getElements() {
        return elements;
    }

    /**
     * Append the element if not present.
     *
     * @param animal element to be added to this list, if absent
     * @return <tt>true</tt> if the element was added
     */
    public boolean addElement(final Elemental animal) {
        return elements.addIfAbsent(animal);
    }

    /**
     *
     * @param animal element to be removed from this list, if present
     * @return <tt>true</tt> if this list contained the specified element
     */
    public boolean removeElement(final Elemental animal) {
        return elements.remove(animal);
    }


    public boolean isStone() {
        return contains(ElementalType.Stone);
    }

    public boolean contains(final ElementalType type) {
        for (final Elemental elm : elements) {
            if (elm.getType() == type) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final Cell cell = (Cell) o;

        return indX == cell.indX && indY == cell.indY;
    }

    @Override
    public int hashCode() {
        int result = indX;
        result = 31 * result + indY;
        return result;
    }

    @Override
    public String toString() {
        return "Cell{X=" + indX + ", Y=" + indY + '}';
    }

    public void paint(final Graphics gr, final int frame) {
        for (Elemental elm : elements) {
            elm.paint(gr, rectangle, frame);
        }
    }

    public void paintGrid(final Graphics gr) {
        gr.setColor(Color.black);
        gr.drawRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
        if (boneRate > 0) {
            gr.setColor(BONE_COLOR);
            gr.drawString(String.valueOf(boneRate), rectangle.x + 16, rectangle.y + 12);
        }
    }
}
