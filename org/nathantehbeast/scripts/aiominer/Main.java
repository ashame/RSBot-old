package org.nathantehbeast.scripts.aiominer;

import org.nathantehbeast.scripts.aiominer.nodes.Drop;
import org.nathantehbeast.scripts.aiominer.nodes.Mine;
import org.powerbot.core.Bot;
import org.powerbot.core.script.ActiveScript;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.core.script.job.state.Tree;
import org.powerbot.game.api.Manifest;
import org.powerbot.game.api.methods.widget.WidgetCache;
import org.powerbot.game.client.Client;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Nathan
 * Date: 5/11/13
 * Time: 3:52 AM
 * To change this template use File | Settings | File Templates.
 */

@Manifest(
        authors         = "NathanTehBeast",
        name            = "Nathan's AIO Miner",
        description     = "AIO Miner, Alpha testing phase. Powermining only for now; banking support to be added later.",
        hidden          = true,
        vip             = false,
        singleinstance  = false,
        version         = 1.1
)

public final class Main extends ActiveScript {

    private Tree jobContainer = null;
    private final List<Node> jobsCollection = Collections.synchronizedList(new ArrayList<Node>());
    private static boolean powermine;
    private static Constants.Ore ore;
    public static boolean start = false;
    private final double version = getClass().getAnnotation(Manifest.class).version();
    private final String user = Bot.context().getDisplayName();
    private static String status;
    private Client client;

    private synchronized final void provide(Node... jobs) {
        for (final Node job : jobs) {
            if (!jobsCollection.contains(job)) {
                jobsCollection.add(job);
            }
        }
        jobContainer = new Tree(jobsCollection.toArray(new Node[jobsCollection.size()]));
    }

    @Override
    public void onStart() {
        System.out.println("Welcome " + user);
        System.out.println("You are using version " + version);
         SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    GUI gui = new GUI();
                    if (!gui.isVisible()) {
                        gui.setVisible(true);
                    }
                } catch (Throwable t) {
                    System.out.println("Error loading GUI: "+t.getMessage());
                    t.printStackTrace();
                }
            }
         });
        while (!start) {
            sleep(600);
        }
        System.out.println("Mining: "+getOre());
        System.out.println("Ore ID: "+getOre().id);
        System.out.println("Rock IDs: ");
        for (int i : getOre().rocks) {
            System.out.println(Integer.toString(i));
        }
        System.out.println("Powermining: "+getPowermine());
        provide(new Mine());
        System.out.println("Providing Mine");
        provide(new Drop());
        System.out.println("Prodivindg Drop");
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
        return 600;
    }

    @Override
    public void onStop() {
        System.out.println("Thanks for using Nathan's AIO Miner!");
    }

    public static void setOre(Constants.Ore orex) {
        ore = orex;
    }

    public static Constants.Ore getOre() {
        return ore;
    }

    public static void setPowermine(boolean b) {
        powermine = b;
    }

    public static boolean getPowermine() {
        return powermine;
    }

    public static void setStatus(String s) {
        status = s;
    }
}
