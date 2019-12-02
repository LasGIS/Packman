import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO

/**
 * Главный запуск программы.
 * @param args аргументы командной строки
 */
fun main(args: Array<String>) {
    // рисуем
    val bim = BufferedImage(200, 100, BufferedImage.TYPE_INT_RGB)
    val gr = bim.graphics
    val x0 = 2
    val y0 = 2
    val x1 = 198
    val y1 = 98
    gr.color = Color(255, 0, 255)
    gr.drawLine(x0, y0, x1, y1)
    gr.drawLine(x0, y1, x1, y0)
    gr.color = Color(255, 255, 255)
    gr.drawRect(x0, y0, x1 - x0, y1 - y0)
    // записываем
    val fileName = if (args.size > 0) args[0] else "saved.png"
    val extension = fileName.substring(fileName.lastIndexOf('.') + 1)
    val outputFile = File(fileName)
    try {
        ImageIO.write(bim, extension, outputFile)
    } catch (e: IOException) {
        e.printStackTrace()
    }
}
