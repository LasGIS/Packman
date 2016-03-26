/**
 * @(#)Cell.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package fkn.dlaskina.packman.map;

import fkn.dlaskina.packman.element.Elemental;
import fkn.dlaskina.packman.element.PackMan;
import fkn.dlaskina.packman.element.Surprise;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

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
    private List<Elemental> elements = new ArrayList<>();

    /**
     * Создание ячейки по нижнему левому углу.
     * @param indexX индекс ячейки по широте (от низа - юг)
     * @param indexY индекс ячейки по долготе (от левого края - запад)
     */
    public Cell(final int indexX, final int indexY) {
        this.indX = indexX;
        this.indY = indexY;
    }

    /**
     * Вернуть индекс ячейки по широте (от низа - юг).
     * @return индекс ячейки по широте
     */
    public int getIndX() {
        return indX;
    }

    /**
     * Вернуть индекс ячейки по долготе (от левого края - запад).
     * @return индекс ячейки по долготе
     */
    public int getIndY() {
        return indY;
    }

    /**
     * Вернуть ячейку, по смещениям относительно данной ячейки.
     * @param delX смещение по x
     * @param delY смещение по y
     * @return ячейка, смещенная от этой
     */
    public final Cell offset(final int delX, final int delY) {
        return this.getCell(indX + delX, indY + delY);
    }

    /**
     * Вернуть ячейку, по смещениям относительно данной ячейки.
     * @param delX смещение по оси X (положительное смещение вверх)
     * @param delY смещение по оси Y (положительное смещение вправо)
     * @return ячейка
     */
    public final Cell getCell(final int delX, final int delY) {
        final int nx = (Matrix.MATRIX_SIZE_X + indX + delX) % Matrix.MATRIX_SIZE_X;
        final int ny = (Matrix.MATRIX_SIZE_Y + indY + delY) % Matrix.MATRIX_SIZE_Y;
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
     * возвращаем Уникальный ключ ячейки.
     * @return ключ ячейки
     */
    public String getKey() {
        return "" + getIndX() + "_" + getIndY();
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

        return elements.add(animal);
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

    final Elemental test = new Surprise();

    public void paint(Graphics gr) {
        final Rectangle rect = new Rectangle(indX * CELL_SIZE, indY * CELL_SIZE, CELL_SIZE, CELL_SIZE);
        test.paint(gr, rect);
        for (Elemental elm : elements) {
            elm.paint(gr, rect);
        }
    }
}
