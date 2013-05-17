package org.nathantehbeast.scripts.guardKiller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static org.nathantehbeast.scripts.guardKiller.GuardKiller.*;

public class GUI extends JFrame {

    private JLabel skillLabel, foodLabel;
    private JButton startButton;
    private JComboBox skills;
    private JTextField food;
    private JCheckBox abilities;
    private String[] skillList = {"Attack", "Defense", "Strength", "Constitution", "Ranged", "Magic"};

    private void addItem(final JFrame base, final Component component, final int x, final int y, final int w, final int h) {
        base.add(component);
        component.setBounds(x, y, w, h);
    }

    public GUI() {
        skillLabel = new JLabel();
        foodLabel = new JLabel();
        startButton = new JButton();
        skills = new JComboBox();
        food = new JTextField();
        abilities = new JCheckBox();

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLayout(null);
        setSize(235, 200);

        skillLabel.setText("Skill to track:");
        addItem(this, skillLabel, 15, 25, 85, 25);

        foodLabel.setText("Food ID:");
        addItem(this, foodLabel, 15, 50, 85, 25);

        startButton.setText("Start");
        startButton.setToolTipText("Start the script.");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                scriptStarted(ae);
            }
        });
        addItem(this, startButton, 70, 120, 65, 30);

        skills.setModel(new DefaultComboBoxModel(skillList));
        skills.setToolTipText("The skill that you want to track EXP for in the paint.");
        addItem(this, skills, 130, 25, 70, 25);

        abilities.setText("Use Abilities");
        abilities.setToolTipText("Check to use abilities, leave unchecked to rely on regular attacks (momentum recommended)");
        addItem(this, abilities, 15, 75, 85, 25);

        food.setText("0");
        food.setToolTipText("Enter food ID, use 0 for no food.");
        addItem(this, food, 130, 50, 70, 25);
        
        setResizable(false);
        setLocationRelativeTo(getOwner());
        setVisible(true);
    }

    private void scriptStarted(ActionEvent ae) {
        skillId = skills.getSelectedIndex();
        foodId = Integer.parseInt(food.getText());
        useAbilities = abilities.isSelected();
        start = true;
        setVisible(false);
    }
}