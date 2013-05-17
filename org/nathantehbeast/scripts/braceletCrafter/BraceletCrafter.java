package org.nathantehbeast.scripts.braceletCrafter;

import org.nathantehbeast.api.tools.Calculations;
import org.nathantehbeast.api.tools.Utilities;
import org.nathantehbeast.scripts.braceletCrafter.Nodes.BankItems;
import org.nathantehbeast.scripts.braceletCrafter.Nodes.ClaimTickets;
import org.nathantehbeast.scripts.braceletCrafter.Nodes.CraftBracelets;
import org.powerbot.core.Bot;
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
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.methods.tab.Skills;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.methods.widget.WidgetCache;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.client.Client;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
@Manifest(
        authors = "NathanTehBeast", 
        name = "BraceletCrafter", 
        description = "Crafts Gold Bars into Bracelets at Edgeville.", 
        version = 1.27, 
        topic = 948733, 
        website = "http://www.powerbot.org/community/topic/948733-braceletcrafter/"
        )
/**
 *
 * @author Nathan
 */
public class BraceletCrafter extends ActiveScript implements PaintListener {

    //Paint
    private static final RenderingHints antialiasing = new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    private static final Color color1 = new Color(255, 255, 255);
    private static final Color color2 = new Color(0, 0, 0);
    private static final BasicStroke stroke1 = new BasicStroke(1);
    private static final Font font1 = new Font("Arial", 1, 15);
    private static final Font font2 = new Font("Arial", 0, 11);
    private static final Font font3 = new Font("Arial", 0, 10);
    private static int startLevel, startExp;
    public static int crafted;
    private static long startTime, runTime, ssTimer;
    private Client client = Bot.client();
    //Item IDs
    private static HashMap<Integer, Integer> prices;
    private static final int GOLD_ID = 2357;
    private static final int BRACELET_ID = 11069;
    private static int goldPrice, braceletPrice;
    //Jobs
    private static final List<Node> jobsCollection = Collections.synchronizedList(new ArrayList<Node>());
    private Tree jobContainer = null;
    Timer ss = new Timer(System.currentTimeMillis());

    public synchronized final void provide(Node... jobs) {
        for (final Node job : jobs) {
            if (!jobsCollection.contains(job)) {
                jobsCollection.add(job);
            }
        }
        jobContainer = new Tree(jobsCollection.toArray(new Node[jobsCollection.size()]));
    }

    @Override
    public void onStop() {
        Utilities.savePaint(0, 0, 765, 48);
        System.out.println("Thank you for using Nathan's Bracelet Crafter! A screenshot of the paint has been saved to a temporary directory. Check the forum post on how to get find it.");
    }

    @Override
    public void onStart() {
        while (Game.getClientState() != Game.INDEX_MAP_LOADED) {
            sleep(1000);
        }
        prices = Calculations.getPrice(GOLD_ID, BRACELET_ID);
        getContainer().submit(new Task() {
            @Override
            public void execute() {
                sleep(5000);
                Environment.enableRandom(SpinTickets.class, false);
            }
        });
        provide(new CraftBracelets(), new BankItems(), new ClaimTickets());
        Mouse.setSpeed(Mouse.Speed.FAST);
        startTime = System.currentTimeMillis();
        startExp = Skills.getExperience(Skills.CRAFTING);
        startLevel = Skills.getRealLevel(Skills.CRAFTING);
        if (prices.size() != 0) {
            goldPrice = prices.get(GOLD_ID);
            braceletPrice = prices.get(BRACELET_ID);
        }
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
        if (Game.getClientState() != Game.INDEX_MAP_LOADED) {
            return 1000;
        }
        if (client != Bot.client()) {
            WidgetCache.purge();
            Bot.context().getEventManager().addListener(this);
            client = Bot.client();
        }
        if (Walking.getEnergy() >= 50 && !Walking.isRunEnabled()) {
            Walking.setRun(true);
        }
        if (Camera.getPitch() < 60 || Camera.getPitch() > 90) {
            Camera.setPitch(80);
        }
        if (Camera.getYaw() < 220 || Camera.getYaw() > 245) {
            Camera.setAngle(235);
        }
        if (((ss.getElapsed() / 1000) - ssTimer) > 3600) {
            Utilities.savePaint(0, 0, 765, 48);
            ssTimer = System.currentTimeMillis();
        }
        return 25;
    }

    @Override
    public void onRepaint(Graphics g1) {
        if (Game.isLoggedIn()) {
            runTime = System.currentTimeMillis() - startTime;
            int expGained = Skills.getExperience(Skills.CRAFTING) - startExp;
            int levelsGained = Skills.getRealLevel(Skills.CRAFTING) - startLevel;
            int expHour = (int) ((3600000.0 / runTime) * expGained);
            int craftHour = (int) ((3600000.0 / runTime) * crafted);
            int profit = crafted * (braceletPrice - goldPrice);
            int profitHour = (int) ((3600000.0 / runTime) * profit);
            int expTNL = Skills.getExperienceRequired(Skills.getRealLevel(Skills.CRAFTING) + 1) - Skills.getExperience(Skills.CRAFTING);
            long timeTNL = (long) ((double) expTNL / (double) ((int) ((3600000.0 / runTime) * expGained)) * 3600000);

            Graphics2D g = (Graphics2D) g1;
            g.setRenderingHints(antialiasing);

            g.setColor(color1);
            g.fillRect(0, 0, 765, 48);
            g.setColor(color2);
            g.setStroke(stroke1);
            g.drawRect(0, 0, 765, 48);
            g.setFont(font1);
            g.drawString("BraceletCrafter", 5, 15);
            g.setFont(font2);
            if (levelsGained > 0) {
                g.drawString("Crafting Level:" + Skills.getRealLevel(Skills.CRAFTING) + " (+" + levelsGained + ") (" + Skills.getExperience(Skills.CRAFTING) + ")", 5, 30);
            } else {
                g.drawString("Crafting Level: " + Skills.getRealLevel(Skills.CRAFTING) + " (" + Skills.getExperience(Skills.CRAFTING) + ")", 5, 30);
            }
            g.drawString("EXP Gained: " + expGained + " (" + expHour + ")", 5, 45);
            g.drawString("Bracelets Banked: " + crafted + " (" + craftHour + ")", 219, 15);
            g.drawString("Profit Earned: " + profit + " (" + profitHour + ")", 219, 30);
            g.drawString("EXP TNL: " + expTNL, 219, 45);
            g.setFont(font3);
            g.drawString("by NathanTehBeast", 666, 45);
            g.drawString("Version: "+BraceletCrafter.class.getAnnotation(Manifest.class).version(), 666, 15);
            g.setFont(font2);
            g.drawString("Time Ran: " + Calculations.formatTime(runTime), 429, 15);
            g.drawString("Time TNL: " + Calculations.formatTime(timeTNL), 429, 45);
        }
    }
}
