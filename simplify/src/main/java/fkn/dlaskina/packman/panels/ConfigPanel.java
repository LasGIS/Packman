package fkn.dlaskina.packman.panels;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.DefaultCaret;
import java.awt.BorderLayout;
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

    private static final Integer[] GEARS = {1, 2, 3, 4, 5};
    /** ссылка на MainFrame. */
    private MainFrame mainFrame = null;
    /** дерево конфигурации. */
    private final JPanel controlPanel = new JPanel(new BorderLayout());
    /** панель для информации об ячейках. */
    private final JTextArea arealInfo = new JTextArea();
    /** поле для ввода команды. */
    private final JTextField commandInput = new JTextField();
    /** поле для ввода расстояния. */
    private final JTextField distanceInput = new JTextField();
    /** поле для ввода угла поворота. */
    private final JTextField angleInput = new JTextField();
    /** поле для ввода передачи. */
    private final JComboBox<Integer> gearComboBox = new JComboBox<>(GEARS);

    /** Обработка события при смене текстовой команды (нажали ENTER в input). */
    private final ActionListener enterOnInputAction = event -> {
        LOG.debug(event.getActionCommand());
        commandInput.setText("");
    };

    public void setMainFrame(final MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    enum CommandActionType {AsIs, Move, Turn};

    /** Обработка события нажатия кнопочки. */
    class CommandActionListener implements ActionListener {

        private final String command;
        private final CommandActionType type;

        public CommandActionListener(final String command, final CommandActionType type) {
            this.command = command;
            this.type = type;
        }

        @Override
        public void actionPerformed(final ActionEvent event) {
            final String oldCom = commandInput.getText();
            String outText = oldCom + command + "30" + ";";
            commandInput.setText(outText);
            LOG.debug(outText);
        }
    };

    /**
     * Конструктор.
     */
    public ConfigPanel() {
        super();

        controlPanel.setBackground(MapPanel.PANEL_GRAY_COLOR);
        fillParametersPanel();

        /** панель для получении информации от робота. */
        arealInfo.setFont(new Font("Arial", Font.PLAIN, 12));
        final JScrollPane plantInfoScroll = new JScrollPane(arealInfo);
        plantInfoScroll.setViewportView(arealInfo);
        ((DefaultCaret) arealInfo.getCaret()).setUpdatePolicy(DefaultCaret.OUT_BOTTOM);

        /* разделительная панелька */
        final JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setContinuousLayout(true);
        splitPane.add(controlPanel, JSplitPane.TOP);
        splitPane.add(plantInfoScroll, JSplitPane.BOTTOM);
        splitPane.setResizeWeight(0.0);

        setLayout(new BorderLayout());
        /* панель режима. */
        add(splitPane, BorderLayout.CENTER);
    }

    /** создание доп атрибутов. */
    private void fillParametersPanel() {
        distanceInput.setText("30");
        angleInput.setText("90");
        gearComboBox.setSelectedItem(4);
        final JPanel parametersPanel = new JPanel(new GridBagLayout());
        final GridBagConstraints labelGbc = new GridBagConstraints(0, 0, 1, 1, 0, 0,
            CENTER, BOTH, new Insets(2, 5, 2, 4), 0, 0);
        final GridBagConstraints inputGbc = new GridBagConstraints(1, 0, 1, 1, 0, 0,
            CENTER, BOTH, new Insets(2, 0, 2, 0), 0, 0);
        final GridBagConstraints dimensionGbc = new GridBagConstraints(2, 0, 1, 1, 0, 0,
            CENTER, BOTH, new Insets(2, 4, 2, 5), 0, 0);

        parametersPanel.add(new JLabel("Дистанция", JLabel.RIGHT), labelGbc);
        parametersPanel.add(distanceInput, inputGbc);
        parametersPanel.add(new JLabel("[см]", JLabel.LEFT), dimensionGbc);

        labelGbc.gridy++;
        inputGbc.gridy++;
        dimensionGbc.gridy++;
        parametersPanel.add(new JLabel("Угол", JLabel.RIGHT), labelGbc);
        parametersPanel.add(angleInput, inputGbc);
        parametersPanel.add(new JLabel("[град]", JLabel.LEFT), dimensionGbc);

        labelGbc.gridy++;
        inputGbc.gridy++;
        dimensionGbc.gridy++;
        parametersPanel.add(new JLabel("Передача", JLabel.RIGHT), labelGbc);
        parametersPanel.add(gearComboBox, inputGbc);
        parametersPanel.add(new JLabel("gear", JLabel.LEFT), dimensionGbc);

        controlPanel.add(parametersPanel, BorderLayout.WEST);
    }

    public JTextArea getArealInfo() {
        return arealInfo;
    }
}
