package org.nathantehbeast.scripts.aiominer;


import org.nathantehbeast.api.tools.Utilities;
import org.nathantehbeast.scripts.aiominer.nodes.Drop;
import org.nathantehbeast.scripts.aiominer.nodes.Mine;
import org.powerbot.core.Bot;
import org.powerbot.core.event.events.MessageEvent;
import org.powerbot.core.event.listeners.MessageListener;
import org.powerbot.core.event.listeners.PaintListener;
import org.powerbot.core.script.ActiveScript;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.core.script.methods.Game;
import org.powerbot.game.api.Manifest;
import org.powerbot.game.api.methods.input.Keyboard;
import org.powerbot.game.api.methods.widget.WidgetCache;
import org.powerbot.game.client.Client;

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
        authors         = "NathanTehBeast",
        name            = "Nathan's AIO Miner",
        description     = "AIO Miner, Alpha testing phase. Powermining only for now; banking support to be added later.",
        hidden          = true,
        vip             = false,
        singleinstance  = false,
        version         = 1.2
)

public final class Main extends ActiveScript implements MessageListener, PaintListener {

    private static boolean powermine = true;
    private static Constants.Ore ore;
    public static boolean start = false;
    private final double version = getClass().getAnnotation(Manifest.class).version();
    private final String user = Bot.context().getDisplayName();
    private Client client;
    private Node currentNode = null;
    public static ArrayList<Node> nodes = new ArrayList<>();

    @Override
    public void onStart() {
        System.out.println("Welcome " + user);
        System.out.println("You are using version " + version);
        new GUI();
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
        Utilities.provide(nodes, new Mine(), new Drop());
    }

    @Override
    public int loop() {
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

    @Override
    public void messageReceived(MessageEvent me) {
        if (me.getMessage().toLowerCase().contains("is kenneh gay?")) {
            Keyboard.sendText("Ain't nobody got time fo' dat", true);
            stop();
        }
    }

    @Override
    public void onRepaint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        if (currentNode != null) {
            g2d.drawString("Current node: "+currentNode, 5, 100);
        }
    }
}
