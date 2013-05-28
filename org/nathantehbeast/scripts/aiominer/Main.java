package org.nathantehbeast.scripts.aiominer;


import org.nathantehbeast.api.framework.Condition;
import org.nathantehbeast.api.tools.Calc;
import org.nathantehbeast.api.tools.Skill;
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

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

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
        version                 = 1.62
)

public final class Main extends ActiveScript implements MessageListener, PaintListener, MouseListener {

    private static final Color gold = new Color(255, 215, 0);
    private static final Color goldT = new Color(255, 215, 0, 150);
    private static final Color mask = new Color(0, 255, 0, 80);
    private static final Color white = Color.WHITE;
    private static final Color black = Color.BLACK;
    private static final Image paint = Utilities.getImage("http://puu.sh/32cpZ.jpg");
    private static final BasicStroke stroke = new BasicStroke(2);

    private final Font font = new Font("Calibri", Font.PLAIN, 12);
    private final Font font1 = new Font("Lithos Pro Regular", Font.PLAIN, 10);
    private final Font font1_b = new Font("Lithos Pro Regular", Font.BOLD, 9);

    private volatile boolean showPaint = true;
    public static boolean paintMouse = true;

    private static Ore ore = Ore.COPPER;

    private final double version = getClass().getAnnotation(Manifest.class).version();
    private final String user = Bot.context().getDisplayName();

    private Client client;

    private Node currentNode = null;
    private static ArrayList<Node> nodes = new ArrayList<>();

    private static Tile startTile = null;
    private static int radius = 1;
    private int oresMined = 0;
    private static long startTime;
    private static SkillData sd;
    private GUI gui;
    public static Logger logger;
    public static boolean debug = false;

