package org.nathantehbeast.scripts.guardKiller;

import org.powerbot.game.api.util.Filter;
import org.powerbot.game.api.wrappers.Area;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.interactive.NPC;
import org.powerbot.game.api.wrappers.map.TilePath;
import org.powerbot.game.api.wrappers.node.Item;

/**
 * Created with IntelliJ IDEA.
 * User: Nathan
 * Date: 5/30/13
 * Time: 9:49 PM
 * To change this template use File | Settings | File Templates.
 */
public final class Constants {

    public static final int GRAPE_ID = 1987;

    public static final Area CASTLE_AREA = new Area(
            new Tile(3200, 3468, 0), new Tile(3203, 3469, 0), new Tile(3205, 3471, 0), new Tile(3210, 3471, 0),
            new Tile(3218, 3471, 0), new Tile(3220, 3469, 0), new Tile(3221, 3467, 0), new Tile(3224, 3467, 0),
            new Tile(3224, 3461, 0), new Tile(3222, 3457, 0), new Tile(3213, 3457, 0), new Tile(3202, 3457, 0),
            new Tile(3199, 3459, 0), new Tile(3199, 3468, 0)
    );

    public static final Area BANK_AREA = new Area(
            new Tile(3177, 3448, 0), new Tile(3177, 3431, 0), new Tile(3194, 3431, 0), new Tile(3194, 3448, 0)
    );

    public static final TilePath BANK_PATH = new TilePath(new Tile[]{
            new Tile(3211, 3460, 0), new Tile(3211, 3455, 0), new Tile(3211, 3450, 0), new Tile(3211, 3444, 0),
            new Tile(3211, 3440, 0), new Tile(3211, 3435, 0), new Tile(3206, 3437, 0), new Tile(3202, 3441, 0),
            new Tile(3199, 3445, 0), new Tile(3195, 3448, 0), new Tile(3190, 3450, 0), new Tile(3185, 3449, 0),
            new Tile(3185, 3444, 0)
    });

    public static final TilePath CASTLE_PATH = new TilePath(new Tile[]{
            new Tile(3186, 3444, 0), new Tile(3186, 3449, 0), new Tile(3191, 3450, 0), new Tile(3195, 3447, 0),
            new Tile(3198, 3443, 0), new Tile(3202, 3440, 0), new Tile(3206, 3437, 0), new Tile(3211, 3435, 0),
            new Tile(3212, 3440, 0), new Tile(3212, 3445, 0), new Tile(3212, 3451, 0), new Tile(3212, 3456, 0),
            new Tile(3212, 3461, 0)
    });

    public static final Filter<NPC> GUARDS = new Filter<NPC>() {
        @Override
        public boolean accept(NPC npc) {
            return npc != null
                    && (npc.getId() == 5919 || npc.getId() == 5920)
                    && !npc.isInCombat()
                    && CASTLE_AREA.contains(npc.getLocation());
        }
    };

    public static final Filter<Item> BANK_FILTER = new Filter<Item>() {
        @Override
        public boolean accept(Item item) {
            return !item.getName().toLowerCase().contains("rune");
        }
    };
}
