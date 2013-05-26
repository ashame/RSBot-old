package org.nathantehbeast.scripts.guardKiller;

import org.nathantehbeast.api.framework.Condition;
import org.nathantehbeast.api.tools.Calculations;
import org.nathantehbeast.api.tools.Utilities;
import org.nathantehbeast.scripts.guardKiller.Nodes.*;
import org.powerbot.core.Bot;
import org.powerbot.core.event.listeners.PaintListener;
import org.powerbot.core.script.ActiveScript;
import org.powerbot.core.script.job.state.Branch;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.core.script.job.state.Tree;
import org.powerbot.game.api.Manifest;
import org.powerbot.game.api.methods.Game;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.tab.Skills;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.methods.widget.WidgetCache;
import org.powerbot.game.api.util.Filter;
import org.powerbot.game.api.util.Time;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.util.net.GeItem;
import org.powerbot.game.api.wrappers.Area;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.interactive.NPC;
import org.powerbot.game.client.Client;
import sk.action.ActionBar;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
@Manifest(
        authors = "NathanTehBeast",
        name = "GuardKiller",
        description = "Kills guards + loots grapes",
        version = 1.36,
        topic = 934250,
        website = "http://www.powerbot.org/community/topic/934250-free-f2p-guardkiller-abilities-support-good-profit-100kh-on-sdn",
        singleinstance = false)
/**
 *
 * @author Nathan
 */
public class GuardKiller extends ActiveScript implements PaintListener {

    public static final List<Node> jobsCollection = Collections.synchronizedList(new ArrayList<Node>());
    private Tree jobContainer = null;
    public static int foodId, foodAmount, skillId = 0;
    public static int expGained, levelsGained, expHour, profit, profitHour;
    public static boolean useAbilities = false;
    public static boolean start = false;
    public static final Area CASTLE = new Area(new Tile(3201, 3456, 0), new Tile(3324, 3474, 0));
    public static final Area BANK = new Area(new Tile(3178, 3432, 0), new Tile(3195, 3446, 0));
    public static final Tile[] PATH = new Tile[]{
        new Tile(3185, 3439, 0), new Tile(3186, 3445, 0), new Tile(3186, 3450, 0),
        new Tile(3191, 3450, 0), new Tile(3195, 3447, 0), new Tile(3198, 3443, 0),
        new Tile(3202, 3440, 0), new Tile(3206, 3437, 0), new Tile(3211, 3436, 0),
        new Tile(3212, 3441, 0), new Tile(3211, 3446, 0), new Tile(3212, 3451, 0),
        new Tile(3212, 3456, 0), new Tile(3212, 3461, 0)};
    public static final int[] GUARD = {5919, 5920};
    public static final int GRAPES = 1987;
    public static int grapePrice = 0;
    public static int grapesLooted = 0;
    public static int startExp, startLevel, expTNL;
    private static long startTime, runTime, ssCheck, timeTNL;
    public static final RenderingHints antialiasing = new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    public static final Color color1 = new Color(255, 255, 255);
    public static final Color color2 = new Color(0, 0, 0);
    public static final BasicStroke stroke1 = new BasicStroke(1);
    public static final Font font1 = new Font("Arial", 1, 15);
    public static final Font font2 = new Font("Arial", 0, 11);
    public static final Font font3 = new Font("Arial", 0, 10);
    private static String foodUsed = "";
    private static String skill = "";
    private static Client client = Bot.client();
    public static boolean momentum = true;
    Timer ss = new Timer(System.currentTimeMillis());
    public static ArrayList<Integer> toBank;
    public static Node currentNode = null;

    @Override
    public void onStop() {
        Utilities.savePaint(0, 1, 765, 48);
    }

