package fkn.dlaskina.packman.panels

import mu.KotlinLogging
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Font
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.Insets
import javax.swing.JButton
import javax.swing.JLabel
import javax.swing.JPanel

private val log = KotlinLogging.logger{}

/**
 * Панель конфигурации.
 * @author VLaskin
 * @version 1.0 Date: 13.01.2005 16:38:05
 */
class ConfigPanel : JPanel() {
    /** всего бонусов  */
    private val lblBonus = JLabel("0", JLabel.LEFT)
    /** собранных бонусов  */
    private val lblBonusMax = JLabel("0", JLabel.LEFT)

    companion object {
        var configPanel: ConfigPanel? = null
        /** число полученых подарков.  */
        private var bonusCount = 0
        /** общее число подарков.  */
        private var bonusCountMax = 0

        /**
         * Добавить подарок и проверить - не последний ли?
         * @return true если это последний бонус
         */
        fun addBonus(): Boolean {
            bonusCount++
            configPanel?.let {
                it.lblBonus.text = bonusCount.toString()
            }
            return bonusCountMax <= bonusCount
        }

        fun setBonusCountMax(maxCount: Int) {
            bonusCount = 0
            bonusCountMax = maxCount
            configPanel?.let {
                it.lblBonusMax.text = bonusCountMax.toString()
            }
        }
    }

    /**
     * Конструктор.
     * final String[] fontNames = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
     * log.info(fontNames);
     */
    init {
        val controlPanel = JPanel(GridBagLayout())
        val c = GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, Insets(2, 10, 2, 10), 0, 0)
        val mainInsets = Insets(2, 10, 2, 10)
        val deskInsets = Insets(2, 10, 2, 2)
        val digitInsets = Insets(2, 5, 2, 10)
        val mainFont = Font("Arial", Font.BOLD, 14)
        val deskFont = Font("Arial", Font.PLAIN, 12)
        val digitFont = Font("Courier New", Font.BOLD, 12)
        c.gridwidth = 2
        c.insets = mainInsets
        var label = JLabel("Призы:", JLabel.CENTER)
        label.font = mainFont
        label.foreground = Color.BLUE
        controlPanel.add(label, c)
        c.gridy = 1
        c.gridx = 0
        c.gridwidth = 1
        c.insets = deskInsets
        label = JLabel("всего:", JLabel.RIGHT)
        label.font = deskFont
        controlPanel.add(label, c)
        c.gridx = 1
        c.insets = digitInsets
        lblBonusMax.font = digitFont
        lblBonusMax.foreground = Color.RED
        controlPanel.add(lblBonusMax, c)
        c.gridx = 0
        c.gridy = 2
        c.insets = deskInsets
        label = JLabel("получено:", JLabel.RIGHT)
        label.font = deskFont
        controlPanel.add(label, c)
        c.gridx = 1
        c.insets = digitInsets
        lblBonus.font = digitFont
        lblBonus.foreground = Color.RED
        controlPanel.add(lblBonus, c)
        c.gridx = 0
        c.gridy = 3
        c.gridwidth = 2
        val button = JButton("Выход")
        controlPanel.add(button, c)
        button.addActionListener { System.exit(0) }
        layout = BorderLayout()
        add(controlPanel, BorderLayout.NORTH)
        configPanel = this
    }
}