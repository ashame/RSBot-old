package org.nathantehbeast.scripts.guardKiller;

import org.nathantehbeast.api.framework.XScript;
import org.nathantehbeast.api.tools.Calc;
import org.nathantehbeast.api.tools.Logger;
import org.nathantehbeast.api.tools.Skill;
import org.nathantehbeast.api.tools.Utilities;
import org.nathantehbeast.scripts.guardKiller.Nodes.AbilityHandler;
import org.powerbot.core.script.Script;
import org.powerbot.game.api.Manifest;
import org.powerbot.game.api.methods.Game;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.util.SkillData;
import org.powerbot.game.api.util.Time;

import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: Nathan
 * Date: 5/30/13
 * Time: 9:59 PM
 * To change this template use File | Settings | File Templates.
 */


@Manifest(
        authors                 = "nathantehbeast",
        name                    = "GuardKiller",
        description             = "Kills guards in Varrock for profit",
        version                 = 2.1,
        topic                   = 934250,
        website                 = "http://www.powerbot.org/community/topic/934250-free-f2p-guardkiller-abilities-support-good-profit-on-sdn/"

)

public class GuardKiller extends XScript implements Script {

    public static boolean useAbilities = false;
    private static int grapePrice;
    private static int grapesLooted;
    private long startTime;
    public static SkillData sd;
    public static Skill skill;
    private static int startExp;

    @Override
    protected boolean setup() {
        try {
            Utilities.loadFont(Font.TRUETYPE_FONT, "http://dl.dropboxusercontent.com/s/um41rjuakffuu07/RAZER.ttf");
            new GUI();
            getContainer().submit(new AbilityHandler());
            grapePrice = Calc.getPrice(Constants.GRAPE_ID);
            Mouse.setSpeed(Mouse.Speed.VERY_FAST);
            delay = 50;
            startTime = System.currentTimeMillis();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void paint(Graphics g) {
        int profit = grapesLooted * grapePrice;
        long runTime = System.currentTimeMillis() - startTime;
        int grapesHour = (int) ((3600000.0 / runTime) * grapesLooted);
        int profitHour = (int) ((3600000.0 / runTime) * profit);
        Graphics2D g2d = (Graphics2D) g;

        g2d.drawString("Current Node: "+currentNode, 5, 100);
        g2d.drawString("Runtime: "+ Time.format(runTime), 5, 115);
        g2d.drawString("Grapes Looted: "+grapesLooted+" ("+grapesHour+"/h)", 5, 130);
        g2d.drawString("Profit: "+profit+" ("+profitHour+"/h)", 5, 145);
        if (skill != null && sd != null) {
            g2d.drawString(skill.getName()+" XP Gained: "+sd.experience(skill.getId())+" ("+sd.experience(SkillData.Rate.HOUR, skill.getId())+"/h)", 5, 160);
        }
    }

    @Override
    public void poll() {
        if (Game.isLoggedIn() && startExp == 0) {
            startExp = Skill.getTotalExperience();
        }
    }

    @Override
    public void exit() {
    }

    public static void addGrape() {
        grapesLooted+= Inventory.getCount(Constants.GRAPE_ID);
        Logger.log("Total grapes looted: " + grapesLooted);
    }
}
