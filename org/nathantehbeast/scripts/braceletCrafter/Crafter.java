package org.nathantehbeast.scripts.braceletCrafter;

import org.nathantehbeast.api.framework.XScript;
import org.nathantehbeast.api.tools.Calc;
import org.nathantehbeast.api.tools.Utilities;
import org.nathantehbeast.scripts.braceletCrafter.Nodes.Banking;
import org.nathantehbeast.scripts.braceletCrafter.Nodes.Crafting;
import org.nathantehbeast.scripts.braceletCrafter.Nodes.WalkBank;
import org.nathantehbeast.scripts.braceletCrafter.Nodes.WalkFurnace;
import org.powerbot.core.script.Script;
import org.powerbot.core.script.job.LoopTask;
import org.powerbot.core.script.methods.Game;
import org.powerbot.game.api.Manifest;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.methods.tab.Skills;
import org.powerbot.game.api.util.SkillData;
import org.powerbot.game.api.util.Time;
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
        description         = "Crafts Gold Bars into Bracelets at Edgeville. Start in NE corner of Edgeville bank with gold bars either in the bank or inventory.",
        version             = 1.33,
        topic               = 948733,
        instances           = 10,
        website             = "http://www.powerbot.org/community/topic/948733-braceletcrafter/"
)
public class Crafter extends XScript implements Script, MouseListener {

    private HashMap<Integer, Integer> priceMap;

    private long startTime;
    private long ssTimer;

    private SkillData sd;

    private int crafted;
    private int a;

    private volatile boolean showPaint = true;
    private volatile boolean paintMouse = true;
    public static boolean debug = false;
    private Timer ss = new Timer(System.currentTimeMillis());

    private static final RenderingHints antialiasing = new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    private static final Color color1 = new Color(208, 209, 191);
    private static final Font font  = new Font("Calibri", Font.PLAIN, 12);
    private static final Font lithos = new Font("Lithos Pro Regular", 0, 10);
    private static final Font lithos_b = new Font("Lithos Pro Regular", Font.BOLD, 10);
    private static final Font font3 = new Font("Lithos Pro Regular", 1, 9);
    private static final Color black = Color.BLACK;
    private static final BasicStroke stroke = new BasicStroke(2);

    private final Image paint = Utilities.getImage("http://puu.sh/32cKT.jpg");

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
            getContainer().submit(new LoopTask() {
                @Override
                public int loop() {
                    if (sd.experience(Skills.CRAFTING) > a) {
                        crafted++;
                        a = sd.experience(Skills.CRAFTING);
                    }
                    return 100;
                }
            });
            delay = 200;
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
            boolean p = showPaint;
            showPaint = true;
            paintMouse = false;
            sleep(100);
            Utilities.savePaint(0, 0, 765, 48);
            showPaint = p;
            paintMouse = true;
            ssTimer = System.currentTimeMillis();
        }
    }

    @Override
    public void exit() {
        showPaint = true;
        paintMouse = false;
        sleep(100);
        Utilities.savePaint(0, 388, 520, 140);
        System.out.println("Thank you for using Nathan's Bracelet Crafter!");
    }

    @Override
    public void paint(Graphics g1) {
        Graphics2D g = (Graphics2D)g1;

        if (currentNode != null && debug) {
            g.setFont(font);
            g.drawString("Current node: " + currentNode, 5, 85);
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

            g.setRenderingHints(antialiasing);

            g.drawImage(paint, -3, 388, null);
            g.setFont(lithos);
            g.setColor(color1);
            g.drawString(Skills.getRealLevel(Skills.CRAFTING) + " (+" + levelsGained + ")", 180, 444);
            g.drawString(expGained + " (" + sd.experience(SkillData.Rate.HOUR, Skills.CRAFTING) + "/h)", 125, 457);
            g.drawString(crafted + " (" + craftHour + "/h)", 170, 470);
            g.drawString(profit + " (" + profitHour + "/h)", 146, 482);
            g.drawString(Time.format(runTime), 316, 444);
            g.drawString(Time.format(timeTNL), 316, 458);
            g.setFont(lithos_b);
            g.drawString("v"+Crafter.class.getAnnotation(Manifest.class).version(), 253, 425);
            g.setFont(font3);
            g.drawString("Paint by Maxmm", 4, 519);
            g.drawString("Script by NathanTehBeast", 358, 519);
        }
        if (paintMouse) {
            Point mouse = Mouse.getLocation();
            g.setColor(black);
            g.setStroke(stroke);
            g.drawLine(mouse.x, 0, mouse.x, 550);
            g.drawLine(0, mouse.y, 775, mouse.y);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        final Rectangle paint = new Rectangle(0, 388, 520, 140);
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
}
