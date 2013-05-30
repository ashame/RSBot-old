package org.nathantehbeast.scripts.aiominer;

import org.nathantehbeast.api.tools.Logger;
import org.nathantehbeast.scripts.aiominer.Constants.Ore;
import org.nathantehbeast.scripts.aiominer.nodes.AintNobodyGotTimeFoDat;
import org.nathantehbeast.scripts.aiominer.nodes.Drop;
import org.nathantehbeast.scripts.aiominer.nodes.Mine;
import org.powerbot.game.api.methods.interactive.Players;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Created with IntelliJ IDEA.
 * User: Nathan
 * Date: 5/11/13
 * Time: 4:54 AM
 * To change this template use File | Settings | File Templates.
 */
public class GUI extends JFrame {

    public JButton startButton;
    public JComboBox ore;
    public DefaultComboBoxModel<Ore> model;
    public JCheckBox powermine;
    public JCheckBox bank;
    public JCheckBox mouse;
    public JSpinner radius;
    public JLabel rLabel;

    public GUI() {
        setTitle("Nathan's AIO Miner");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        startButton = new JButton("Start");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                start(ae);
            }
        });

        model = new DefaultComboBoxModel<>();
        for (Ore o : Ore.values()) {
            model.addElement(o);
        }

        ore = new JComboBox<>(model);
        ore.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                Main.setOre((Constants.Ore) ore.getSelectedItem());
            }
        });

        powermine = new JCheckBox("Powermine");
        powermine.setToolTipText("Always keep this selected. Script only supports powermining ATM");
        powermine.setSelected(true);
        powermine.setEnabled(false);

        bank = new JCheckBox("Bank");
        bank.setToolTipText("To be implemented later");
        bank.setSelected(false);
        bank.setEnabled(false);

        mouse = new JCheckBox("Show Mouse");
        mouse.setToolTipText("Shows a simple crosshair for mouse location");
        mouse.setSelected(true);

        radius = new JSpinner();
        radius.setModel(new SpinnerNumberModel(1, 1, 30, 1));
        radius.add(Box.createVerticalBox());
        radius.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                Main.setRadius((int) radius.getValue());
            }
        });

        rLabel = new JLabel("Radius:");

        JPanel base = new JPanel();
        base.setLayout(new BorderLayout());

        JPanel base1 = new JPanel();
        base1.setLayout(new BorderLayout());
        base1.setBorder(new TitledBorder("Ore"));

        JPanel base2 = new JPanel();
        base2.setLayout(new BorderLayout());
        base2.setBorder(new TitledBorder("Options"));

        JPanel base3 = new JPanel();
        base3.setLayout(new BorderLayout());
        base3.setBorder(new TitledBorder("Radius"));

        JLabel title = new JLabel("Nathan's AIO Miner");
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setFont(new Font("Jokerman", Font.PLAIN, 13));
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
        options.add(mouse);
        options.add(Box.createVerticalGlue());

        base1.add(ores, BorderLayout.NORTH);
        base2.add(options, BorderLayout.NORTH);
        base3.add(radius, BorderLayout.NORTH);

        base.add(base1, BorderLayout.NORTH);
        base.add(base2, BorderLayout.CENTER);
        base.add(base3, BorderLayout.SOUTH);

        setResizable(false);
        setLocationRelativeTo(getOwner());
        getContentPane().add(title, BorderLayout.NORTH);
        getContentPane().add(base, BorderLayout.CENTER);
        getContentPane().add(startButton, BorderLayout.SOUTH);
        pack();
        setVisible(true);
    }

    private void start(ActionEvent ae) {
        setVisible(false);
        Main.setOre((Ore) ore.getSelectedItem());
        Logger.log("Mining: " + ore.getSelectedItem());
        Main.paintMouse = mouse.isSelected();
        Main.setStartTile();
        Main.startTimer();
        Main.setRadius((int) radius.getValue());
        Logger.log("Radius set to "+radius.getValue());
        Main.setSkillData();
        Main.provide(new Mine((Ore) ore.getSelectedItem(), Players.getLocal().getLocation(), (int) radius.getValue()));
        if (powermine.isSelected()) {
            Main.provide(new Drop(powermine.isSelected(), (Ore) ore.getSelectedItem()));
        }
        Main.provide(new AintNobodyGotTimeFoDat(Players.getLocal().getLocation()));
    }
}
