package org.nathantehbeast.api.framework;

import org.nathantehbeast.api.tools.Logger;
import org.powerbot.core.Bot;
import org.powerbot.core.event.listeners.PaintListener;
import org.powerbot.core.script.ActiveScript;
import org.powerbot.core.script.job.LoopTask;
import org.powerbot.game.api.methods.Environment;
import org.powerbot.game.api.methods.Game;
import org.powerbot.game.api.methods.widget.WidgetCache;
import org.powerbot.game.client.Client;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Nathan
 * Date: 5/26/13
 * Time: 4:17 AM
 * To change this template use File | Settings | File Templates.
 */

public abstract class XScript extends ActiveScript implements PaintListener {

    private static final List<XNode> container = Collections.synchronizedList(new ArrayList<XNode>());
    private Client client;
    public XNode currentNode;
    public int delay = 600;
    private boolean logger = Environment.getDisplayName().toLowerCase().equals("nathantehbeast");

    public static synchronized final void provide(final XNode... nodes) {
        if (nodes != null ) {
            for (XNode node : nodes) {
                if (!container.contains(node)) {
                    container.add(node);
                    Logger.log("Providing: "+node);
                }
            }
        }
    }

    public static synchronized final void revoke(final XNode... nodes) {
        if (nodes != null) {
            for (XNode node : nodes) {
                if (container.contains(node)) {
                    container.remove(node);
                    Logger.log("Revoking: "+node);
                }
            }
        }
    }

    @Override
    public void onStart() {
        if (logger) {
            new Logger(new Font("Calibri", Font.PLAIN, 11));
            getContainer().submit(new LoopTask() {
                @Override
                public int loop() {
                    Logger.updateTime();
                    return 1000;
                }
            });
        }
        if (!setup()) {
            Logger.log("There was an error starting the script. Stopping.");
            try { Logger.remove(); } catch (Exception ignored) {}
            stop();
        }
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
            if (Game.isLoggedIn() && container != null ){
                synchronized (container) {
                    for (XNode node : container) {
                        if (node.activate()) {
                            node.execute();
                            currentNode = node;
                        }
                    }
                }
            }
            poll();
        } catch (Exception e) {
            System.out.println("Timer pls fix internal errors");
        }
        return delay;
    }

    @Override
    public void onStop() {
        if (logger)
            Logger.remove();
        exit();
    }

    @Override
    public void onRepaint(Graphics graphics) {
        paint(graphics);
    }

    /**
     * The setup method; run through once before anything else
     * @return Whether the methods executed successfully or not
     */
    protected abstract boolean setup();

    /**
     * onRepaint method
     * @param g Graphics
     */
    public abstract void paint(Graphics g);

    /**
     * Additional methods to be used in the loop
     */
    public abstract void poll();

    /**
     * The method to run through once on script stop
     */
    public abstract void exit();
}
