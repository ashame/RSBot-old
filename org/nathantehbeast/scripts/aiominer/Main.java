package org.nathantehbeast.scripts.aiominer;


import org.nathantehbeast.api.framework.Condition;
import org.nathantehbeast.api.tools.Calc;
import org.nathantehbeast.api.tools.Utilities;
import org.nathantehbeast.scripts.aiominer.Constants.Ore;
import org.powerbot.core.Bot;
import org.powerbot.core.event.events.MessageEvent;
import org.powerbot.core.event.listeners.MessageListener;
import org.powerbot.core.event.listeners.PaintListener;
import org.powerbot.core.script.ActiveScript;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.Manifest;
import org.powerbot.game.api.methods.Calculations;
import org.powerbot.game.api.methods.Game;
import org.powerbot.game.api.methods.input.Keyboard;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.methods.tab.Skills;
import org.powerbot.game.api.methods.widget.WidgetCache;
import org.powerbot.game.api.util.Filter;
import org.powerbot.game.api.util.SkillData;
import org.powerbot.game.api.util.Time;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.node.SceneObject;
import org.powerbot.game.client.Client;
import sk.action.ActionBar;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Nathan
 * Date: 5/11/13
 * Time: 3:52 AM
 * To change this template use File | Settings | File Templates.
 */

@Manifest(
        authors                 = "NathanTehBeast",
        name                    = "Nathan's AIO Miner",
        description             = "Powermines ore in various locations. Actionbar Dropping. If your location doesn't work, take a screenshot and post in thread.",
        topic                   = 1012215,
        website                 = "http://www.powerbot.org/community/topic/1012215-free-nathans-aio-miner-powermining-actionbar-dropping/",
        hidden                  = false,
        vip                     = false,
        instances               = 3,
        version                 = 1.6
)

public final class Main extends ActiveScript implements MessageListener, PaintListener {

    private static final Color gold = new Color(255, 215, 0);
    private static final Color goldT = new Color(255, 215, 0, 150);
    private static final Color mask = new Color(0, 255, 0, 80);

    private static boolean powermine = true;

    private static Ore ore = Ore.COPPER;

    private final double version = getClass().getAnnotation(Manifest.class).version();
    private final String user = Bot.context().getDisplayName();

    private Client client;

    private Node currentNode = null;
    private static ArrayList<Node> nodes = new ArrayList<>();

    private static Tile startTile = null;
    private static int radius = 1;
    private static long startTime;
    private static SkillData sd;
    private GUI gui;

    @Override
    public void onStart() {
        System.out.println("Welcome " + user);
        System.out.println("You are using version " + version);
        Utilities.loadFont(Font.TRUETYPE_FONT, "http://dl.dropboxusercontent.com/s/sz0p52rlowgwrid/Jokerman-Regular.ttf");
        Mouse.setSpeed(Mouse.Speed.VERY_FAST);
        gui = new GUI();
    }

    @Override
    public int loop() {
        try {
            if (Game.getClientState() != Game.INDEX_MAP_LOADED) {
                return 1000;
            }
            if (client != Bot.client()) {
                WidgetCache.purge();
                Bot.context().getEventManager().addListener(this);
                client = Bot.client();
            }
            for (Node node : nodes) {
                if (node.activate()) {
                    currentNode = node;
                    node.execute();
                }
            }
            if (gui != null && gui.isVisible() && Game.isLoggedIn()) {
                startTile = Players.getLocal().getLocation();
            }
        } catch (Exception e) {
            System.out.println("Timer plx fix internal errors");
        }
        return 600;
    }

    @Override
    public void onStop() {
        System.out.println("Thanks for using Nathan's AIO Miner!");
    }

    @Override
    public void messageReceived(MessageEvent me) {
        if (me.getMessage().toLowerCase().contains("cya nerds")) {
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

    final Font font = new Font("Calibri", Font.PLAIN, 12);

    @Override
    public void onRepaint(Graphics g) {
        long runTime = System.currentTimeMillis() - startTime;
        SceneObject[] ROCKS = null;
        if (startTile != null )                                                            {
            ROCKS = SceneEntities.getLoaded(new Filter<SceneObject>() {
                @Override
                public boolean accept(SceneObject sceneObject) {
                    return sceneObject != null && Utilities.contains(ore.getRocks(), sceneObject.getId()) && Calc.isInArea(startTile, sceneObject, radius);
                }
            });
        }

        Graphics2D g2d = (Graphics2D) g;

        g.setFont(font);
        if (sd == null ) {
            g2d.drawString("Waiting for GUI...", 5, 100);
        }
        if (sd != null) {
            long timeTNL = (long) ((double) (Skills.getExperienceRequired(Skills.getRealLevel(Skills.MINING) + 1) - Skills.getExperience(Skills.MINING)) / (double) ((int) ((3600000.0 / runTime) * sd.experience(Skills.MINING))) * 3600000);
            g2d.drawString("Run Time: " + Time.format(runTime), 5, 85);
            if (currentNode != null) {
                g2d.drawString("Current node: " + currentNode, 5, 100);
            }
            g2d.drawString("XP Gained: " + sd.experience(Skills.MINING) + " (" + sd.experience(SkillData.Rate.HOUR, Skills.MINING) + " p/h)", 5, 115);
            g2d.drawString("Mining Level: " + Skills.getRealLevel(Skills.MINING) +"(+"+sd.level(Skills.MINING)+")", 5, 130);
            g2d.drawString("TTL: " + Time.format(timeTNL), 5, 145);
        }

        if (startTile != null) {
            g2d.setColor(goldT);
            Point p = Calculations.worldToMap(startTile.getX(), startTile.getY());
            g2d.fillOval(p.x - (radius * 5), p.y - (radius * 5), 5 * (radius * 2), 5 * (radius * 2));
            g2d.setColor(gold);
            g2d.drawOval(p.x - (radius * 5), p.y - (radius * 5), 5 * (radius * 2), 5 * (radius * 2));
        }

        if (ROCKS != null) {
            for (final SceneObject so : ROCKS) {
                for (final Polygon p : so.getBounds()) {
                    g2d.setColor(mask);
                    g2d.fill(p);
                }
            }
        }
    }

    public static boolean setOre(Ore o) {
        ore = o;
        return ore.equals(o);
    }

    public static Ore getOre() {
        return ore;
    }

    public static boolean setPowermine(boolean b) {
        return powermine = b;
    }

    public static boolean getPowermine() {
        return powermine;
    }

    public static boolean setStartTile() {
        try {
            startTile = Players.getLocal().getLocation();
        } catch (Exception e) {
            System.out.println("Error while setting central point.");
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
    }

    public static void provide(Node... n) {
        for (Node node : n) {
            System.out.println("Providing: "+node);
            nodes.add(node);
        }
    }
}
