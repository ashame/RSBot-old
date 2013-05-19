package org.nathantehbeast.scripts.aiominer;

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
        COPPER      (436, 437, "Copper ore",
                /* Al Kharid */     11937, 11936,
                /* Varrock East */  11960, 11961, 11962,
                /* Rimmington */    72098, 72099, 72100,
                /* Lumbridge */     3027, 3229,
                /* Yanille */       11937),
        TIN         (438, 439, "Tin ore",
                /* Al Kharid */     11933,
                /* Varrock West */  11957, 11958, 11959,
                /* Rimmington */    72092, 72094,
                /* Lumbridge */     3038, 3245,
                /* Barbarian */     11933, 11934, 11935,
                /* Yanille */       11933, 11934, 11935),
        CLAY        (434, 435, "Clay",
                /* Varrock West */  15504, 15505,
                /* Rimmington */    72075, 72077,
                /* Yanille */       10577, 10578),
        IRON        (440, 441, "Iron ore",
                /* Al Kharid */     37309, 37308, 37307,
                /* Varrock West */  11956, 11954,
                /* Varrock East */  11955,
                /* Rimmington */    72081, 72082, 72083,
                /* Yanille */       37307, 37308, 37309),
        SILVER      (442, 443, "Silver ore",
                /* Al Kharid */     37304, 37306,
                /* Varrock West */  11950, 11949, 11948,
                /* Fally Resource */29224, 29225, 29226),
        COAL        (453, 454, "Coal",
                /* Al Kharid */     11930, 11932,
                /* Draynor */       3233, 3032,
                /* Barbarian */     11930, 11931, 11932,
                /* Fally Resource */32426, 32427, 32428,
                /* Yanille */       11930),
        GOLD        (444, 445, "Gold ore",
                /* Al Kharid */     37310, 37312),
        MITHRIL     (447, 448, "Mithril ore",
                /* Al Kharid */     11942, 11944,
                /* Draynor */       3041, 3280,
                /* Fally Resource */32428, 32429, 332440,
                /* Yanille */       11942, 11943),
        ADAMANTITE  (449, 450, "Adamantite ore",
                /* Al Kharid */     11939, 11941,
                /* Draynor */       3273, 3040);

        private final int id;
        private final int notedId;
        private final String name;
        private final int[] rocks;

        Ore(final int id, final int notedId, final String name, final int... rocks) {
            this.id = id;
            this.notedId = notedId;
            this.name = name;
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
    }
}
