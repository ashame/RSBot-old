package org.nathantehbeast.scripts.guardKiller;

import org.nathantehbeast.api.tools.Logger;
import org.nathantehbeast.api.tools.Skill;
import org.nathantehbeast.scripts.guardKiller.Nodes.*;
import org.powerbot.game.api.util.SkillData;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created with IntelliJ IDEA.
 * User: Nathan
 * Date: 5/30/13
 * Time: 9:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class GUI extends JFrame {

    public JPanel base;
    public JPanel options, optionBase;
    public JCheckBox abilities;
    public JSpinner food;
    public JButton startButton;
    public DefaultComboBoxModel<Skill> model;
    public JComboBox skill;
    public JLabel title;

    public GUI() {
        setTitle("GuardKiller");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}
        setBackground(Color.BLACK);

        title = new JLabel("GuardKiller");
        title.setFont(new Font("Razer Regular", Font.PLAIN, 16));
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setBorder(new EmptyBorder(0, 0, 5, 0));
        title.setBackground(Color.BLACK);

        startButton = new JButton("Start");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                start();
            }
        });
        startButton.setFont(new Font("Razer Regular", Font.PLAIN, 12));
        startButton.setToolTipText("Click me to start");

        model = new DefaultComboBoxModel<>();
        for (Skill s : Skill.getFightingSkills()) {
            model.addElement(s);
        }
        skill = new JComboBox<>(model);
        skill.setFont(new Font("Razer Regular", Font.PLAIN, 11));
        skill.setToolTipText("The skill that you wish to track");

        abilities = new JCheckBox("Use Abilities");
        abilities.setToolTipText("Tick this to use abilities, leave uncheced to not.");
        abilities.setFont(new Font("Razer Regular", Font.ITALIC, 11));
        abilities.setSelected(true);

        food = new JSpinner();
        food.setModel(new SpinnerNumberModel(0, 0, 99999, 1));
        food.setToolTipText("The ID of the food to use; Leave it at 0 to not use food");
        food.setFont(new Font("Razer Regular", Font.PLAIN, 12));

        options = new JPanel();
        options.setLayout(new BoxLayout(options, BoxLayout.Y_AXIS));
        options.add(abilities);
        options.add(Box.createVerticalGlue());

        JPanel skillBase = new JPanel();
        skillBase.setLayout(new BorderLayout());
        TitledBorder skillTitle = new TitledBorder("Skill");
        skillTitle.setTitleFont(new Font("Razer Regular", Font.ITALIC, 11));
        skillBase.setBorder(skillTitle);
        skillBase.add(skill);

        optionBase = new JPanel();
        optionBase.setLayout(new BorderLayout());
        TitledBorder optionBorder = new TitledBorder("Options");
        optionBorder.setTitleFont(new Font("Razer Regular", Font.ITALIC, 11));
        optionBase.setBorder(optionBorder);

        JPanel foodBase = new JPanel();
        foodBase.setLayout(new BorderLayout());
        TitledBorder foodBorder = new TitledBorder("Food ID");
        foodBorder.setTitleFont(new Font("Razer Regular", Font.ITALIC, 11));
        foodBase.setBorder(foodBorder);
        foodBase.add(food);

        optionBase.add(skillBase, BorderLayout.NORTH);
        optionBase.add(foodBase, BorderLayout.CENTER);
        optionBase.add(options, BorderLayout.SOUTH);

        base = new JPanel();
        base.setLayout(new BorderLayout());
        base.add(title, BorderLayout.NORTH);
        base.add(Box.createVerticalGlue());
        base.add(optionBase, BorderLayout.CENTER);
        base.add(startButton, BorderLayout.SOUTH);
        getContentPane().add(base, BorderLayout.CENTER);
        setResizable(false);
        setLocationRelativeTo(getWindows()[0]);
        setVisible(true);
        pack();
    }

    public void start() {
        try {
            GuardKiller.provide(new FightGuards(Constants.CASTLE_AREA, Constants.GUARDS, (int) food.getValue()));
            GuardKiller.provide(new BankItems((int) food.getValue(), Constants.BANK_AREA, Constants.BANK_FILTER));
            GuardKiller.provide(new TraverseBank(Constants.BANK_PATH, Constants.BANK_AREA, (int) food.getValue()));
            GuardKiller.provide(new TraverseCastle(Constants.CASTLE_AREA, Constants.CASTLE_PATH));
            GuardKiller.provide(new Looting(Constants.GRAPE_ID, (int) food.getValue(), Constants.CASTLE_AREA));
            if (abilities.isSelected())
                GuardKiller.useAbilities = true;
            if ((int) food.getValue() > 0) {
                GuardKiller.provide(new EatFood((int) food.getValue()));
            }
            GuardKiller.sd = new SkillData();
            GuardKiller.skill = (Skill) skill.getSelectedItem();
            Logger.log("Tracking Skill: "+skill.getSelectedItem());
            setVisible(false);
        } catch (Exception e) {
            Logger.log("Error loading script");
            e.printStackTrace();
        }
    }
}
