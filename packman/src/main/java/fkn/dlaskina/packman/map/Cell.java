package fkn.dlaskina.packman.map;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import fkn.dlaskina.packman.element.Elemental;
import fkn.dlaskina.packman.element.ElementalType;

import static fkn.dlaskina.packman.map.Matrix.CELL_SIZE;

/**
 * Ячейка карты. Эта ячейка содержит список различных элементов
 *
 * @author Laskin
 * @version 1.0
 * @since 16.06.2010 21:47:16
 */
public class Cell {

    /** индекс широты ячейки 0 - это юг, 100 - это север. */
    private int indX;
    /** индекс долгота ячейки 0 - это запад, 100 - это восток. */
    private int indY;
    /** список сущьностей, населяющих ячейку. */
    private CopyOnWriteArrayList<Elemental> elements = new CopyOnWriteArrayList<>();

    /**
     * Создание ячейки по нижнему левому углу.
     * @param indexX индекс ячейки (справа - налево)
     * @param indexY индекс ячейки (сверху - вниз)
     */
    public Cell(final int indexX, final int indexY) {
        this.indX = indexX;
        this.indY = indexY;
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
     * расстояние от данной ячейки до предлагаемой.
     * @param to предлагаемая ячейка
     * @return расстояние
     */
    public final double distance(final Cell to) {
        double dx = Math.abs(to.indX - indX);
        if (dx > Matrix.MATRIX_SIZE_X / 2.) {
            dx -= Matrix.MATRIX_SIZE_X;
        }
        double dy = Math.abs(to.indY - indY);
        if (dy > Matrix.MATRIX_SIZE_Y / 2.) {
            dy -= Matrix.MATRIX_SIZE_Y;
        }
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Получить доступ к животным в данной ячейке.
     * Добавлять или удалять животных можно только через Cell.
     * @return список животных в этой ячейке
     */
    public final List<Elemental> getElements() {
        return elements;
    }

    /**
     * Append the element if not present.
     *
     * @param animal element to be added to this list, if absent
     * @return <tt>true</tt> if the element was added
     */
    public boolean addAnimal(final Elemental animal) {
        return elements.addIfAbsent(animal);
    }

    /**
     *
     * @param animal element to be removed from this list, if present
     * @return <tt>true</tt> if this list contained the specified element
     */
    public boolean removeAnimal(final Elemental animal) {
        return elements.remove(animal);
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
        final Rectangle rect = new Rectangle(indX * CELL_SIZE, indY * CELL_SIZE, CELL_SIZE, CELL_SIZE);
        gr.setColor(Color.black);
        gr.drawRect(rect.x, rect.y, rect.width, rect.height);
        for (Elemental elm : elements) {
            elm.paint(gr, rect, frame);
        }
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
}
