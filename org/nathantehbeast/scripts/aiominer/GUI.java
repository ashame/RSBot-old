package org.nathantehbeast.scripts.aiominer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static org.nathantehbeast.scripts.aiominer.Main.setOre;
import static org.nathantehbeast.scripts.aiominer.Main.setPowermine;
import static org.nathantehbeast.scripts.aiominer.Main.start;

/**
 * Created with IntelliJ IDEA.
 * User: Nathan
 * Date: 5/11/13
 * Time: 4:54 AM
 * To change this template use File | Settings | File Templates.
 */
public final class GUI extends JFrame {

    private JLabel oreLabel;
    private JButton startButton;
    private JComboBox ore;
    private JCheckBox powermine;

    public GUI() {
        startButton = new JButton();
        oreLabel = new JLabel();
        ore = new JComboBox();
        powermine = new JCheckBox();

        setTitle("Nathan's Miner");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLayout(null);
        setSize(235, 160);

        oreLabel.setText("Ore to mine: ");
        addItem(this, oreLabel, 10, 30, 95, 25);

        ore.setModel(new DefaultComboBoxModel(Constants.Ore.values()));
        addItem(this, ore, 130, 30, 95, 25);

        powermine.setText("Powermine");
        powermine.setToolTipText("Always keep this selected. Script only supports powermining ATM");
        powermine.setSelected(true);
        addItem(this, powermine, 10, 60, 180, 25);

        startButton.setText("Start");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                start(ae);
            }
        });
        addItem(this, startButton, 70, 90, 65, 30);


        setResizable(false);
        setLocationRelativeTo(getOwner());
        setVisible(true);
    }

    private void start(ActionEvent ae) {
        setVisible(false);
        setOre((Constants.Ore) ore.getSelectedItem());
        setPowermine(powermine.isSelected());
        start = true;
    }

    private void addItem(final JFrame base, final Component component, final int x, final int y, final int w, final int h) {
        base.add(component);
        component.setBounds(x, y, w, h);
    }
}
