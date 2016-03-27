/**
 * @(#)Matrix.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package fkn.dlaskina.packman.map;

import fkn.dlaskina.packman.element.*;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.awt.Dimension;
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;

/**
 * Матрица элементов карты.
 * @author Laskin
 * @version 1.0
 * @since 06.06.2010 22:24:26
 */
public final class Matrix {

    private static final Logger LOG = LogManager.getLogger(Matrix.class);

    /** размер элементарной ячейки в пикселях. */
    public static final int CELL_SIZE = 30;
    /** singleton матрицы. */
    private static final Matrix MATRIX = new Matrix("matrix.txt");

    /** размер матрицы по горизонтали. */
    public static int MATRIX_SIZE_X;
    /** размер матрицы по вертикали. */
    public static int MATRIX_SIZE_Y;

    /** сама матрица. */
    private final Cell[][] cells;
    /** pokemon. */
    private PackMan packMan = null;
    /** pokemon. */
    private List<ActiveElemental> animals = new ArrayList<>();

    /**
     * Создаем и заполняем матрицу.
     * @param name имя ресурса с матрицей
     */
    private Matrix(String name) {
        int[] sizeX = {0};
        final ArrayList<String> list = load(name, sizeX);
        MATRIX_SIZE_X = sizeX[0];
        MATRIX_SIZE_Y = list.size();
        cells = new Cell[MATRIX_SIZE_Y][MATRIX_SIZE_X];
        for (int y = 0; y < MATRIX_SIZE_Y; y++) {
            for (int x = 0; x < MATRIX_SIZE_X; x++) {
                final Cell cell = new Cell(x, y);
                final char ch = list.get(y).charAt(x);
                switch (ch) {
                    case 'p':
                        cell.addAnimal(new PackMan());
                        break;
                    case 'e':
                        cell.addAnimal(new Enemy());
                        break;
                    case 's':
                        cell.addAnimal(new Stone());
                        break;
                    case '1':
                        cell.addAnimal(new Surprise());
                        break;
                }
                cells[y][x] = cell;
            }
        }
    }

    /**
     * Читаем матрицу из файла
     * @param fileName имя файла
     */
    private  ArrayList<String> load(final String fileName, final int[] retMaxX) {
        final ArrayList<String> list = new ArrayList<>();
        int maxX = 0;
        final ClassLoader ldr = Matrix.class.getClassLoader();
        try (
            final InputStream in = ldr.getResourceAsStream(fileName);
            final InputStreamReader rd = new InputStreamReader(in, "UTF-8");
            final BufferedReader br = new BufferedReader(rd);
        ) {
            String line;
            while ((line = br.readLine()) != null) {
                list.add(line);
                if (maxX < line.length()) {
                    maxX = line.length();
                }
            }
        } catch (IOException ex) {
            LOG.error(ex.getMessage(), ex);
        }
        retMaxX[0] = maxX;
        return list;
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
        for (final Cell[] yCells : cells) {
            for (final Cell cell : yCells) {
                cell.paint(gr);
            }
        }
    }

    public Dimension getSize() {
        return new Dimension(MATRIX_SIZE_X * CELL_SIZE + 1, MATRIX_SIZE_Y * CELL_SIZE + 1);
    }

    public PackMan getPackMan() {
        return packMan;
    }

    public List<ActiveElemental> getAnimals() {
        return animals;
    }
}
