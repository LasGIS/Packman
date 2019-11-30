package fkn.dlaskina.packman.map

import fkn.dlaskina.packman.element.*
import fkn.dlaskina.packman.panels.ConfigPanel
import org.apache.log4j.LogManager
import java.awt.Dimension
import java.awt.Graphics
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList

/**
 * Матрица элементов карты.
 * @author Laskin
 * @version 1.0
 * @since 06.06.2010 22:24:26
 */
class Matrix private constructor(name: String) {
    /** сама матрица.  */
    private val cells: Array<Array<Cell?>>
    /** pokemon.  */
    private var packMan: PackMan? = null
    /** pokemon.  */
    private val elements: MutableList<ActiveElemental> = CopyOnWriteArrayList()

    companion object {
        private val log = LogManager.getLogger(Matrix::class.java)
        /** размер элементарной ячейки в пикселях.  */
        const val CELL_SIZE = 30
        /**
         * @return выдаем singleton матрицы по первому требованию.
         */
        /** singleton матрицы.  */
        var matrix: Matrix? = null
        /** уровень игры.  */
        private var level = 0
        private const val levelMax = 2
        /** размер матрицы по горизонтали.  */
        var MATRIX_SIZE_X: Int = 10
        /** размер матрицы по вертикали.  */
        var MATRIX_SIZE_Y: Int = 20

        /**
         * @return выдаем singleton матрицы по первому требованию.
         */
        @JvmStatic
        fun createMatrix(isNewLevel: Boolean): Matrix? {
            if (isNewLevel && level < levelMax) level++
            matrix = Matrix("matrix$level.txt")
            return matrix
        }
    }

    /**
     * Читаем матрицу из файла
     * @param fileName имя файла
     * @param retMaxX возвращаемый размер по горизонтали
     */
    private fun load(fileName: String, retMaxX: IntArray): ArrayList<String> {
        val list = ArrayList<String>()
        var maxX = 0
        val ldr = Matrix::class.java.classLoader
        try {
            ldr.getResourceAsStream(fileName).use { `in` ->
                InputStreamReader(`in`, "UTF-8").use { rd ->
                    BufferedReader(rd).use { br ->
                        var line: String
                        while (br.readLine().also { line = it } != null) {
                            list.add(line)
                            if (maxX < line.length) {
                                maxX = line.length
                            }
                        }
                    }
                }
            }
        } catch (ex: IOException) {
            log.error(ex.message, ex)
        }
        retMaxX[0] = maxX
        return list
    }

    /**
     * Вернуть ячейку по индексам.
     * @param x индекс широты ячейки
     * @param y индекс долготы ячейки
     * @return ячейка или NULL если вышли из диапазона
     */
    fun getCell(x: Int, y: Int): Cell? {
        return if (isValidIndex(x, y)) cells[y][x] else null
    }

    /**
     * Вернуть ячейку по индексам.
     * @param x индекс широты ячейки
     * @param y индекс долготы ячейки
     * @return ячейка или NULL если вышли из диапазона
     */
    private fun isValidIndex(x: Int, y: Int): Boolean {
        return x in 0 until MATRIX_SIZE_X && y in 0 until MATRIX_SIZE_Y
    }

    fun paint(gr: Graphics?, frame: Int) {
        for (yCells in cells) {
            for (cell in yCells) {
                cell!!.paint(gr, frame)
            }
        }
    }

    fun paintGrid(gr: Graphics?) {
        for (yCells in cells) {
            for (cell in yCells) {
                cell!!.paintGrid(gr!!)
            }
        }
    }

    val size: Dimension
        get() = Dimension(MATRIX_SIZE_X * CELL_SIZE + 1, MATRIX_SIZE_Y * CELL_SIZE + 1)

    fun getElements(): List<ActiveElemental> {
        return elements
    }

    /**
     * удаляем врага
     * @param enemy враг
     */
    fun removeEnemy(enemy: AbstractEnemy) {
        if (enemy.isDeleted) {
            log.info("Вторичное удаление врага {$enemy}", Throwable())
            val enemyCell = enemy.cell
            enemyCell?.removeElement(enemy)
            elements.remove(enemy)
        } else {
            enemy.isDeleted = true
            log.info("удаляем врага {$enemy}", Throwable())
            val enemyCell = enemy.cell
            enemyCell.removeElement(enemy)
            elements.remove(enemy)
            val bones = Bones(enemyCell, enemy.isDummy)
            enemyCell.addElement(bones)
            elements.add(bones)
            var maxDist = 5.0
            var cellMaxDist: Cell? = null
            for (row in cells) {
                for (cell in row) {
                    if (!cell!!.contains(ElementalType.Stone)) {
                        val dist = enemyCell.distance(cell)
                        if (dist > maxDist) {
                            maxDist = dist
                            cellMaxDist = cell
                        }
                    }
                }
            }
            if (cellMaxDist != null) {
                cellMaxDist.addElement(MedicalBox())
                createBoneRate()
            }
        }
    }

