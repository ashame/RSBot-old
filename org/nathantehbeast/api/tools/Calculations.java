package org.nathantehbeast.api.tools;

import org.powerbot.game.api.methods.Game;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.tab.Skills;
import org.powerbot.game.api.wrappers.Entity;
import org.powerbot.game.api.wrappers.widget.WidgetChild;

import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: Nathan
 * Date: 5/7/13
 * Time: 3:35 PM
 * To change this template use File | Settings | File Templates.
 */
public final class Calculations {

    /**
     *
     * @param skill ID of the skill to grab experience data for
     * @return Experience required in #skill to level.
     */
    public static int getRequiredExperience(int skill) {
        return Skills.getExperienceRequired(Skills.getRealLevel(skill) + 1) - Skills.getExperience(skill);
    }

    /**
     *
     * @param runTime Total elapsed runtime
     * @param x       The value to calculate perHour data for
     * @return        The amount of x gained per hour, based on current elapsed time (runTime)
     */
    public static int perHour(long runTime, int x) {
        return (int) ((3600000.0 / runTime) * x);
    }

    /**
     *
     * @param id Item ID of price to lookup
     * @return   The price of the item specified.
     */
    public static int getPrice(final int id) {
        try (final BufferedReader br = new BufferedReader(new InputStreamReader(
                new URL("http://scriptwith.us/api/?return=text&item=" + id).openConnection().getInputStream()))) {
            final String s = br.readLine();
            br.close();
            return Integer.parseInt(s.split("[:]")[1]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     *
     * @param ids Integer array of items to lookup
     * @return    The price of specified items in a Map by item ID, followed by price
     */
    public static Map<Integer, Integer> getPrices(final int... ids) {
        Map<Integer, Integer> map = new HashMap<>();
        String add = "http://scriptwith.us/api/?return=text&item=";
        for (int i = 0; i < ids.length; i++) {
            add += ((i == ids.length - 1) ? ids[i] : ids[i] + ",");
        }
        try (final BufferedReader in = new BufferedReader(new InputStreamReader(
                new URL(add).openConnection().getInputStream()))) {
            final String line = in.readLine();
            for (String s : line.split("[;]")) {
                map.put(Integer.parseInt(s.split("[:]")[0]), Integer.parseInt(s.split("[:]")[1]));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    public static HashMap<Integer, Integer> getPrice(final int... ids) {
        HashMap<Integer, Integer> map = new HashMap<>();
        String add = "http://scriptwith.us/api/?return=text&item=";
        for (int i = 0; i < ids.length; i++) {
            add += ((i == ids.length - 1) ? ids[i] : ids[i] + ",");
        }
        try (final BufferedReader in = new BufferedReader(new InputStreamReader(
                new URL(add).openConnection().getInputStream()))) {
            final String line = in.readLine();
            for (String s : line.split("[;]")) {
                map.put(Integer.parseInt(s.split("[:]")[0]), Integer.parseInt(s.split("[:]")[1]));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     *
     * @param x Time to format, in milliseconds
     * @return  Formatted time, in a string
     */
    public static String formatTime(final long x) {
        String s;
        long days = TimeUnit.MILLISECONDS.toDays(x);
        long hours = TimeUnit.MILLISECONDS.toHours(x) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(x));
        long minutes = TimeUnit.MILLISECONDS.toMinutes(x) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(x));
        long seconds = TimeUnit.MILLISECONDS.toSeconds(x) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(x));
        String d = String.valueOf(days);
        String h = String.valueOf(hours);
        String m = String.valueOf(minutes);
        String sec = String.valueOf(seconds);
        if (days < 10) {
            d = ("0" + String.valueOf(days));
        }
        if (hours < 10) {
            h = ("0" + String.valueOf(hours));
        }
        if (minutes < 10) {
            m = ("0" + String.valueOf(minutes));
        }
        if (seconds < 10) {
            sec = ("0" + String.valueOf(seconds));
        }
        if (days == 0) {
            s = (h + ":" + m + ":" + sec);
        } else if (days == 0 && hours == 0) {
            s = (m + ":" + sec);
        } else if (days > 1000) {
            s = "Undefined";
        } else  {
            s = (d + ":" + h + ":" + m + ":" + sec);
        }
        return s;
    }

    /**
     *
     * @param l Long to convert to date format
     * @return  The date specified as l, in a string.
     */
    public static String longToDate(long l) {
        return new SimpleDateFormat("dd MMM yyyy h-m-s a").format(new Date(l));
    }

    /**
     *
     * @param e Entity to check
     * @return  Whether the entity is on screen or not; accounts for actionbar
     */
    public static boolean isOnScreen(final Entity e) {
        Rectangle screen = new Rectangle(new Point(0, 0), Game.getDimensions());
        WidgetChild actionBarWidget = Widgets.get(640, 6);
        Rectangle actionBar = actionBarWidget == null || !actionBarWidget.isOnScreen() ? null : actionBarWidget.getBoundingRectangle();
        for (Polygon p : e.getBounds()) {
            for (int i = 0; i < p.npoints; i++) {
                int x = p.xpoints[i], y = p.ypoints[i];
                if (screen.contains(x, y) && (actionBar == null || !actionBar.contains(x, y))) {
                    return true;
                }
            }
        }
        return false;
    }
}
