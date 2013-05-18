package org.nathantehbeast.scripts.aiominer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static org.nathantehbeast.scripts.aiominer.Main.*;

/**
 * Created with IntelliJ IDEA.
 * User: Nathan
 * Date: 5/11/13
 * Time: 4:54 AM
 * To change this template use File | Settings | File Templates.
 */
public final class GUI {

    private static JFrame frame;
    public JButton startButton;
    public JComboBox ore;
    public JCheckBox powermine;
    public JCheckBox bank;

    public GUI() {
        frame = new JFrame("Nathan's AIO Miner");
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        startButton = new JButton("Start");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                start(ae);
            }
        });

        ore = new JComboBox(new DefaultComboBoxModel(Constants.Ore.values()));

        powermine = new JCheckBox("Powermine");
        powermine.setToolTipText("Always keep this selected. Script only supports powermining ATM");
        powermine.setSelected(true);
        powermine.setEnabled(false);

        bank = new JCheckBox("Bank");
        bank.setToolTipText("To be implemented later");
        bank.setSelected(false);
        bank.setEnabled(false);

        JPanel base = new JPanel();
        base.setLayout(new BorderLayout());
        base.setBorder(new TitledBorder("Ore to Mine"));

        JLabel title = new JLabel("Nathan's AIO Miner");
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setFont(new Font("Calibri", Font.PLAIN, 13));
        title.setBorder(new EmptyBorder(2, 15, 0, 15));

        JPanel ores = new JPanel();
        ores.setLayout(new BoxLayout(ores, BoxLayout.Y_AXIS));
        ores.add(ore);

        JPanel options = new JPanel();
        options.setLayout(new BoxLayout(options, BoxLayout.Y_AXIS));
        options.add(powermine);
        options.add(Box.createVerticalGlue());
        options.add(bank);
        options.add(Box.createVerticalGlue());


        base.add(ores, BorderLayout.NORTH);
        base.add(options, BorderLayout.CENTER);
        base.add(startButton, BorderLayout.SOUTH);

        frame.setSize(200, 350);
        frame.setResizable(false);
        frame.setLocationRelativeTo(frame.getOwner());
        frame.getContentPane().add(title, BorderLayout.NORTH);
        frame.getContentPane().add(base);
        frame.pack();
        frame.setVisible(true);
    }

    private void start(ActionEvent ae) {
        frame.setVisible(false);
        setOre((Constants.Ore) ore.getSelectedItem());
        setPowermine(powermine.isSelected());
        start = true;
    }
}
