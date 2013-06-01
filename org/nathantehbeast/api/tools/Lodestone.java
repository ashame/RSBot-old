package org.nathantehbeast.api.tools;

import org.powerbot.game.api.methods.Tabs;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.wrappers.Area;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.widget.Widget;
import org.powerbot.game.api.wrappers.widget.WidgetChild;
import sk.general.TimedCondition;

/**
 * Created with IntelliJ IDEA.
 * User: Nathan
 * Date: 5/28/13
 * Time: 11:29 PM
 * To change this template use File | Settings | File Templates.
 */

public class Lodestone {

    private static final Widget PARENT = new Widget(1092);
    private static final Widget TAB = new Widget(275);
    private static final WidgetChild MAGIC_TAB = TAB.getChild(33);
    private static final WidgetChild TELEPORTS = TAB.getChild(47);
    private static final WidgetChild HOME_TELEPORT = TAB.getChild(18).getChild(155);
    private static final WidgetChild INTERFACE = Widgets.get(1092, 8);

    /**
     * @return Whether the player is currently teleporting or not
     */
    public static boolean isTeleporting() {
        return Players.getLocal().getAnimation() == 16385;
    }

    /**
     * Teleports to your specified location.
     *
     * @param location Location of which to teleport to
     * @return Whether the player successfully teleported or not
     */
    public static boolean teleport(final Location location) {
        if (!isTeleporting()) {
            Tabs.ABILITY_BOOK.open();
            if (!TELEPORTS.validate()) {
                click(MAGIC_TAB, true);
                new TimedCondition(1000) {
                    @Override
                    public boolean isDone() {
                        return MAGIC_TAB.validate();
                    }
                }.waitStop();
            }
            if (!HOME_TELEPORT.validate()) {
                click(TELEPORTS, true);
                new TimedCondition(1000) {
                    @Override
                    public boolean isDone() {
                        return TELEPORTS.validate();
                    }
                }.waitStop();
            }
            if (!INTERFACE.validate()) {
                click(HOME_TELEPORT, true);
                new TimedCondition(1000) {
                    @Override
                    public boolean isDone() {
                        return INTERFACE.validate();
                    }
                }.waitStop();
            }
            if (INTERFACE.validate()) {
                click(location.getWidgetChild(), true);
                new TimedCondition(15000) {
                    @Override
                    public boolean isDone() {
                        return Players.getLocal().getAnimation() == -1 && location.getArea().contains(Players.getLocal());
                    }
                }.waitStop();
                Walking.walk(Players.getLocal().getLocation());
            }
        }
        return location.getArea().contains(Players.getLocal().getLocation());
    }

    /**
     * Clicks a random point within the bounding rectangle of a WidgetChild
     *
     * @param wc   The WidgetChild
     * @param left To left click or not to
     * @return Whether we successfully clicked the point or not
     */
    public static boolean click(WidgetChild wc, boolean left) {
        return Mouse.click(wc.getCentralPoint(), left);
    }

    public enum Location {
        LUNAR_ISLE(PARENT.getChild(39), new Area(new Tile(2080, 3918, 0), new Tile(2088, 3918, 0), new Tile(2088, 3910, 0), new Tile(2080, 3190, 0))),
        YANILLE(PARENT.getChild(52), new Area(new Tile(2521, 3101, 0), new Tile(2535, 3101, 0), new Tile(2535, 3085, 0), new Tile(2521, 3085, 0))),
        ARDOUGNE(PARENT.getChild(41), new Area(new Tile(2627, 3344, 0), new Tile(2638, 3344, 0), new Tile(2638, 3353, 0), new Tile(2627, 3353, 0))),
        SEERS(PARENT.getChild(49), new Area(new Tile(2682, 3487, 0), new Tile(2694, 3487, 0), new Tile(2694, 3476, 0), new Tile(2682, 3476, 0))),
        CATHERBY(PARENT.getChild(43), new Area(new Tile(2826, 3456, 0), new Tile(2836, 3456, 0), new Tile(2836, 3446, 0), new Tile(2826, 3446, 0))),
        BURTHROPE(PARENT.getChild(42), new Area(new Tile(2893, 3549, 0), new Tile(2904, 3549, 0), new Tile(2904, 3538, 0), new Tile(2893, 3538, 0))),
        TAVERLY(PARENT.getChild(50), new Area(new Tile(2872, 3447, 0), new Tile(2884, 3447, 0), new Tile(2884, 3435, 0), new Tile(2872, 3435, 0))),
        FALADOR(PARENT.getChild(46), new Area(new Tile(2960, 3407, 0), new Tile(2973, 3407, 0), new Tile(2973, 3397, 0), new Tile(2960, 3397, 0))),
        PORT_SARIM(PARENT.getChild(48), new Area(new Tile(3003, 3221, 0), new Tile(3017, 3221, 0), new Tile(3017, 3207, 0), new Tile(3003, 3207, 0))),
        EDGEVILLE(PARENT.getChild(45), new Area(new Tile(3061, 3510, 0), new Tile(3071, 3510, 0), new Tile(3071, 3499, 0), new Tile(3061, 3499, 0))),
        DRAYNOR(PARENT.getChild(44), new Area(new Tile(3099, 3303, 0), new Tile(3109, 3303, 0), new Tile(3109, 3293, 0), new Tile(3099, 3293, 0))),
        VARROCK(PARENT.getChild(51), new Area(new Tile(3208, 3381, 0), new Tile(3220, 3381, 0), new Tile(3220, 3369, 0), new Tile(3208, 3369, 0))),
        AL_KHARID(PARENT.getChild(40), new Area(new Tile(3291, 3180, 0), new Tile(3301, 3180, 0), new Tile(3301, 3189, 0), new Tile(3291, 3189, 0))),
        LUMBRIDGE(PARENT.getChild(47), new Area(new Tile(3228, 3226, 0), new Tile(3236, 3226, 0), new Tile(3236, 3216, 0), new Tile(3228, 3216, 0))),
        BANDIT_CAMP(PARENT.getChild(7), new Area(new Tile(3208, 2959, 0), new Tile(3219, 2959, 0), new Tile(3219, 2948, 0), new Tile(3208, 2948, 0)));

        private WidgetChild child;
        private Area area;

        Location(final WidgetChild child, final Area area) {
            this.child = child;
            this.area = area;
        }

        /**
         * @return The WidgetChild of the lodestone in the interface.
         */
        public WidgetChild getWidgetChild() {
            return child;
        }

        /**
         * @return The area containing the lodestone
         */
        public Area getArea() {
            return area;
        }
    }
}
