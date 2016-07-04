package fkn.dlaskina.packman;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;

/**
 * The Class ShowFonts.
 * @author Vladimir Laskin
 * @version 1.0
 */
public class ShowFonts {
    /**
     * Главный запуск программы.
     * @param args аргументы командной строки
     */
    public static void main(final String[] args) {
        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

            // поиск доступных шрифтов
            String[] fontFamilies = ge.getAvailableFontFamilyNames();
            for (String fontName : fontFamilies) {
                System.out.println(fontName);
            }
            // регистрация шрифта для java
            Font font = Font.createFont(1, new File("MyFont.fnt"));
            ge.registerFont(font);

        } catch (FontFormatException | IOException ex) {
            ex.printStackTrace();
        }

    }
}
