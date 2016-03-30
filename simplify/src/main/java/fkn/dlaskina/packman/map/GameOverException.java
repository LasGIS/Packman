package fkn.dlaskina.packman.map;

/**
 * The Class GameOverException.
 * @author Vladimir Laskin
 * @version 1.0
 */
public class GameOverException extends Exception {

    public final boolean win;

    public GameOverException(final boolean isWin, final String message) {
        super(message);
        win = isWin;
    }

    public boolean isWin() {
        return win;
    }
}
