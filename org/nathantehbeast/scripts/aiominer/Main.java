package org.nathantehbeast.scripts.aiominer;


import org.nathantehbeast.api.framework.Condition;
import org.nathantehbeast.api.framework.XScript;
import org.nathantehbeast.api.tools.Calc;
import org.nathantehbeast.api.tools.Logger;
import org.nathantehbeast.api.tools.Skill;
import org.nathantehbeast.api.tools.Utilities;
import org.nathantehbeast.scripts.aiominer.Constants.Ore;
import org.powerbot.core.event.events.MessageEvent;
import org.powerbot.core.event.listeners.MessageListener;
import org.powerbot.core.script.Script;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.Manifest;
import org.powerbot.game.api.methods.Calculations;
import org.powerbot.game.api.methods.Environment;
import org.powerbot.game.api.methods.Game;
import org.powerbot.game.api.methods.input.Keyboard;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.methods.tab.Skills;
import org.powerbot.game.api.util.Filter;
import org.powerbot.game.api.util.SkillData;
import org.powerbot.game.api.util.Time;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.node.SceneObject;
import org.powerbot.game.client.Client;
import sk.action.ActionBar;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Nathan
 * Date: 5/11/13
 * Time: 3:52 AM
 * To change this template use File | Settings | File Templates.
 */

@Manifest(
        authors = "NathanTehBeast",
        name = "Nathan's AIO Miner",
        description = "Powermines ore in various locations. Actionbar Dropping. If your location doesn't work, take a screenshot and post in thread.",
        topic = 1012215,
        website = "http://www.powerbot.org/community/topic/1012215-free-nathans-aio-miner-powermining-actionbar-dropping/",
        hidden = false,
        vip = false,
        instances = 3,
        version = 1.66
)

public final class Main extends XScript implements MessageListener, Script, MouseListener {

    private static final Color gold = new Color(0, 255, 247);
    private static final Color goldT = new Color(0, 255, 217, 150);
    private static final Color mask = new Color(0, 255, 0, 80);
    private static final Color white = Color.WHITE;
    private static final Color black = Color.BLACK;
    private static final Image paint = Utilities.getImage("http://puu.sh/3563g.jpg");
    private static final BasicStroke stroke = new BasicStroke(2);
    public static final RenderingHints antialiasing = new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

    private final Font font = new Font("Calibri", Font.PLAIN, 12);
    private final Font font1 = new Font("Lithos Pro Regular", Font.PLAIN, 10);
    private final Font font1_b = new Font("Lithos Pro Regular", Font.BOLD, 9);

    private volatile boolean showPaint = true;
    public static boolean paintMouse = true;

    private static Ore ore = Ore.COPPER;

    private final double version = getClass().getAnnotation(Manifest.class).version();

    private Client client;

    private Node currentNode = null;
    private static ArrayList<Node> nodes = new ArrayList<>();

    private static Tile startTile = null;
    private static int radius = 1;
    private int oresMined = 0;
    private static long startTime;
    private static SkillData sd;
    private GUI gui;
    public static boolean debug = true;
    private int orePrice;

