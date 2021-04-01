import java.awt.Font
import java.awt.FontFormatException
import java.awt.GraphicsEnvironment
import java.io.File
import java.io.IOException

/**
 * Главный запуск программы.
 */
fun main() {
    try {
        val ge = GraphicsEnvironment.getLocalGraphicsEnvironment()
        // поиск доступных шрифтов
        val fontFamilies = ge.availableFontFamilyNames
        for (fontName in fontFamilies) {
            println(fontName)
        }
        // регистрация шрифта для java
        val font = Font.createFont(Font.TRUETYPE_FONT, File("fonts/Candara.TTF"))
        ge.registerFont(font)
    } catch (ex: FontFormatException) {
        ex.printStackTrace()
    } catch (ex: IOException) {
        ex.printStackTrace()
    }
}