    @Override
    public void onStart() {
        Utilities.loadFont(Font.TRUETYPE_FONT, "http://dl.dropboxusercontent.com/s/sz0p52rlowgwrid/Jokerman-Regular.ttf");
        Utilities.loadFont(Font.TRUETYPE_FONT, "http://www.dropbox.com/s/i4y5ipsblbu64mv/LithosPro-Regular.ttf");
        Mouse.setSpeed(Mouse.Speed.VERY_FAST);
        logger = new Logger();
        if (debug) {
            logger.display();
        }
        gui = new GUI();
        log("Welcome " + user);
        log("You are using version " + version);
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
            log("Timer plx fix internal errors");
        }
        return 600;
    }

    @Override
    public void onStop() {
        showPaint = true;
        paintMouse = false;
        sleep(100);
        Utilities.savePaint(0, 388, 520, 140);
        log("Thanks for using Nathan's AIO Miner!");
    }

    @Override
    public void messageReceived(MessageEvent me) {
        String msg = me.getId() != 2 ? me.getMessage().toLowerCase() : "";
        if (msg.contains("mine some") || msg.contains("armour allows you to mine an additional ore")) {
            oresMined++;
        }
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

    @Override
    public void onRepaint(Graphics g) {
        long runTime = System.currentTimeMillis() - startTime;
        SceneObject[] ROCKS = null;
        Graphics2D g2d = (Graphics2D) g;
        g.setFont(font);

        g2d.drawString("Run Time: " + Time.format(runTime), 5, 85);
        if (sd == null) {
            g2d.drawString("Waiting for GUI...", 5, 100);
        }
        if (currentNode != null && sd != null) {
            long ttl = (long) ((double) Skill.MINING.getExperienceRequired() / (double) ((int) ((3600000.0 / runTime) * sd.experience(Skills.MINING))) * 3600000);
            g2d.drawString("Current node: " + currentNode, 5, 100);
            g2d.drawString("Time till level: " +Time.format(ttl), 5, 115);
        }

        if (startTile != null && showPaint) { //Paints radius + rocks
            ROCKS = SceneEntities.getLoaded(new Filter<SceneObject>() {
                @Override
                public boolean accept(SceneObject sceneObject) {
                    return sceneObject != null && Utilities.contains(ore.getRocks(), sceneObject.getId()) && Calc.isInArea(startTile, sceneObject, radius);
                }
            });
            g2d.setColor(goldT);
            Point p = Calculations.worldToMap(startTile.getX(), startTile.getY());
            g2d.fillOval(p.x - (radius * 5), p.y - (radius * 5), 5 * (radius * 2), 5 * (radius * 2));
            g2d.setColor(gold);
            g2d.drawOval(p.x - (radius * 5), p.y - (radius * 5), 5 * (radius * 2), 5 * (radius * 2));

            if (ROCKS != null) {
                for (final SceneObject so : ROCKS) {
                    for (final Polygon py : so.getBounds()) {
                        g2d.setColor(mask);
                        g2d.fill(py);
                    }
                }
            }
        }

        if (sd != null && showPaint) { //Main paint
            long oresHour = (int) ((3600000.0 / runTime) * oresMined);
            g2d.drawImage(paint, -2, 388, null);
            g2d.setFont(font1);
            g2d.setColor(white);
            g2d.drawString(Skills.getRealLevel(Skills.MINING) +"(+"+sd.level(Skills.MINING)+")", 190, 443);
            g2d.drawString(sd.experience(Skills.MINING) + " (" + sd.experience(SkillData.Rate.HOUR, Skills.MINING) + "/h)", 190, 457);
            g2d.drawString(oresMined + " ("+oresHour+"/h)", 190, 470);
            g2d.setFont(font1_b);
            g2d.drawString("Script by NathanTehBeast", 359, 519);
            g2d.drawString("Paint by Maxmm", 6, 519);
            g2d.drawString("v"+Main.class.getAnnotation(Manifest.class).version(), 319, 425);
        }
        if (paintMouse) {
            Point mouse = Mouse.getLocation();
            g2d.setColor(black);
            g2d.setStroke(stroke);
            g2d.drawLine(mouse.x, 0, mouse.x, 550);
            g2d.drawLine(0, mouse.y, 775, mouse.y);
        }
    }

    public static boolean setOre(Ore o) {
        ore = o;
        return ore.equals(o);
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

    @Override
    public void mouseClicked(MouseEvent e) {
        final Rectangle area = new Rectangle(0, 388, 520, 140);
        if (area.contains(e.getPoint())) {
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

    public static void log(String s) {
        logger.log(s);
    }

    private class Logger {
        public void main(String[] args) {
            Logger logger = new Logger();
            logger.display();
            log("started");
        }

        public void display() {
            if(frame != null) {
                frame.setVisible(true);
            }
        }

        public Logger() {
            init();
        }

        public void dispose() {
            if(frame != null && frame.isVisible()) {
                frame.dispose();
            }
        }

        public void setTitle(String title) {
            if(frame != null) {
                frame.setTitle(title);
            }
        }

        public String getTime(String dateFormat) {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
            return sdf.format(cal.getTime());
        }

        public void log() {
            textArea1.append(System.getProperty("line.separator"));
            textArea1.scrollRectToVisible(new Rectangle(0, textArea1.getHeight() - 2, 1, 1));
        }

        public void log(String o) {
            try {
                textArea1.append("[" + getTime("hh:mm:ss z") + "] " + o + System.getProperty("line.separator"));
                textArea1.scrollRectToVisible(new Rectangle(0, textArea1.getHeight() - 2, 1, 1));
                System.out.println(o);
            } catch(Exception ignored) {}
        }

        public void init() {

            frame = new JFrame("Log");
            scrollPane1 = new JScrollPane();
            textArea1 = new JTextArea();

            Container contentPane = frame.getContentPane();

            {

                //---- textArea1 ----
                textArea1.setFont(new Font("Lithos Pro Regular", Font.PLAIN, 11));
                textArea1.setWrapStyleWord(true);
                textArea1.setBackground(Color.black);
                textArea1.setForeground(Color.WHITE);
                textArea1.setEditable(false);
                textArea1.setLineWrap(true);
                scrollPane1.setViewportView(textArea1);
            }

            GroupLayout contentPaneLayout = new GroupLayout(contentPane);
            contentPane.setLayout(contentPaneLayout);
            contentPaneLayout.setHorizontalGroup(
                    contentPaneLayout.createParallelGroup()
                            .addComponent(scrollPane1, GroupLayout.DEFAULT_SIZE, 749, Short.MAX_VALUE)
            );
            contentPaneLayout.setVerticalGroup(
                    contentPaneLayout.createParallelGroup()
                            .addComponent(scrollPane1, GroupLayout.DEFAULT_SIZE, 209, Short.MAX_VALUE)
            );
            frame.setSize(780, 245);
            frame.setLocationRelativeTo(frame.getOwner());
            frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        }

        private JScrollPane scrollPane1;
        private JTextArea textArea1;
        private JFrame frame;
    }
}
