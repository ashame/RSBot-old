package org.nathantehbeast.scripts.wineGrabber;

import org.nathantehbeast.api.framework.Condition;
import org.nathantehbeast.api.framework.XScript;
import org.nathantehbeast.api.tools.Calc;
import org.nathantehbeast.api.tools.Logger;
import org.nathantehbeast.api.tools.Utilities;
import org.nathantehbeast.scripts.wineGrabber.Nodes.*;
import org.powerbot.core.event.events.MessageEvent;
import org.powerbot.core.event.listeners.MessageListener;
import org.powerbot.core.script.Script;
import org.powerbot.core.script.job.LoopTask;
import org.powerbot.game.api.Manifest;
import org.powerbot.game.api.methods.Environment;
import org.powerbot.game.api.methods.Game;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.input.Keyboard;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.tab.Skills;
import org.powerbot.game.api.util.SkillData;
import org.powerbot.game.api.util.Time;
import org.powerbot.game.bot.Context;
import sk.action.ActionBar;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: Nathan
 * Date: 5/28/13
 * Time: 10:40 PM
 * To change this template use File | Settings | File Templates.
 */

@Manifest(
        authors = "nathantehbeast",
        name = "Nathan's Wine Grabber [P]",
        description = "Grabs wines for profit. Unlimited instances. Money making",
        version = 1.0,
        hidden = true,
        instances = 0,
        topic = 1021905,
        website = "http://www.powerbot.org/community/topic/1021905-vip-f2p-nathans-wine-grabber/"
)

public class PrivateWineGrabber extends XScript implements Script, MessageListener {

    private SkillData sd;
    private int a;
    private int winesGrabbed;
    private int winesMissed;
    private int winePrice;
    private int lawPrice;
    private int spellsCasted;
    private int netGain;
    private long startTime;
    private static final Color black = Color.BLACK;
    private static final BasicStroke stroke = new BasicStroke(2);
    private int world = Integer.parseInt(JOptionPane.showInputDialog(null, "Relog World", "Enter the number of the world to relog on.", JOptionPane.PLAIN_MESSAGE));

    @Override
    protected boolean setup() {
        try {
            Logger.log("Starting up...");
            Logger.log("Welcome "+Environment.getDisplayName());
            startTime = System.currentTimeMillis();
            sd = new SkillData();
            a = Inventory.getCount(Constants.WINE_ID);
            winePrice = Calc.getPrice(Constants.WINE_ID);
            Logger.log("Current wine price: "+winePrice);
            lawPrice = Calc.getPrice(Constants.LAW_ID);
            Logger.log("Current law price: "+lawPrice);
            netGain = winePrice - lawPrice;
            Logger.log("Projected profit per grab: "+(winePrice - lawPrice));
            getContainer().submit(new LoopTask() {
                @Override
                public int loop() {
                    if (sd.experience(Skills.MAGIC) > a) {
                        spellsCasted++;
                        a = sd.experience(Skills.MAGIC);
                    }
                    winesGrabbed = spellsCasted - winesMissed;
                    return 600;
                }
            });
            Context.setLoginWorld(world);
            Logger.log("Relogging on world: " + world);
            Mouse.setSpeed(Mouse.Speed.VERY_FAST);

            provide(new BankItems());
            provide(new EscapeCombat());
            provide(new GrabWines());
            provide(new TraverseBank());
            provide(new TraverseTemple());
            delay = 25;
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void paint(Graphics g) {
        long runTime = System.currentTimeMillis() - startTime;
        int profit = winesGrabbed * netGain;
        int profitHour = (int) ((3600000.0 / runTime) * profit);
        int grabsHour = (int) ((3600000.0 / runTime) * winesGrabbed);
        int missHour = (int) ((3600000.0 / runTime) * winesMissed);

        Graphics2D g2d = (Graphics2D) g;
        if (currentNode != null)
            g2d.drawString("Current node: " + currentNode, 5, 85);
        g2d.drawString("Run Time: "+ Time.format(runTime), 5, 100);
        g2d.drawString("Wines Grabbed: "+winesGrabbed  + " ("+grabsHour+"/h)", 5, 115);
        g2d.drawString("Wines Missed: "+winesMissed+" ("+missHour+"/h)", 5, 130);
        g2d.drawString("Profit: "+profit+" ("+profitHour+"/h)", 5, 145);
        Point mouse = Mouse.getLocation();
        g2d.setColor(black);
        g2d.setStroke(stroke);
        g2d.drawLine(mouse.x, 0, mouse.x, 550);
        g2d.drawLine(0, mouse.y, 775, mouse.y);
    }

    @Override
    public void poll() {
        if (!Walking.isRunEnabled() && Walking.getEnergy() > 40) {
            Walking.setRun(true);
        }
    }

    @Override
    public void exit() {
    }

    @Override
    public void messageReceived(MessageEvent me) {
        String msg = me.getId() != 2 ? me.getMessage().toLowerCase() : "";
        if (msg.contains("it's gone")) {
            winesMissed++;
            Logger.log("Missed a wine!");
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
            shutdown();
        }
    }
}
