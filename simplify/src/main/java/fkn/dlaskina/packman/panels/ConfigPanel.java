package fkn.dlaskina.packman.panels;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
    private JLabel lblBonusMax = new JLabel("null", JLabel.LEFT);

    /**
     * Конструктор.
    final String[] fontNames = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
    LOG.info(fontNames);
     */
    public ConfigPanel() {
        super();

        final JPanel controlPanel = new JPanel(new GridBagLayout());
        final GridBagConstraints c = new GridBagConstraints(0, 0, 1, 1, 0, 0,
            CENTER, BOTH, new Insets(2, 10, 2, 10), 0, 0);

        final Insets mainInsets = new Insets(2, 10, 2, 10);
        final Insets deskInsets = new Insets(2, 10, 2, 2);
        final Insets digitInsets = new Insets(2, 5, 2, 10);
        final Font mainFont = new Font("Arial", Font.BOLD, 14);
        final Font deskFont = new Font("Arial", Font.PLAIN, 12);
        final Font digitFont = new Font("Courier New", Font.BOLD, 12);

        c.gridwidth = 2;
        c.insets = mainInsets;
        JLabel label = new JLabel("Призы:", JLabel.CENTER);
        label.setFont(mainFont);
        label.setForeground(Color.BLUE);
        controlPanel.add(label, c);

        c.gridy = 1;
        c.gridx = 0;
        c.gridwidth = 1;
        c.insets = deskInsets;
        label = new JLabel("всего:", JLabel.RIGHT);
        label.setFont(deskFont);
        controlPanel.add(label, c);

        c.gridx = 1;
        c.insets = digitInsets;
        lblBonusMax.setFont(digitFont);
        lblBonusMax.setForeground(Color.RED);
        controlPanel.add(lblBonusMax, c);

        c.gridx = 0;
        c.gridy = 2;
        c.insets = deskInsets;
        label = new JLabel("получено:", JLabel.RIGHT);
        label.setFont(deskFont);
        controlPanel.add(label, c);

        c.gridx = 1;
        c.insets = digitInsets;
        lblBonus.setFont(digitFont);
        lblBonus.setForeground(Color.RED);
        controlPanel.add(lblBonus, c);

        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 2;
        final JButton button = new JButton("Выход");
        controlPanel.add(button, c);
        button.addActionListener(
            new ActionListener() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    System.exit(0);
                }
            }
        );

        setLayout(new BorderLayout());
        add(controlPanel, BorderLayout.NORTH);
        singleton = this;
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
