import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * The Class CreateImage.
 * @author Vladimir Laskin
 * @version 1.0
 */
public class CreateImage {
    /**
     * Главный запуск программы.
     * @param args аргументы командной строкина
     */
    public static void main(final String[] args) {

        // рисуем
        final BufferedImage bim = new BufferedImage(200, 100, BufferedImage.TYPE_INT_RGB);
        final Graphics gr = bim.getGraphics();
        final int x0 = 2, y0 = 2, x1 = 198, y1 = 98;
        gr.setColor(new Color(255, 0, 255));
        gr.drawLine(x0, y0, x1, y1);
        gr.drawLine(x0, y1, x1, y0);
        gr.setColor(new Color(255, 255, 255));
        gr.drawRect(x0, y0, x1 - x0, y1 - y0);

        // записываем
        final String fileName = args.length > 0 ? args[0] : "saved.png";
        final String extension = fileName.substring(fileName.lastIndexOf('.') + 1);
        final File outputFile = new File(fileName);
        try {
            ImageIO.write(bim, extension, outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