    @Override
    protected boolean setup() {
        try {
            Utilities.loadFont(Font.TRUETYPE_FONT, "http://dl.dropboxusercontent.com/s/sz0p52rlowgwrid/Jokerman-Regular.ttf");
            Utilities.loadFont(Font.TRUETYPE_FONT, "http://dl.dropboxusercontent.com/s/i4y5ipsblbu64mv/LithosPro-Regular.ttf");
            Mouse.setSpeed(Mouse.Speed.VERY_FAST);
            gui = new GUI();
            Logger.log("You are using version " + version);
            delay = 50;
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void poll() {
        if (gui != null && gui.isVisible() && Game.isLoggedIn()) {
            startTile = Players.getLocal().getLocation();
            showPaint = true;
        }
        if (ore != null && orePrice == 0)
            orePrice = Calc.getPrice(ore.getId());
    }

    @Override
    public void paint(Graphics g) {
        long runTime = System.currentTimeMillis() - startTime;
        int profit = orePrice * oresMined;
        int profitHour = (int) (3600000.0 / runTime) * profit;

        SceneObject[] ROCKS;
        Graphics2D g2d = (Graphics2D) g;
        g2d.addRenderingHints(antialiasing);
        g.setFont(font);

        if (sd == null) {
            g2d.drawString("Waiting for GUI...", 5, 100);
        }
        if (currentNode != null && debug) {
            g2d.drawString("Current node: " + currentNode, 5, 100);
        }

        if (startTile != null && showPaint) { //Paints radius + rocks
            ROCKS = SceneEntities.getLoaded(new Filter<SceneObject>() {
                @Override
                public boolean accept(SceneObject sceneObject) {
                    if (ore.getRockArea() != null) {
                        return sceneObject != null && Utilities.contains(ore.getRocks(), sceneObject.getId()) && Calc.isInArea(ore.getRockArea(), sceneObject);
                    }
                    return sceneObject != null && Utilities.contains(ore.getRocks(), sceneObject.getId()) && (Calc.isInArea(startTile, sceneObject, radius));
                }
            });
            if (radius > 0) {
                g2d.setColor(goldT);
                Point p = Calculations.worldToMap(startTile.getX(), startTile.getY());
                g2d.fillOval(p.x - (radius * 5), p.y - (radius * 5), 5 * (radius * 2), 5 * (radius * 2));
                g2d.setColor(gold);
                g2d.drawOval(p.x - (radius * 5), p.y - (radius * 5), 5 * (radius * 2), 5 * (radius * 2));
            } else {
                if (ore.getRockArea() != null && ore.getRockArea().getCentralTile().isOnMap()) {
                    g2d.setColor(goldT);
                    Polygon p = new Polygon();
                    for (Tile t : ore.getRockArea().getBoundingTiles()) {
                        Point map = Calculations.worldToMap(t.getX(), t.getY());
                        p.addPoint(map.x, map.y);
                    }
                    g2d.fillPolygon(p);
                    g2d.setColor(gold);
                    g2d.drawPolygon(p);
                }
            }

            if (ROCKS != null) {
                for (final SceneObject so : ROCKS) {
                    for (final Polygon py : so.getBounds()) {
                        g2d.setColor(mask);
                        g2d.fill(py);
                    }
                }
            }
        }

        if (paintMouse) {
            Point mouse = Mouse.getLocation();
            g2d.setColor(black);
            g2d.setStroke(stroke);
            g2d.drawLine(mouse.x, 0, mouse.x, 550);
            g2d.drawLine(0, mouse.y, 775, mouse.y);
        }

        if (sd != null && showPaint) { //Main paint
            long oresHour = (int) ((3600000.0 / runTime) * oresMined);
            long ttl = (long) ((double) Skill.MINING.getExperienceRequired() / (double) ((int) ((3600000.0 / runTime) * sd.experience(Skills.MINING))) * 3600000);
            g2d.drawImage(paint, -2, 388, null);
            g2d.setFont(font1);
            g2d.setColor(white);
            g2d.drawString(Skills.getRealLevel(Skills.MINING) + "(+" + sd.level(Skills.MINING) + ")", 190, 443);
            if (ore.getBankArea() != null)
                g2d.drawString(profit + " (" + profitHour +"/h)", 360, 440);
            else
                g2d.drawString("N/A", 360, 440);
            g2d.drawString(sd.experience(Skills.MINING) + " (" + sd.experience(SkillData.Rate.HOUR, Skills.MINING) + "/h)", 190, 457);
            g2d.drawString(oresMined + " (" + oresHour + "/h)", 190, 470);
            if (Skills.getRealLevel(Skills.MINING) < 99)
                g2d.drawString(Time.format(ttl), 190, 483);
            else
                g2d.drawString("N/A", 190, 483);
            g2d.drawString(Time.format(System.currentTimeMillis() - startTime), 190, 496);
            g2d.setFont(font1_b);
            g2d.drawString("Script by NathanTehBeast", 359, 519);
            g2d.drawString("Paint by Maxmm", 6, 519);
            g2d.drawString("v" + Main.class.getAnnotation(Manifest.class).version(), 319, 425);
        }
    }

    @Override
    public void exit() {
        showPaint = true;
        paintMouse = false;
        sleep(100);
        Utilities.savePaint(0, 388, 520, 140);
        Logger.log("Thanks for using Nathan's AIO Miner!");
    }

    public static boolean setOre(Ore o) {
        ore = o;
        return ore.equals(o);
    }

    public static boolean setStartTile() {
        try {
            startTile = Players.getLocal().getLocation();
            Logger.log("Central tile set to " + startTile);
        } catch (Exception e) {
            Logger.log("Error while setting central tile.");
            return false;
        }
        return startTile == Players.getLocal().getLocation();
    }

    public static void setRadius(int r) {
        radius = r;
    }

    public static void startTimer() {
        startTime = System.currentTimeMillis();
    }

    public static void setSkillData() {
        sd = new SkillData();
        Logger.log("Initializing SkillData.");
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        final Rectangle area = new Rectangle(0, 388, 520, 140);
        if (area.contains(e.getPoint())) {
            showPaint = !showPaint;
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void messageReceived(MessageEvent me) {
        String msg = me.getId() != 2 ? me.getMessage().toLowerCase() : "";
        if (msg.contains("manage to mine some") || msg.contains("armour allows you to mine an additional ore")) {
            oresMined++;
        }
        if (msg.contains("manage to mine two")) {
            oresMined += 2;
        }
        if (me.getMessage().toLowerCase().contains("cya nerds") && !Environment.getDisplayName().toLowerCase().equals("nathantehbeast")) {
            ActionBar.setExpanded(false);
            Utilities.waitFor(new Condition() {
                @Override
                public boolean validate() {
                    return !ActionBar.isExpanded();
                }
            }, 3000);
            Keyboard.sendText("Bye!", true, 100, 200);
            if (Players.getLocal().isInCombat()) {
                Utilities.waitFor(new Condition() {
                    @Override
                    public boolean validate() {
                        return !Players.getLocal().isInCombat();
                    }
                }, 30000);
            }
            Game.logout(false);
            stop();
        }
    }
}
