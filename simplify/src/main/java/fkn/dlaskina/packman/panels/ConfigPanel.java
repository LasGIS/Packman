package fkn.dlaskina.packman.panels;

import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import static java.awt.GridBagConstraints.BOTH;
import static java.awt.GridBagConstraints.CENTER;

/**
 * Панель конфигурации.
 * @author VLaskin
 * @version 1.0 Date: 13.01.2005 16:38:05
 */
public class ConfigPanel extends JPanel {

    private static final Logger LOG = LogManager.getLogger(ConfigPanel.class);
    private static ConfigPanel singleton;

    /** число полученых подарков. */
    private static int bonusCount = 0;
    /** общее число подарков. */
    private static int bonusCountMax;
    /** всего бонусов */
    private JLabel lblBonus = new JLabel("0", JLabel.LEFT);
    /** собранных бонусов */
    private JLabel lblBonusMax = new JLabel("20", JLabel.LEFT);

    /**
     * Конструктор.
     */
    public ConfigPanel() {
        super();

        final JPanel controlPanel = new JPanel(new GridBagLayout());
        controlPanel.setBackground(MapPanel.PANEL_GRAY_COLOR);
        final GridBagConstraints c = new GridBagConstraints(0, 0, 1, 1, 0, 0,
            CENTER, BOTH, new Insets(2, 5, 2, 4), 0, 0);

        controlPanel.add(new JLabel("Призы", JLabel.CENTER), c);
        c.gridy = 1;
        controlPanel.add(new JLabel("всего:", JLabel.RIGHT), c);
        c.gridx = 1;
        controlPanel.add(lblBonusMax, c);
        c.gridx = 0;
        c.gridy = 2;
        controlPanel.add(new JLabel("получено", JLabel.RIGHT), c);
        c.gridx = 1;
        controlPanel.add(lblBonus, c);

        setLayout(new BorderLayout());
        add(controlPanel, BorderLayout.CENTER);
        singleton = this;
    }

    public static ConfigPanel getConfigPanel() {
        return singleton;
    }

    /**
     * Добавить подарок и проверить - не последний ли?
     * @return true если это последний бонус
     */
    public static boolean addBonus() {
        bonusCount++;
        if (singleton != null) {
            singleton.lblBonus.setText(String.valueOf(bonusCount));
        }
        return bonusCountMax <= bonusCount;
    }

    public static void setBonusCountMax(final int maxCount) {
        bonusCount = 0;
        bonusCountMax = maxCount;
        if (singleton != null) {
            singleton.lblBonusMax.setText(String.valueOf(bonusCountMax));
        }
    }
}
