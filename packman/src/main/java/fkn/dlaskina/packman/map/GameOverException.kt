package fkn.dlaskina.packman.map

/**
 * The Class GameOverException.
 * @author Vladimir Laskin
 * @version 1.0
 */
class GameOverException(val isWin: Boolean, message: String) : Exception(message)
