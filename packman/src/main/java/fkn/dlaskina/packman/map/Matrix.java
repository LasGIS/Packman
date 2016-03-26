/**
 * @(#)Matrix.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package fkn.dlaskina.packman.map;

import fkn.dlaskina.util.ResourceLoader;

import java.awt.*;

/**
 * Матрица элементов карты.
 * @author Laskin
 * @version 1.0
 * @since 06.06.2010 22:24:26
 */
public final class Matrix {

    /** размер матрицы по вертикали (низ - ноль). */
    public static final int MATRIX_SIZE_X = 16; // ResourceLoader.getInteger("matrix.latitude.size");
    /** размер матрицы по горизонтали (лево - ноль). */
    public static final int MATRIX_SIZE_Y = 10; //ResourceLoader.getInteger("matrix.longitude.size");
    /** размер элементарной ячейки в пикселях. */
    public static final int CELL_SIZE = 30;
    /** сама матрица. */
    private final Cell[][] cells = new Cell[MATRIX_SIZE_X][MATRIX_SIZE_Y];
    /** singleton матрицы. */
    private static final Matrix MATRIX = new Matrix();

    /**
     * Создаем и заполняем матрицу.
     */
    private Matrix() {
        for (int x = 0; x < MATRIX_SIZE_X; x++) {
            for (int y = 0; y < MATRIX_SIZE_Y; y++) {
                cells[x][y] = new Cell(x, y);
            }
        }
    }

    /**
     * @return выдаем singleton матрицы по первому требованию.
     */
    public static Matrix getMatrix() {
        return MATRIX;
    }

    /**
     * Вернуть ячейку по индексам.
     * @param x индекс широты ячейки
     * @param y индекс долготы ячейки
     * @return ячейка или NULL если вышли из диапазона
     */
    public Cell getCell(final int x, final int y) {
        return isValidIndex(x, y) ? cells[x][y] : null;
    }

    /**
     * Вернуть ячейку по индексам.
     * @param x индекс широты ячейки
     * @param y индекс долготы ячейки
     * @return ячейка или NULL если вышли из диапазона
     */
    public boolean isValidIndex(final int x, final int y) {
        return x >= 0 && x < MATRIX_SIZE_X && y >= 0 && y < MATRIX_SIZE_Y;
    }

    public void paint(Graphics gr) {
        for (final Cell[] xCells : cells) {
            for (final Cell cell : xCells) {
                cell.paint(gr);
            }
        }
    }

    public Dimension getSize() {
        return new Dimension(MATRIX_SIZE_X * CELL_SIZE, MATRIX_SIZE_Y * CELL_SIZE);
    }
}
