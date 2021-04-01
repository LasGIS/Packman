/**
 * @(#)Util.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */
package fkn.dlaskina.util

import org.slf4j.LoggerFactory
import java.awt.Dimension
import java.awt.Image
import java.awt.event.ActionListener
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStreamReader
import javax.imageio.ImageIO
import javax.swing.ImageIcon
import javax.swing.JButton
import javax.swing.JMenu
import javax.swing.JMenuItem

/**
 * Created by IntelliJ IDEA.
 * @author VLaskin
 * @version 1.0
 * Date: Sep 3, 2004
 * Time: 6:20:54 PM
 */
object Util {
    private val log = LoggerFactory.getLogger(Util::class.java)
    private const val BUFF_SIZE = 1024

    /**
     * Загрузит иконку из каталога image.
     * @param name имя иконки
     * @return ImageIcon
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     */
    @Throws(IOException::class)
    fun loadImageIcon(name: String?): ImageIcon {
        return ImageIcon(loadImage(name))
    }

    /**
     * Загрузит иконку из каталога image.
     * @param name имя иконки
     * @return ImageIcon
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     */
    @Throws(IOException::class)
    fun loadImage(name: String?): Image {
        val ldr = Util::class.java.classLoader
        val `in` = ldr.getResourceAsStream("image/$name")
        return if (`in` != null) {
            try {
                ImageIO.read(`in`)
            } finally {
                `in`.close()
            }
        } else {
            throw IOException("Файл \"image/$name\" не найден!")
        }
    }

    /**
     * Загрузит ресурс из classpath.
     * @param name имя иконки
     * @return ImageIcon
     */
    fun loadString(name: String): StringBuilder? {
        val ldr = Util::class.java.classLoader
        val `in` = ldr.getResourceAsStream(name)
        val sb = StringBuilder()
        if (`in` != null) {
            try {
                InputStreamReader(`in`, "UTF-8").use { reader -> return getStringBuilder(sb, reader) }
            } catch (ex: IOException) {
                log.error(ex.message, ex)
            }
        } else {
            log.error("Файл \"$name\" не найден!")
        }
        return null
    }

    @Throws(IOException::class)
    private fun getStringBuilder(sb: StringBuilder, reader: InputStreamReader): StringBuilder {
        val buf = CharArray(BUFF_SIZE)
        var count = reader.read(buf, 0, BUFF_SIZE)
        while (count >= 0) {
            sb.append(buf, 0, count)
            count = reader.read(buf, 0, BUFF_SIZE)
        }
        return sb
    }

    /**
     * Загрузит ресурс из classpath.
     * @param fileName имя иконки
     * @return ImageIcon
     */
    fun loadStringFromFile(fileName: String): StringBuilder? {
        try {
            FileInputStream(fileName).use { `in` ->
                InputStreamReader(`in`, "UTF-8").use { reader ->
                    val sb = StringBuilder()
                    return getStringBuilder(sb, reader)
                }
            }
        } catch (ex: FileNotFoundException) {
            log.error("Файл \"$fileName\" не найден!")
        } catch (ex: IOException) {
            log.error(ex.message, ex)
        }
        return null
    }

    /**
     * создать кнопку.
     * @param name надпись на кнопке
     * @param iconName иконка на кнопке
     * @param width ширина кнопки
     * @param height высота кнопки
     * @param toolTip всплывающая подсказка
     * @param actListener действие на кнопку
     * @return created JButton
     */
    fun createImageButton(
        name: String?,
        iconName: String?,
        width: Int, height: Int,
        toolTip: String?,
        actListener: ActionListener?
    ): JButton {
        val button = JButton()
        if (name != null) {
            button.text = name
        }
        if (iconName != null) {
            try {
                button.icon = loadImageIcon(iconName)
            } catch (e: IOException) {
                log.error(e.message)
            }
        }
        //        button.setMaximumSize(new Dimension(width, height));
        button.minimumSize = Dimension(width, height)
        button.preferredSize = Dimension(width, height)
        button.toolTipText = toolTip
        button.addActionListener(actListener)
        return button
    }

    /**
     * создать кнопку.
     * @param set настройки для создания кнопки
     * @return created JButton
     */
    fun createImageButton(set: SettingToolBarItem): JButton {
        val button = JButton()
        if (set.text != null) {
            button.text = set.text
        }
        if (set.image != null) {
            try {
                button.icon = loadImageIcon(set.image)
            } catch (e: IOException) {
                log.error(e.message)
            }
        }
        val dim = Dimension(set.width!!, set.height!!)
        //        button.setMaximumSize(new Dimension(width, height));
        button.minimumSize = dim
        button.preferredSize = dim
        button.toolTipText = set.toolTip
        button.addActionListener(set.action)
        return button
    }

    /**
     * Создаём и возвращаем объект типа JMenu или JMenuItem.
     * @param set объект типа SettingMenuItem, содержащий информацию для создания меню
     * @return объект типа JMenu или JMenuItem
     */
    fun createImageMenuItem(
        set: SettingMenuItem
    ): JMenuItem {
        return if (set.items == null) {
            // создаём одну строку меню
            val menu = JMenuItem()
            menu.text = set.text
            if (set.image != null) {
                try {
                    menu.icon = loadImageIcon(set.image)
                } catch (e: IOException) {
                    log.error(e.message)
                }
            }
            menu.toolTipText = set.toolTip
            menu.addActionListener(set.action)
            menu
        } else {
            // создаём выпадающее подменю
            val menu = JMenu()
            menu.text = set.text
            if (set.image != null) {
                try {
                    menu.icon = loadImageIcon(set.image)
                } catch (e: IOException) {
                    log.error(e.message)
                }
            }
            menu.toolTipText = set.toolTip
            val setts = set.items
            for (sett in setts!!) {
                menu.add(createImageMenuItem(sett))
            }
            menu
        }
    }
}
