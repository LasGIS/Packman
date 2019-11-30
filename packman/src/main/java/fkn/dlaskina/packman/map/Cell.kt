package fkn.dlaskina.packman.map

import fkn.dlaskina.packman.element.AlterCellMove
import fkn.dlaskina.packman.element.Elemental
import fkn.dlaskina.packman.element.ElementalType
import fkn.dlaskina.packman.element.MoveType
import java.awt.Color
import java.awt.Graphics
import java.awt.Rectangle
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList

/**
 * Ячейка карты. Эта ячейка содержит список различных элементов
 *
 * @author Laskin
 * @version 1.0
 * @since 16.06.2010 21:47:16
 */
/**
 * Создание ячейки по нижнему левому углу.
 * @param indexX индекс ячейки (справа - налево)
 * @param indexY индекс ячейки (сверху - вниз)
 */
class Cell(
        /** индекс широты ячейки (справа - налево) 0 - это юг, 100 - это север.  */
        private val indX: Int,
        /** индекс долгота ячейки (сверху - вниз) 0 - это запад, 100 - это восток.  */
        private val indY: Int
) {
    /* это статические объекты класса Cell */
    companion object {
        private val DXS = intArrayOf(0, 1, -1, 0)
        private val DYS = intArrayOf(1, 0, 0, -1)
        private val D_MOVE_TYPES = arrayOf(MoveType.DOWN, MoveType.RIGHT, MoveType.LEFT, MoveType.UP)
        private val BONE_COLOR = Color(125, 0, 0)
        private val PACK_MAN_COLOR = Color(0, 125, 0)
    }

    /** рейтинг ячейки для покемона */
    var packManRate: Int = 0
    /** рейтинг ячейки для костей */
    var boneRate: Int = 0
    /** список сущьностей, населяющих ячейку.  */
    val elements = CopyOnWriteArrayList<Elemental>()
    val rectangle: Rectangle

    init {
        rectangle = Rectangle(indX * Matrix.CELL_SIZE, indY * Matrix.CELL_SIZE, Matrix.CELL_SIZE, Matrix.CELL_SIZE)
    }

    /**
     * Вернуть ячейку, по смещениям относительно данной ячейки.
     * @param delX смещение по оси X
     * @param delY смещение по оси Y
     * @return ячейка, смещенная от этой
     */
    fun getCell(delX: Int, delY: Int): Cell? {
        val nx = indX + delX
        val ny = indY + delY
        return Matrix.matrix?.getCell(nx, ny)
    }

    /**
     * Вернуть массив из окружения данной ячейки.
     * @return окружение данной ячейки
     */
    val aroundCells: Array<AlterCellMove>
        get() {
            val tmp: MutableList<AlterCellMove> = ArrayList()
            for (i in DXS.indices) {
                val cell = getCell(DXS[i], DYS[i])
                if (cell != null && !cell.contains(ElementalType.Stone)) {
                    tmp.add(AlterCellMove(cell, D_MOVE_TYPES[i]))
                }
            }
            return tmp.toTypedArray()
        }

    /**
     * расстояние от данной ячейки до предлагаемой.
     * @param to предлагаемая ячейка
     * @return расстояние
     */
    fun distance(to: Cell): Double {
        val dx = Math.abs(to.indX - indX).toDouble()
        val dy = Math.abs(to.indY - indY).toDouble()
        return Math.sqrt(dx * dx + dy * dy)
    }

    /**
     * Получить доступ к животным в данной ячейке.
     * Добавлять или удалять животных можно только через Cell.
     * @return список животных в этой ячейке
    fun getElements(): Collection<Elemental> {
    return elements
    }
     */

    /**
     * Append the element if not present.
     *
     * @param animal element to be added to this list, if absent
     * @return <tt>true</tt> if the element was added
     */
    fun addElement(animal: Elemental): Boolean {
        return elements.addIfAbsent(animal)
    }

    /**
     *
     * @param animal element to be removed from this list, if present
     * @return <tt>true</tt> if this list contained the specified element
     */
    fun removeElement(animal: Elemental?): Boolean {
        return elements.remove(animal)
    }

    val isStone: Boolean
        get() = contains(ElementalType.Stone)

    operator fun contains(type: ElementalType): Boolean {
        for (elm in elements) {
            if (elm.type == type) {
                return true
            }
        }
        return false
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o == null || javaClass != o.javaClass) {
            return false
        }
        val cell = o as Cell
        return indX == cell.indX && indY == cell.indY
    }

    override fun hashCode(): Int {
        var result = indX
        result = 31 * result + indY
        return result
    }

    override fun toString(): String {
        return "Cell{X=$indX, Y=$indY}"
    }

    fun paint(gr: Graphics?, frame: Int) {
        for (elm in elements) {
            elm.paint(gr, rectangle, frame)
        }
    }

    fun paintGrid(gr: Graphics) {
        gr.color = Color.black
        gr.drawRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height)
        if (boneRate > 0) {
            gr.color = BONE_COLOR;
            gr.drawString(boneRate.toString(), rectangle.x + 16, rectangle.y + 12);
        }
        if (packManRate > 0) {
            gr.color = PACK_MAN_COLOR;
            gr.drawString(packManRate.toString(), rectangle.x + 1, rectangle.y + 12);
        }
    }
}