package org.nathantehbeast.scripts.braceletCrafter;

import org.nathantehbeast.api.framework.XScript;
import org.nathantehbeast.api.tools.Calc;
import org.nathantehbeast.api.tools.Utilities;
import org.nathantehbeast.scripts.braceletCrafter.Nodes.Banking;
import org.nathantehbeast.scripts.braceletCrafter.Nodes.Crafting;
import org.nathantehbeast.scripts.braceletCrafter.Nodes.WalkBank;
import org.nathantehbeast.scripts.braceletCrafter.Nodes.WalkFurnace;
import org.powerbot.core.script.Script;
import org.powerbot.core.script.methods.Game;
import org.powerbot.game.api.Manifest;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.methods.tab.Skills;
import org.powerbot.game.api.util.SkillData;
import org.powerbot.game.api.util.Timer;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Nathan
 * Date: 5/26/13
 * Time: 3:53 PM
 * To change this template use File | Settings | File Templates.
 */

@Manifest(
        authors             = "NathanTehBeast",
        name                = "BraceletCrafter",
        description         = "Crafts Gold Bars into Bracelets at Edgeville.",
        version             = 1.3,
        topic               = 948733,
        instances           = 10,
        website             = "http://www.powerbot.org/community/topic/948733-braceletcrafter/"
)
public class Main extends XScript implements Script, MouseListener {

    private HashMap<Integer, Integer> priceMap;

    private long startTime;
    private long ssTimer;

    private SkillData sd;

    private static int crafted;

    private volatile boolean showPaint = true;
    private Timer ss = new Timer(System.currentTimeMillis());

    private final RenderingHints antialiasing = new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    private final Color color1 = new Color(255, 255, 255);
    private final Color color2 = new Color(0, 0, 0);
    private final BasicStroke stroke1 = new BasicStroke(1);
    private final Font font1 = new Font("Arial", 1, 15);
    private final Font font2 = new Font("Arial", 0, 11);
    private final Font font3 = new Font("Arial", 0, 10);

    @Override
    protected boolean setup() {
        try {
            while (Game.getClientState() != Game.INDEX_MAP_LOADED) {
                sleep(600);
            }
            priceMap = Calc.getPrice(Constants.GOLD_ID, Constants.BRACELET_ID);
            Mouse.setSpeed(Mouse.Speed.VERY_FAST);
            startTime = System.currentTimeMillis();
            sd = new SkillData();
            provide(new Banking(Constants.GOLD_ID, Constants.BRACELET_ID, Constants.BANK_AREA));
            provide(new Crafting(Constants.FURNACE_AREA, Constants.GOLD_ID, Constants.FURNACE_ID));
            provide(new WalkBank(Constants.GOLD_ID, Constants.BANK_AREA));
            provide(new WalkFurnace(Constants.GOLD_ID, Constants.FURNACE_AREA));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void poll() {
        if (Walking.getEnergy() >= 50 && !Walking.isRunEnabled()) {
            Walking.setRun(true);
        }
        if (((ss.getElapsed() / 1000) - ssTimer) > 3600) {
            Utilities.savePaint(0, 0, 765, 48);
            ssTimer = System.currentTimeMillis();
        }
    }

    @Override
    public void exit() {
        Utilities.savePaint(0, 0, 765, 50);
        System.out.println("Thank you for using Nathan's Bracelet Crafter!");
    }

    @Override
    public void paint(Graphics g1) {
        if (currentNode != null) {
            Graphics2D g2d = (Graphics2D) g1;
            g2d.drawString("Current node: " + currentNode, 5, 100);
        }
        if (Game.isLoggedIn() && showPaint && sd != null) {
            int expGained = sd.experience(Skills.CRAFTING);
            int levelsGained = sd.level(Skills.CRAFTING);
            int expTNL = Skills.getExperienceRequired(Skills.getRealLevel(Skills.CRAFTING) + 1) - Skills.getExperience(Skills.CRAFTING);
            int profit = crafted * (priceMap.get(Constants.BRACELET_ID) - priceMap.get(Constants.GOLD_ID));

            long runTime = System.currentTimeMillis() - startTime;
            long timeTNL = (long) ((double) expTNL / (double) ((int) ((3600000.0 / runTime) * expGained)) * 3600000);
            int profitHour = (int) ((3600000.0 / runTime) * profit);
            int craftHour = (int) ((3600000.0 / runTime) * crafted);

            Graphics2D g = (Graphics2D) g1;
            g.setRenderingHints(antialiasing);

            g.setColor(color1);
            g.fillRect(0, 0, 765, 50);
            g.setColor(color2);
            g.setStroke(stroke1);
            g.drawRect(0, 0, 765, 50);
            g.setFont(font1);
            g.drawString("BraceletCrafter", 5, 15);
            g.setFont(font2);
            if (levelsGained > 0) {
                g.drawString("Crafting Level:" + Skills.getRealLevel(Skills.CRAFTING) + " (+" + levelsGained + ")", 5, 30);
            } else {
                g.drawString("Crafting Level: " + Skills.getRealLevel(Skills.CRAFTING), 5, 30);
            }
            g.drawString("EXP Gained: " + expGained + " (" + sd.experience(SkillData.Rate.HOUR, Skills.CRAFTING) + "/hr)", 5, 45);
            g.drawString("Bracelets Banked: " + crafted + " (" + craftHour + "/hr)", 219, 15);
            g.drawString("Profit Earned: " + profit + " (" + profitHour + "/hr)", 219, 30);
            g.drawString("EXP TNL: " + expTNL, 219, 45);
            g.setFont(font3);
            g.drawString("by NathanTehBeast", 666, 45);
            g.drawString("Version: " + Main.class.getAnnotation(Manifest.class).version(), 666, 15);
            g.setFont(font2);
            g.drawString("Time Ran: " + Calc.formatTime(runTime), 429, 15);
            g.drawString("Time TNL: " + Calc.formatTime(timeTNL), 429, 45);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Rectangle paint = new Rectangle(0, 0, 765, 50);
        if (paint.contains(e.getPoint())) {
            showPaint = !showPaint;
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public static void addCrafted(int i) {
        crafted += i;
    }
}
