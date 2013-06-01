package org.nathantehbeast.scripts.aiominer;

import org.powerbot.game.api.wrappers.Area;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.map.TilePath;

/**
 * Created with IntelliJ IDEA.
 * User: Nathan
 * Date: 5/11/13
 * Time: 4:22 AM
 * To change this template use File | Settings | File Templates.
 */

public final class Constants {

    public static final int[] COAL_DEPOSITS = {};
    public static final int[] GOLD_DEPOSITS = {};

    public static final int[] PICKAXES = {1265, 1267, 1269, 1271, 1273, 1275, 14099, 14107, 27082, 27083, 27084, 27085, 27086, 15259};

    public enum Ore {
        COPPER      (436, 437, "Copper ore rocks",
                /* Al Kharid */     11937, 11936,
                /* Varrock East */  11960, 11961, 11962,
                /* Rimmington */    72098, 72099, 72100,
                /* Lumbridge */     3027, 3229,
                /* Yanille */       11937,
                /* Edgeville */     29230, 29231),
        TIN         (438, 439, "Tin ore rocks",
                /* Al Kharid */     11933,
                /* Varrock West */  11957, 11958, 11959,
                /* Rimmington */    72092, 72094,
                /* Lumbridge */     3038, 3245,
                /* Barbarian */     11933, 11934, 11935,
                /* Yanille */       11933, 11934, 11935,
                /* Edgeville */     29227, 29229),
        CLAY        (434, 435, "Clay rocks",
                /* Varrock West */  15504, 15505,
                /* Rimmington */    72075, 72077,
                /* Yanille */       10577, 10578),
        IRON        (440, 441, "Iron ore rocks",
                /* Al Kharid */     37309, 37308, 37307,
                /* Varrock West */  11956, 11954,
                /* Varrock East */  11955,
                /* Rimmington */    72081, 72082, 72083,
                /* Yanille */       37307, 37308, 37309,
                /* Edgeville */     29221, 29222, 29223,
                /* Wilderness */    14856, 14857, 14858,
                /* NE Ardougne */   2092, 2093,
                /* SE Ardougne */   21281, 21282),
        SILVER      (442, 443, "Silver ore rocks",
                /* Al Kharid */     37304, 37306,
                /* Varrock West */  11950, 11949, 11948,
                /* Fally Resource */29224, 29225, 29226,
                /* Edgeville */     229224, 29225, 29226),
        COAL        (453, 454, "Coal rocks",
                /* Al Kharid */     11930, 11932,
                /* Draynor */       3233, 3032,
                /* Barbarian */     11930, 11931, 11932,
                /* Fally Resource */32426, 32427, 32428,
                /* Yanille */       11930,
                /* Edgeville */     29215, 29216, 29217,
                /* NE Ardougne */   2096, 2097,
                /* SE Ardougne */   21287,
                /* Coal Trucks */   14850, 14851),
        GOLD        (444, 445, "Gold ore rocks",
                /* Al Kharid */     37310, 37312,
                /* Brimhaven */     2098, 2099),
        MITHRIL     (447, 448, "Mithril ore rocks",
                /* Al Kharid */     11942, 11944,
                /* Draynor */       3041, 3280,
                /* Fally Resource */32428, 32429, 332440,
                /* Yanille */       11942, 11943),
        ADAMANTITE  (449, 450, "Adamantite ore rocks",
                /* Al Kharid */     11939, 11941,
                /* Draynor */       3273, 3040,
                /* Edgeville */     29233, 29235),
        YANILLE_IRON (440, 441, "Iron ore rocks",
                /* Bank Path */     new TilePath(new Tile[]{
                                        new Tile(2626, 3140, 0), new Tile(2623, 3136, 0), new Tile(2622, 3131, 0), new Tile(2622, 3126, 0), new Tile(2621, 3121, 0),
                                        new Tile(2621, 3116, 0), new Tile(2618, 3112, 0), new Tile(2615, 3108, 0), new Tile(2612, 3104, 0), new Tile(2609, 3100, 0),
                                        new Tile(2605, 3097, 0), new Tile(2604, 3092, 0), new Tile(2609, 3093, 0)
                                    }),
                /* Rock Path */     new TilePath(new Tile[]{
                                        new Tile(2610, 3093, 0), new Tile(2605, 3093, 0), new Tile(2605, 3098, 0), new Tile(2609, 3101, 0), new Tile(2613, 3104, 0),
                                        new Tile(2616, 3108, 0), new Tile(2618, 3113, 0), new Tile(2620, 3118, 0), new Tile(2621, 3123, 0), new Tile(2622, 3128, 0),
                                        new Tile(2623, 3133, 0), new Tile(2625, 3138, 0), new Tile(2625, 3142, 0)
                                    }),
                /* Bank Area */     new Area(new Tile(2607, 3097, 0), new Tile(2616, 3097, 0), new Tile(2616, 3087, 0), new Tile(2607, 3087, 0)),
                /* Rock Area */     new Area(new Tile(2620, 3138, 0), new Tile(2632, 3138, 0), new Tile(2632, 3147, 0), new Tile(2620, 3147, 0)),
                /* Rock IDs */      37307, 37308, 37309);

        private final int id;
        private final int notedId;
        private final String name;
        private final TilePath toBank;
        private final TilePath toRocks;
        private final Area bankArea;
        private final Area rockArea;
        private final int[] rocks;

        Ore(final int id, final int notedId, final String name, final int... rocks) {
            this(id, notedId, name, null, null, null, null, rocks);
        }

        Ore(final int id, final int notedId, final String name, final TilePath toBank, final TilePath toRocks, final Area bankArea, final Area rockArea, final int... rocks) {
            this.id = id;
            this.notedId = id;
            this.name = name;
            this.toBank = toBank;
            this.toRocks = toRocks;
            this.bankArea = bankArea;
            this.rockArea = rockArea;
            this.rocks = rocks;
        }

        public final boolean isNoted(int i) {
            for (final Ore ore : Ore.values()) {
                if (i == ore.notedId) {
                    return true;
                }
            }
            return false;
        }

        public int getId() {
            return this.id;
        }

        public int getNotedId() {
            return this.notedId;
        }

        public String getName() {
            return this.name;
        }

        public int[] getRocks() {
            return this.rocks;
        }

        public TilePath getBankPath() {
            return this.toBank;
        }

        public TilePath getRockPath() {
            return this.toRocks;
        }

        public Area getBankArea() {
            return this.bankArea;
        }

        public Area getRockArea() {
            return this.rockArea;
        }
    }
}