    fun createBoneRate() {
        val temp = ArrayList<Cell?>()
        clearBoneRate(temp)
        var nextRate = 2
        while (!temp.isEmpty()) {
            val tempCell = ArrayList<Cell>()
            for (tCell in temp) {
                for (cellMove in tCell!!.aroundCells) {
                    val cell = cellMove.cell
                    if (cell.boneRate == 0) {
                        cell.boneRate = nextRate
                        tempCell.add(cell)
                    }
                }
            }
            temp.clear()
            temp.addAll(tempCell)
            nextRate++
        }
    }

    private fun clearBoneRate(temp: ArrayList<Cell?>) {
        for (row in cells) {
            for (cell in row) {
                if (!cell!!.contains(ElementalType.Stone)) {
                    if (cell.contains(ElementalType.MedBox)) {
                        temp.add(cell)
                        cell.boneRate = 1
                    } else {
                        cell.boneRate = 0
                    }
                }
            }
        }
    }

    fun createPackManRate() {
        val temp = ArrayList<Cell?>()
        clearPackManRate(temp)
        var nextRate = 2
        while (!temp.isEmpty()) {
            val tempCell = ArrayList<Cell>()
            for (tCell in temp) {
                for (cellMove in tCell!!.aroundCells) {
                    val cell = cellMove.cell
                    if (cell.packManRate == 0) {
                        cell.packManRate = nextRate
                        tempCell.add(cell)
                    }
                }
            }
            temp.clear()
            temp.addAll(tempCell)
            nextRate++
        }
    }

    private fun clearPackManRate(temp: ArrayList<Cell?>) {
        for (row in cells) {
            for (cell in row) {
                if (!cell!!.contains(ElementalType.Stone)) {
                    if (cell.contains(ElementalType.PackMan)) {
                        temp.add(cell)
                        cell.packManRate = 1
                    } else {
                        cell.packManRate = 0
                    }
                }
            }
        }
    }

    /**
     * Создаем и заполняем матрицу.
     * @param name имя ресурса с матрицей
     */
    init {
        val sizeX = intArrayOf(0)
        val list = load(name, sizeX)
        MATRIX_SIZE_X = sizeX[0]
        MATRIX_SIZE_Y = list.size
        cells = Array(MATRIX_SIZE_Y) { arrayOfNulls<Cell>(MATRIX_SIZE_X) }
        var bonusCountMax = 0
        for (y in 0 until MATRIX_SIZE_Y) {
            for (x in 0 until MATRIX_SIZE_X) {
                val cell = Cell(x, y)
                val ch = list[y][x]
                when (ch) {
                    'p' -> {
                        val pokemon = PackMan(cell)
                        packMan = pokemon
                        cell.addElement(pokemon)
                        elements.add(pokemon)
                    }
                    'e' -> {
                        val enemy: AbstractEnemy = Enemy(cell)
                        cell.addElement(enemy)
                        cell.addElement(Surprise(SurpriseType.simple))
                        bonusCountMax++
                        elements.add(enemy)
                    }
                    'd' -> {
                        val enemy: AbstractEnemy = EnemyDummy(cell)
                        cell.addElement(enemy)
                        cell.addElement(Surprise(SurpriseType.simple))
                        bonusCountMax++
                        elements.add(enemy)
                    }
                    '1' -> {
                        cell.addElement(Surprise(SurpriseType.simple))
                        bonusCountMax++
                    }
                    '2' -> {
                        cell.addElement(Surprise(SurpriseType.speed))
                        bonusCountMax++
                    }
                    '3' -> {
                        cell.addElement(Surprise(SurpriseType.aggressive))
                        bonusCountMax++
                    }
                    ' ' -> {
                    }
                    else -> cell.addElement(Stone())
                }
                cells[y][x] = cell
            }
        }
        ConfigPanel.setBonusCountMax(bonusCountMax)
    }
}