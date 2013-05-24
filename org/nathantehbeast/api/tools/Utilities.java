package org.nathantehbeast.api.tools;


import org.nathantehbeast.api.framework.Condition;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Environment;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.widget.Bank;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.wrappers.Area;
import org.powerbot.game.api.wrappers.Tile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

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
        if (!Bank.isOpen()) {
            return false;
        }
        Arrays.sort(ids);
        for (int i : ids) {
            if (Arrays.binarySearch(ids, i) < 0) {
                final boolean b = Bank.deposit(i, 0);
                if (!b) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean contains(final int[] array, final int id) {
        for (final int i : array) {
            if (i == id) {
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
        final Timer t = new Timer(600);
        while (t.isRunning() && Players.getLocal().getAnimation() == -1) {
            sleep(50);
        }
        return Players.getLocal().getAnimation() == -1;
    }

    public static void provide(ArrayList<Node> list, Node... nodes) {
        for (Node node : nodes) {
            System.out.println("Providing: "+node);
            list.add(node);
        }
    }

    public static void loadFont(int type, String url) {
        try {
            URL fontUrl = new URL(url);
            Font font = Font.createFont(type, fontUrl.openStream());
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(font);
            System.out.println("Successfully registered Font: "+font.getFontName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
