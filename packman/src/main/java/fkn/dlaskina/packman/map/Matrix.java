package fkn.dlaskina.packman.map;

import java.awt.Dimension;
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fkn.dlaskina.packman.element.AbstractEnemy;
import fkn.dlaskina.packman.element.ActiveElemental;
import fkn.dlaskina.packman.element.AlterCellMove;
import fkn.dlaskina.packman.element.Bones;
import fkn.dlaskina.packman.element.ElementalType;
import fkn.dlaskina.packman.element.Enemy;
import fkn.dlaskina.packman.element.EnemyDummy;
import fkn.dlaskina.packman.element.MedicalBox;
import fkn.dlaskina.packman.element.PackMan;
import fkn.dlaskina.packman.element.Stone;
import fkn.dlaskina.packman.element.Surprise;
import fkn.dlaskina.packman.panels.ConfigPanel;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import static fkn.dlaskina.packman.element.SurpriseType.aggressive;
import static fkn.dlaskina.packman.element.SurpriseType.simple;
import static fkn.dlaskina.packman.element.SurpriseType.speed;

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
    private static Matrix MATRIX;
    /** уровень игры. */
    private static int level = 0;
    private static final int levelMax = 2;
    /** размер матрицы по горизонтали. */
    public static int MATRIX_SIZE_X;
    /** размер матрицы по вертикали. */
    public static int MATRIX_SIZE_Y;

    /** сама матрица. */
    private final Cell[][] cells;
    /** pokemon. */
    private PackMan packMan = null;
    /** pokemon. */
    private List<ActiveElemental> elements = Collections.synchronizedList(new ArrayList<>());

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
        int bonusCountMax = 0;
        for (int y = 0; y < MATRIX_SIZE_Y; y++) {
            for (int x = 0; x < MATRIX_SIZE_X; x++) {
                final Cell cell = new Cell(x, y);
                final char ch = list.get(y).charAt(x);
                switch (ch) {
                    case 'p': {
                        final PackMan packMan = new PackMan(cell);
                        cell.addElement(packMan);
                        this.packMan = packMan;
                        elements.add(packMan);
                        break;
                    }
                    case 'e': {
                        final AbstractEnemy enemy = new Enemy(cell);
                        cell.addElement(enemy);
                        cell.addElement(new Surprise(simple));
                        bonusCountMax++;
                        elements.add(enemy);
                        break;
                    }
                    case 'd': {
                        final AbstractEnemy enemy = new EnemyDummy(cell);
                        cell.addElement(enemy);
                        cell.addElement(new Surprise(simple));
                        bonusCountMax++;
                        elements.add(enemy);
                        break;
                    }
                    case '1':
                        cell.addElement(new Surprise(simple));
                        bonusCountMax++;
                        break;
                    case '2':
                        cell.addElement(new Surprise(speed));
                        bonusCountMax++;
                        break;
                    case '3':
                        cell.addElement(new Surprise(aggressive));
                        bonusCountMax++;
                        break;
                    case ' ':
                        break;
                    default:
                        cell.addElement(new Stone());
                        break;
                }
                cells[y][x] = cell;
            }
        }
        ConfigPanel.setBonusCountMax(bonusCountMax);
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
    public static Matrix createMatrix(final boolean isNewLevel) {
        if (isNewLevel && level < levelMax) level++;
        MATRIX = new Matrix("matrix" + level + ".txt");
        return MATRIX;
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
        return isValidIndex(x, y) ? cells[y][x] : null;
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

    public void paint(final Graphics gr, final int frame) {
        for (final Cell[] yCells : cells) {
            for (final Cell cell : yCells) {
                cell.paint(gr, frame);
            }
        }
    }

    public void paintGrid(final Graphics gr) {
        for (final Cell[] yCells : cells) {
            for (final Cell cell : yCells) {
                cell.paintGrid(gr);
            }
        }
    }

    public Dimension getSize() {
        return new Dimension(MATRIX_SIZE_X * CELL_SIZE + 1, MATRIX_SIZE_Y * CELL_SIZE + 1);
    }

    public PackMan getPackMan() {
        return packMan;
    }

    public List<ActiveElemental> getElements() {
        return elements;
    }

    /**
     * удаляем врага
     * @param enemy враг
     */
    public synchronized void removeEnemy(final AbstractEnemy enemy) {
        LOG.info("удаляем врага", new Throwable());
        final Cell enemyCell = enemy.getCell();
        enemyCell.removeElement(enemy);
        elements.remove(enemy);

        final Bones bones = new Bones(enemyCell, enemy.isDummy());
        enemyCell.addElement(bones);
        elements.add(bones);
        double maxDist = 5;
        Cell cellMaxDist = null;
        for (Cell[] row : cells) {
            for (Cell cell : row) {
                if (!cell.contains(ElementalType.Stone)) {
                    final double dist = enemyCell.distance(cell);
                    if (dist > maxDist) {
                        maxDist = dist;
                        cellMaxDist = cell;
                    }
                }
            }
        }
        if (cellMaxDist != null) {
            cellMaxDist.addElement(new MedicalBox());
            createBoneRate();
        }
    }

    public void createBoneRate() {
        final ArrayList<Cell> temp = new ArrayList<>();
        clearBoneRate(temp);
        int nextRate = 2;
        while (!temp.isEmpty()) {
            final ArrayList<Cell> tempCell = new ArrayList<>();
            for (final Cell tCell : temp) {
                for (final AlterCellMove cellMove : tCell.getAroundCells()) {
                    final Cell cell = cellMove.getCell();
                    if (cell.getBoneRate() == 0) {
                        cell.setBoneRate(nextRate);
                        tempCell.add(cell);
                    }
                }
            }
            temp.clear();
            temp.addAll(tempCell);
            nextRate++;
        }
    }

    private void clearBoneRate(final ArrayList<Cell> temp) {
        for (Cell[] row : cells) {
            for (Cell cell : row) {
                if (!cell.contains(ElementalType.Stone)) {
                    if (cell.contains(ElementalType.MedBox)) {
                        temp.add(cell);
                        cell.setBoneRate(1);
                    } else {
                        cell.setBoneRate(0);
                    }
                }
            }
        }
    }

    public void createPackManRate() {
        final ArrayList<Cell> temp = new ArrayList<>();
        clearPackManRate(temp);
        int nextRate = 2;
        while (!temp.isEmpty()) {
            final ArrayList<Cell> tempCell = new ArrayList<>();
            for (final Cell tCell : temp) {
                for (final AlterCellMove cellMove : tCell.getAroundCells()) {
                    final Cell cell = cellMove.getCell();
                    if (cell.getPackManRate() == 0) {
                        cell.setPackManRate(nextRate);
                        tempCell.add(cell);
                    }
                }
            }
            temp.clear();
            temp.addAll(tempCell);
            nextRate++;
        }
    }

    private void clearPackManRate(final ArrayList<Cell> temp) {
        for (Cell[] row : cells) {
            for (Cell cell : row) {
                if (!cell.contains(ElementalType.Stone)) {
                    if (cell.contains(ElementalType.PackMan)) {
                        temp.add(cell);
                        cell.setPackManRate(1);
                    } else {
                        cell.setPackManRate(0);
                    }
                }
            }
        }
    }
}