    @Override
    public void onStart() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    GUI gui = new GUI();
                    if (!gui.isVisible()) {
                        gui.setVisible(true);
                    }
                } catch (Throwable t) {
                    System.out.println("Error loading GUI: " + t.getMessage());
                }
            }
        });
        while (!start) {
            sleep(300);
        }
        grapePrice = Calculations.getPrice(GRAPES);
        Node[] branch1 = new Node[]{new fightGuards(), new eatFood(), new lootGrapes()}; //Idle & at castle area
        Node[] branch2 = new Node[]{new bankItems()}; //at Bank
        provide(new branch1(branch1), new branch2(branch2), new openBank(), new walkCastle(), new walkBank(), new useAbility());
        Utilities.waitFor(new Condition() {
            @Override
            public boolean validate() {
                return Game.isLoggedIn();
            }
        }, 300000);
        startTime = System.currentTimeMillis();
        switch (skillId) {
            case 0:
                skill = "Attack";
                break;
            case 1:
                skill = "Defence";
                break;
            case 2:
                skill = "Strength";
                break;
            case 3:
                skill = "Constitution";
                break;
            case 4:
                skill = "Ranged";
                break;
            case 5:
                skill = "Magic";
                skillId = 6;
                break;
            default:
                skill = Integer.toString(skillId);
                break;
        }
        startExp = Skills.getExperience(skillId);
        startLevel = Skills.getRealLevel(skillId);
        if (foodId == 0) {
            foodUsed = "None";
        }
        if (foodId != 0) {
            foodUsed = GeItem.lookup(foodId).getName();
            foodAmount = Inventory.getCount(foodId);
        }
        Mouse.setSpeed(Mouse.Speed.FAST);
    }

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
        if (Walking.getEnergy() >= 50) {
            Walking.setRun(true);
        }
        if (Camera.getPitch() > 20) {
            Camera.setPitch(15);
        }
        if (ActionBar.isExpanded() && !useAbilities) {
            ActionBar.setExpanded(false);
        }
        if ((ss.getElapsed() - ssCheck) > 3600000) {
            Utilities.savePaint(0, 0, 765, 48);
            ssCheck = ss.getElapsed();
        }
        return 50;
    }
    public static final Filter<NPC> GUARDS = new Filter<NPC>() {
        @Override
        public boolean accept(NPC n) {
            for (int i : GUARD) {
                if (n.getId() == i && n.getInteracting() == null && n.validate()) {
                    return true;
                }
            }
            return false;
        }
    };

    public class branch1 extends Branch {

        public branch1(Node[] nodes) {
            super(nodes);
        }

        @Override
        public boolean branch() {
            return Utilities.isIdle() && Utilities.isAt(CASTLE) && Game.getClientState() == Game.INDEX_MAP_LOADED;
        }
    }

    public class branch2 extends Branch {

        public branch2(Node[] nodes) {
            super(nodes);
        }

        @Override
        public boolean branch() {
            return Utilities.isAt(BANK) && Game.getClientState() == Game.INDEX_MAP_LOADED;
        }
    }

    public static boolean grapeLooted(int i) {
        Timer t = new Timer(3000);
        while (Inventory.getCount(GRAPES) < i && t.isRunning()) {
            sleep(300);
        }
        if (Inventory.getCount(GRAPES) > i) {
            return true;
        }
        return false;
    }

    @Override
    public void onRepaint(Graphics g1) {
        runTime = System.currentTimeMillis() - startTime;
        expGained = Skills.getExperience(skillId) - startExp;
        levelsGained = Skills.getRealLevel(skillId) - startLevel;
        expHour = (int) ((3600000.0 / runTime) * expGained);
        profit = grapesLooted * grapePrice;
        profitHour = (int) ((3600000.0 / runTime) * profit);
        if (Skills.getRealLevel(skillId) < 99) {
            expTNL = Skills.getExperienceRequired(Skills.getRealLevel(skillId) + 1) - Skills.getExperience(skillId);
        } else {
            expTNL = 0;
        }
        if (Skills.getRealLevel(skillId) < 99) {
            timeTNL = (long) ((double) expTNL / (double) ((int) ((3600000.0 / runTime) * expGained)) * 3600000);
        }

        Graphics2D g = (Graphics2D) g1;

        if (currentNode != null) {
            g.drawString("Current node: "+currentNode, 5, 100);
        }
        g.setRenderingHints(antialiasing);

        g.setColor(color1);
        g.fillRect(0, 0, 765, 48);
        g.setColor(color2);
        g.setStroke(stroke1);
        g.drawRect(0, 0, 765, 48);
        g.setFont(font1);
        g.drawString("GuardKiller", 5, 15);
        g.setFont(font2);
        if (levelsGained > 0) {
            g.drawString(skill + " Level:" + Skills.getRealLevel(skillId) + " (+" + levelsGained + ") (" + Skills.getExperience(skillId) + ")", 5, 30);
        } else {
            g.drawString(skill + " Level: " + Skills.getRealLevel(skillId) + " (" + Skills.getExperience(skillId) + ")", 5, 30);
        }
        g.drawString("EXP Gained: " + expGained + " (" + expHour + ")", 5, 45);
        g.drawString("Grapes Looted: " + grapesLooted, 219, 15);
        g.drawString("Profit Earned: " + profit + " (" + profitHour + ")", 219, 30);
        g.drawString("EXP TNL: " + expTNL, 219, 45);
        g.drawString("Time Ran: " + Calculations.formatTime(runTime), 429, 15);
        if (!useAbilities) {
            g.drawString("Using food: " + foodUsed + "*", 429, 30);
        } else {
            g.drawString("Using food: " + foodUsed, 429, 30);
        }
        if (Skills.getLevel(skillId) < 99) {
            g.drawString("Time TNL: " + Time.format(timeTNL), 429, 45);
        } else {
            g.drawString("Time TNL: Undefined", 429, 45);
        }
        g.setFont(font3);
        g.drawString("by NathanTehBeast", 666, 45);
        g.drawString("Version: " + GuardKiller.class.getAnnotation(Manifest.class).version(), 666, 15);
    }
}
