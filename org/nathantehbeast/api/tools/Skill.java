package org.nathantehbeast.api.tools;

import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.tab.Skills;
import org.powerbot.game.api.wrappers.widget.WidgetChild;

/**
 * Created with IntelliJ IDEA.
 * User: Nathan
 * Date: 5/27/13
 * Time: 1:51 AM
 * To change this template use File | Settings | File Templates.
 */

public enum Skill {

    ATTACK          (0, "Attack", 1),
    DEFENSE         (1, "Defense", 18),
    STRENGTH        (2, "Strength", 4),
    CONSTITUTION    (3, "Constitution", 2),
    RANGE           (4, "Range", 35),
    PRAYER          (5, "Prayer", 53),
    MAGIC           (6, "Magic", 66),
    COOKING         (7, "Cooking", 47),
    WOODCUTTING     (8, "Woodcutting",78),
    FLETCHING       (9, "Fletching", 72),
    FISHING         (10, "Fishing", 29),
    FIREMAKING      (11, "Firemaking", 65),
    CRAFTING        (12, "Crafting", 59),
    SMITHING        (13, "Smithing", 16),
    MINING          (14, "Mining", 3),
    HERBLORE        (15, "Herblore", 23),
    AGILITY         (16, "Agility", 10),
    THIEVING        (17, "Thieving", 41),
    SLAYER          (18, "Slayer", 85),
    FARMING         (19, "Farming", 91),
    RUNECRAFTING    (20, "Runecrafting", 79),
    HUNTER          (21, "Hunter", 103),
    CONSTRUCTION    (22, "Construction", 97),
    SUMMONING       (23, "Summoning", 109),
    DUNGEONEERING   (24, "Dungeoneering", 115),
    TOTAL           (99, "Total", 999);

    private int id;
    private String name;
    private int widgetChild;

    Skill(int id, String name, int widgetChild) {
        this.id = id;
        this.name = name;
        this.widgetChild = widgetChild;
    }

    public static Skill[] getCombatSkills() {
        return new Skill[] {ATTACK, DEFENSE, STRENGTH, CONSTITUTION, RANGE, MAGIC, PRAYER, SUMMONING, TOTAL};
    }

    public static Skill[] getFightingSkills() {
        return new Skill[] {ATTACK, DEFENSE, STRENGTH, CONSTITUTION, RANGE, MAGIC, TOTAL};
    }

    public static Skill[] getGatheringSkills() {
        return new Skill[] {MINING, WOODCUTTING, FISHING, HUNTER, FARMING};
    }

    public static Skill[] getSupportSkills() {
        return new Skill[] {DUNGEONEERING, THIEVING, AGILITY, SLAYER};
    }

    public static Skill[] getArtisanSkills() {
        return new Skill[] {FIREMAKING, HERBLORE, CRAFTING, RUNECRAFTING, CONSTRUCTION, SMITHING, COOKING, FLETCHING};
    }

    public static Skill[] getMemberSkills() {
        return new Skill[] {HERBLORE, SUMMONING, CONSTRUCTION, THIEVING, HUNTER, AGILITY, FLETCHING, SLAYER, FARMING};
    }

    @SuppressWarnings("deprecation")
    public static int getTotalLevel() {
        int temp = 0;
        for (int i : Skills.getLevels()) {
            temp += i;
        }
        return temp;
    }

    public static int getTotalExperience() {
        int temp = 0;
        for (int i : Skills.getExperiences()) {
            temp += i;
        }
        return temp;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public int getLevel() {
        return Skills.getRealLevel(id);
    }

    public int getExperience() {
        return Skills.getExperience(id);
    }

    public int getExperienceRequired() {
        return Skills.getExperienceRequired(Skills.getLevel(id) + 1) - Skills.getExperience(id);
    }

    public WidgetChild getWidgetChild() {
        return Widgets.get(320).getChild(widgetChild);
    }
}
