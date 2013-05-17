package org.nathantehbeast.scripts.yanilleIron;

import org.nathantehbeast.api.framework.Condition;
import org.nathantehbeast.api.tools.Calculations;
import org.nathantehbeast.api.tools.Utilities;
import org.nathantehbeast.scripts.yanilleIron.Nodes.*;
import org.powerbot.core.Bot;
import org.powerbot.core.event.events.MessageEvent;
import org.powerbot.core.event.listeners.MessageListener;
import org.powerbot.core.event.listeners.PaintListener;
import org.powerbot.core.randoms.SpinTickets;
import org.powerbot.core.script.ActiveScript;
import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.core.script.job.state.Tree;
import org.powerbot.game.api.Manifest;
import org.powerbot.game.api.methods.Environment;
import org.powerbot.game.api.methods.Game;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.methods.tab.Skills;
import org.powerbot.game.api.methods.widget.Bank;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.methods.widget.WidgetCache;
import org.powerbot.game.api.util.Filter;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.map.TilePath;
import org.powerbot.game.api.wrappers.node.Item;
import org.powerbot.game.api.wrappers.node.SceneObject;
import org.powerbot.game.client.Client;
import sk.action.ActionBar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
@Manifest(
        authors = "NathanTehBeast",
        name = "YanilleIron",
        description = "Mines iron at yanille, option to powermine.",
        version = 1.25,
        topic = 976673,
        website = "http://www.powerbot.org/community/topic/976673-yanilleiron-actionbar-dropping-powermining-support-banking-support/")
/**
 *
 * @author Nathan
 */
public class YanilleIron extends ActiveScript implements MessageListener, PaintListener, MouseListener {

    public static final boolean debug = false;
    private boolean hide = false;
    public static final List<Node> jobsCollection = Collections.synchronizedList(new ArrayList<Node>());
    public static Tree jobContainer = null;
    public static Client client = Bot.client();
    public static boolean pm = JOptionPane.showInputDialog(new javax.swing.JFrame(), "Powermine? Type YES or NO", JOptionPane.INFORMATION_MESSAGE).toLowerCase().matches("yes") ? true : false;
    public static int startExp, startLevel, levelsGained, expGained, expTNL, expHour, oresMined,
            oresHour, profitEarned, profitHour, sapphiresMined, rubiesMined, emeraldsMined, diamondsMined,
            ironPrice, sapphirePrice, emeraldPrice, rubyPrice, diamondPrice, gemsMined, gemPrice;
    public static long startTime, runTime, timeTNL, ssCheck;
    public static final int[] ROCK = {37308, 37309};
    public static final int IRON = 440;
    public static final int SAPPHIRE = 1623;
    public static final int RUBY = 1621;
    public static final int EMERALD = 1619;
    public static final int DIAMOND = 1617;
    public static final Tile[] PATH_Y = new Tile[]{
        new Tile(2610, 3092, 0), new Tile(2606, 3095, 0), new Tile(2605, 3100, 0),
        new Tile(2610, 3102, 0), new Tile(2615, 3104, 0), new Tile(2617, 3109, 0),
        new Tile(2619, 3114, 0), new Tile(2621, 3119, 0), new Tile(2622, 3124, 0),
        new Tile(2623, 3129, 0), new Tile(2624, 3134, 0), new Tile(2626, 3139, 0)};
    public static final RenderingHints antialiasing = new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    public static ArrayList<Integer> toBank = new ArrayList<>();
    public static ArrayList<Item> toDrop = new ArrayList<>();
    private static Map<Integer, Integer> prices;
    public static int world;
    TilePath path = new TilePath(new Tile[]{new Tile(0, 0, 0)});
    Timer t = new Timer(4000000);
    public static final Filter<Item> itemFilter = new Filter<Item>() {
        @Override
        public boolean accept(Item i) {
            if (i != null && !i.getName().toLowerCase().contains("pickaxe") && i.getId() != SpinTickets.ITEM_ID_SPIN_TICKET) {
                return true;
            }
            return false;
        }
    };
    public static final Filter<Item> dropFilter = new Filter<Item>() {
        @Override
        public boolean accept(Item t) {
            if (t != null) {
                if (t.getId() == SAPPHIRE || t.getId() == EMERALD || t.getId() == RUBY || t.getId() == DIAMOND) {
                    return true;
                }
            }
            return false;
        }
    };
    public static final Filter<SceneObject> bankFilter = new Filter<SceneObject>() {
        @Override
        public boolean accept(SceneObject t) {
            if (t != null) {
                for (int i : Bank.BANK_BOOTH_IDS) {
                    if (t.getId() == i && t.getLocation().canReach()) {
                        return true;
                    }
                }
                for (int i : Bank.BANK_CHEST_IDS) {
                    if (t.getId() == i && t.getLocation().canReach()) {
                        return true;
                    }
                }
                for (int i : Bank.BANK_COUNTER_IDS) {
                    if (t.getId() == i && t.getLocation().canReach()) {
                        return true;
                    }
                }
            }
            return false;
        }
    };

    public synchronized final void provide(Node... jobs) {
        for (final Node job : jobs) {
            if (!jobsCollection.contains(job)) {
                jobsCollection.add(job);
            }
        }
        jobContainer = new Tree(jobsCollection.toArray(new Node[jobsCollection.size()]));
    }

