package org.nathantehbeast.api.tools;


import org.nathantehbeast.api.framework.Condition;
import org.powerbot.game.api.methods.Environment;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Bank;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.wrappers.Area;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.node.Item;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.powerbot.core.script.job.Task.sleep;

/**
 * Created with IntelliJ IDEA.
 * User: Nathan
 * Date: 5/8/13
 * Time: 1:22 PM
 * To change this template use File | Settings | File Templates.
 */
public final class Utilities {

    public static boolean waitFor(final Condition c, final long minWait, final long maxWait) {
        final Timer t = new Timer(maxWait);
        while (t.isRunning() && t.getElapsed() > minWait && !c.validate()) {
            sleep(100);
        }
        return c.validate();
    }

    public static boolean waitFor(final Condition c, final long timeout) {
        final Timer t = new Timer(timeout);
        while (t.isRunning() && !c.validate()) {
            sleep(100);
        }
        return c.validate();
    }

    public static boolean waitFor(final boolean b, final long timeout) {
        final Timer t = new Timer(timeout);
        while (t.isRunning() && !b) {
            sleep(100);
        }
        return b;
    }

    public static void walkPath(final Tile[] path, final boolean randomize, final boolean reverse) {
        if (randomize) {
            if (reverse) {
                Walking.newTilePath(path).reverse().randomize(1, 3).traverse();
            } else {
                Walking.newTilePath(path).randomize(1, 3).traverse();
            }
        } else {
            if (reverse) {
                Walking.newTilePath(path).reverse().traverse();
            } else {
                Walking.newTilePath(path).traverse();
            }
        }
    }

    public static void openURL(final String url) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean savePaint(final int x, final int y, final int w, final int h) {
        final File path = new File(Environment.getStorageDirectory().getPath(), Calculations.longToDate(System.currentTimeMillis()) + ".png");
        final BufferedImage img = Environment.captureScreen().getSubimage(x, y, w, h);
        try {
            ImageIO.write(img, "png", path);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean depositAllExcept(final int... ids) {
        final List<Item> items = new ArrayList<>();
        for (final Item i : Inventory.getItems()) {
            if (!items.contains(i) && !includes(i.getId(), 0)) {
                items.add(i);
            }
        }
        for (final Item i : items) {
            if (Bank.deposit(i.getId(), Bank.Amount.ALL)) {
                items.remove(i);
            }
        }
        return items.size() == 0;
    }

    public static boolean includes(final int item, final int... ids) {
        for (final int i : ids) {
            if (i == item) {
                return true;
            }
        }
        return false;
    }

    public static final Image getImage(final String url) {
        try {
            return ImageIO.read(new URL(url));
        } catch (IOException e) {
            System.out.println(e);
            return null;
        }
    }

    public static boolean isAt(final Area a) {
        return a.contains(Players.getLocal().getLocation());
    }

    public static boolean isIdle() {
        final Timer t = new Timer(1500);
        while (t.isRunning()) {
            if (Players.getLocal().getAnimation() != -1) {
                t.reset();
            }
        }
        return Players.getLocal().getAnimation() == -1;
    }
}
