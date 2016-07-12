import java.awt.GraphicsEnvironment;

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
//        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

            // поиск доступных шрифтов
            String[] fontFamilies = ge.getAvailableFontFamilyNames();
            for (String fontName : fontFamilies) {
                System.out.println(fontName);
            }
            // регистрация шрифта для java
//            Font font = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/Candara.TTF"));
//            ge.registerFont(font);

/*
        } catch (FontFormatException | IOException ex) {
            ex.printStackTrace();
        }
*/

    }
}