    @Override
    public int loop() {
        if (jobContainer != null) {
            final Node job = jobContainer.state();
            if (job != null) {
                jobContainer.set(job);
                getContainer().submit(job);
                job.join();
            }
        }
        if (client != Bot.client()) {
            WidgetCache.purge();
            Bot.context().getEventManager().addListener(this);
            client = Bot.client();
        }
        if (Walking.getEnergy() >= 50 && !Walking.isRunEnabled()) {
            Walking.setRun(true);
        }
        if (!ActionBar.isOpen() && pm) {
            ActionBar.makeReady();
        }
        if (ActionBar.isOpen() && !pm) {
            ActionBar.expand(false);
        }
        if (Camera.getPitch() < 20 || Camera.getPitch() > 40) {
            Camera.setPitch(30);
        }
        if ((t.getElapsed() - ssCheck) >= 3600000) {
            Utilities.savePaint(0, 388, 515, 140);
            ssCheck = t.getElapsed();
        }

        return 150;
    }

    @Override
    public void onStart() {
        WidgetCache.purge();
        client = Bot.client();
        while (Game.getClientState() != Game.INDEX_MAP_LOADED) {
            sleep(50);
        }
        Utilities.waitFor(new Condition() {
            @Override
            public boolean validate() {
                return Game.isLoggedIn();
            }
        }, 200, 3500);
        prices = Calculations.getPrices(IRON, SAPPHIRE, EMERALD, RUBY, DIAMOND);
        startExp = Skills.getExperience(Skills.MINING);
        startTime = System.currentTimeMillis();
        startLevel = Skills.getRealLevel(Skills.MINING);
        provide(new mineOres(), new dropOres(), new bankOres(), new walkOresY(), new walkBankY(), new ClaimTickets());
        getContainer().submit(new Task() {
            @Override
            public void execute() {
                sleep(5000);
                Environment.enableRandom(SpinTickets.class, false);
            }
        });
        ironPrice = prices.get(IRON);
        sapphirePrice = prices.get(SAPPHIRE);
        emeraldPrice = prices.get(EMERALD);
        rubyPrice = prices.get(RUBY);
        diamondPrice = prices.get(DIAMOND);
        Mouse.setSpeed(Mouse.Speed.FAST);
        world = Integer.parseInt(Widgets.get(550).getChild(18).getText().substring(26));
    }

    @Override
    public void onStop() {
        Utilities.savePaint(0, 388, 515, 140);
    }

    @Override
    public void messageReceived(MessageEvent me) {
        String msg = me.getId() != 2 ? me.getMessage().toString().toLowerCase() : "";
        if (msg.contains("mine some iron")) {
            oresMined++;
        }
        if (msg.contains("an additional ore")) {
            oresMined++;
        }
        if (msg.contains("just found a sapphire")) {
            sapphiresMined++;
        }
        if (msg.contains("just found a ruby")) {
            rubiesMined++;
        }
        if (msg.contains("just found an emerald")) {
            emeraldsMined++;
        }
        if (msg.contains("just found a diamond")) {
            diamondsMined++;
        }
    }

    private final Color color1 = new Color(255, 255, 255);
    private final Color color2 = new Color(0, 0, 0);

    private final BasicStroke stroke1 = new BasicStroke(1);

    private final Font font1 = new Font("Arial", 0, 11);

    private final Image img1 = Utilities.getImage("http://i.imgur.com/nQIYcET.png");

    @Override
    public void onRepaint(Graphics g1) {
        if (!hide) {
        runTime = System.currentTimeMillis() - startTime;
        levelsGained = Skills.getRealLevel(Skills.MINING) - startLevel;
        expGained = Skills.getExperience(Skills.MINING) - startExp;
        oresHour = (int) ((3600000.0 / runTime) * oresMined);
        expHour = (int) ((3600000.0 / runTime) * expGained);
        expTNL = Skills.getExperienceRequired(Skills.getRealLevel(Skills.MINING) + 1) - Skills.getExperience(Skills.MINING);
        timeTNL = (long) ((double) expTNL / (double) ((int) ((3600000.0 / runTime) * expGained)) * 3600000);
        gemsMined = sapphiresMined + emeraldsMined + rubiesMined + diamondsMined;
        gemPrice = (sapphiresMined * sapphirePrice) + (emeraldsMined * emeraldPrice) + (rubiesMined * rubyPrice) + (diamondsMined * diamondPrice);
        profitEarned = gemPrice + (oresMined * ironPrice);
        profitHour = (int) ((3600000.0 / runTime) * profitEarned);

        Graphics2D g = (Graphics2D) g1;
        g.setRenderingHints(antialiasing);

        g.drawImage(img1, -2, 388, null);
        g.setFont(font1);
        g.setColor(color1);

        if (levelsGained == 0)
            g.drawString(Integer.toString(Skills.getRealLevel(Skills.MINING)), 154, 447);
        else
            g.drawString(Skills.getRealLevel(Skills.MINING) + " (" + levelsGained + ")", 154, 447);
        g.drawString(Calculations.formatTime(runTime), 147, 468);
        g.drawString(Integer.toString(expGained), 137, 488);
        g.drawString(Integer.toString(expTNL), 140, 510);
        g.drawString(Calculations.formatTime(runTime), 390, 448);
        g.drawString(oresMined + " (" + oresHour + ")", 400, 467);
        g.drawString(Integer.toString(gemsMined), 407, 488);
        g.drawString(profitEarned + " (" + profitHour + ")", 410, 510);
        g.fillRect(480, 404, 22, 22);
        g.setColor(color2);
        g.setStroke(stroke1);
        g.drawRect(480, 404, 22, 22);
        sleep(300);
        }
        if (hide) {
            Graphics2D g = (Graphics2D) g1;
            g.fillRect(480, 404, 22, 22);
            g.setColor(Color.red);
            g.setStroke(stroke1);
            g.drawRect(480, 404, 22, 22);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Point p = e.getPoint();
        Rectangle hide = new Rectangle(480, 404, 22, 22);
        if (hide.contains(p)) {
            this.hide = !this.hide;
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
