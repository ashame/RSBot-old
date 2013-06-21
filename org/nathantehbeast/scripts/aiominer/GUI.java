package org.nathantehbeast.scripts.aiominer;

import org.nathantehbeast.api.tools.Logger;
import org.nathantehbeast.api.tools.Utilities;
import org.nathantehbeast.scripts.aiominer.Constants.Ore;
import org.nathantehbeast.scripts.aiominer.Constants.BankLocations;
import org.nathantehbeast.scripts.aiominer.nodes.*;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.bot.Context;

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
    public JComboBox banks;
    public DefaultComboBoxModel<Ore> model;
    public JCheckBox powermine;
    public JCheckBox bank;
    public JCheckBox mouse;
    public JCheckBox urns;
    public JSpinner world;
    public JSpinner radius;
    public JLabel rLabel;
    public DefaultComboBoxModel<BankLocations> bankModel;

    public GUI() {
        Utilities.loadFont(Font.TRUETYPE_FONT, "http://dl.dropboxusercontent.com/s/sz0p52rlowgwrid/Jokerman-Regular.ttf");
        try {UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}
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
                final Ore o = (Ore) ore.getSelectedItem();
                if (o != null) {
                    Main.setOre(o);
                    if (o.getBanks() != null && o.getBanks().length > 0) {
                        bankModel.removeAllElements();
                        for (BankLocations b : o.getBanks()) {
                            bankModel.addElement(b);
                        }
                        banks = new JComboBox<>(bankModel);
                    }
                    if (o.getBanks() == null) {
                        bankModel.removeAllElements();
                        bankModel.addElement(BankLocations.UNSUPPORTED);
                        banks = new JComboBox<>(bankModel);
                    }
                }
            }
        });

        bankModel = new DefaultComboBoxModel<>();
        if (Ore.COPPER.getBanks() != null) {
            bankModel.removeAllElements();
            if (Ore.COPPER.getBanks().length < 1) {
                bankModel.addElement(BankLocations.UNSUPPORTED);
                return;
            }
            for (BankLocations b : Ore.COPPER.getBanks()) {
                bankModel.addElement(b);
            }
        }

        banks = new JComboBox<>(bankModel);


        powermine = new JCheckBox("Powermine");
        powermine.setToolTipText("Always keep this selected. Script only supports powermining ATM");
        powermine.setSelected(true);
        powermine.setEnabled(false);

        bank = new JCheckBox("Bank");
        bank.setToolTipText("Currently only supports iron at Yanille.");
        bank.setSelected(false);
        bank.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (bank.isSelected()) {
                    powermine.setEnabled(false);
                    powermine.setSelected(false);
                    //urns.setSelected(true);
                    //urns.setEnabled(true);
                } else {
                    powermine.setEnabled(true);
                    powermine.setSelected(true);
                    //urns.setSelected(false);
                    //urns.setEnabled(false);
                }
            }
        });

        urns = new JCheckBox("Use Urns");
        urns.setSelected(false);
        urns.setEnabled(false);

        mouse = new JCheckBox("Show Mouse");
        mouse.setToolTipText("Shows a simple crosshair for mouse location");
        mouse.setSelected(true);

        world = new JSpinner();
        world.setModel(new SpinnerNumberModel(0, 0, 139, 1));

        radius = new JSpinner();
        radius.setModel(new SpinnerNumberModel(1, 0, 30, 1));
        radius.setToolTipText("Defines the mining radius. If banking, leave this at 0 to use the default mining area, and anything else to specify your own radius.");
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


        JPanel bankx = new JPanel();
        bankx.setLayout(new BorderLayout());
        bankx.setBorder(new TitledBorder("Bank Location"));
        bankx.add(banks);

        JPanel ores2 = new JPanel();
        ores2.setLayout(new BoxLayout(ores2, BoxLayout.Y_AXIS));
        ores2.add(ore);

        JPanel orebase = new JPanel();
        orebase.setLayout(new BorderLayout());
        orebase.setBorder(new TitledBorder("Ore"));
        orebase.add(ores2, BorderLayout.NORTH);

        JPanel ores = new JPanel();
        ores.setLayout(new BorderLayout());
        ores.add(orebase, BorderLayout.NORTH);
        ores.add(bankx, BorderLayout.SOUTH);

        JPanel options = new JPanel();
        options.setLayout(new BoxLayout(options, BoxLayout.Y_AXIS));
        options.add(mouse);
        options.add(Box.createVerticalGlue());
        options.add(powermine);
        options.add(Box.createVerticalGlue());
        options.add(bank);
        options.add(Box.createVerticalGlue());
        options.add(urns);
        options.add(Box.createVerticalGlue());

        JPanel options2 = new JPanel();
        options2.setLayout(new BorderLayout());
        options2.setBorder(new TitledBorder("Relog World"));
        options2.add(world);

        base1.add(ores, BorderLayout.NORTH);
        base2.add(options, BorderLayout.NORTH);
        base2.add(options2, BorderLayout.SOUTH);
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
        if ((int) world.getValue() > 0) {
            setVisible(false);
            Main.setOre((Ore) ore.getSelectedItem());
            Logger.log("Mining: " + ore.getSelectedItem());
            Main.paintMouse = mouse.isSelected();
            Main.setStartTile();
            Main.startTimer();
            Main.setRadius((int) radius.getValue());
            Logger.log("Radius set to " + radius.getValue());
            Main.setSkillData();
            Main.provide(new Mine((Ore) ore.getSelectedItem(), Players.getLocal().getLocation(), (int) radius.getValue(), bank.isSelected() ? (BankLocations) banks.getSelectedItem() : null));
            if (powermine.isSelected()) {
                Main.provide(new Drop(powermine.isSelected(), (Ore) ore.getSelectedItem()));
            }
            if (bank.isSelected()) {
                final BankLocations bankLocation = (BankLocations) banks.getSelectedItem();
                Main.provide(new Banking(bankLocation.getBankArea(), urns.isSelected()));
                Main.provide(new WalkToBank(bankLocation.getBankPath(), bankLocation.getBankArea()));
                Main.provide(new WalkToOres(bankLocation.getRockPath(), bankLocation.getRockArea()));
                Main.setBankLocation(bankLocation);
            }
            Main.provide(new EscapeCombat(Players.getLocal().getLocation()));
            Context.setLoginWorld((int) world.getValue());
            Logger.log("Relogging on world: " + world.getValue());
        } else {
            JOptionPane.showConfirmDialog(this, "Please select a world to relog on", "World Selection", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
        }
    }
}
