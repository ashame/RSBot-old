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
        COPPER      (436, 437, "Copper ore rocks", new BankLocations[]{BankLocations.YANILLE, BankLocations.FALADOR, BankLocations.YANILLE},
                /* Al Kharid */     11937, 11936,
                /* Varrock East */  11960, 11961, 11962,
                /* Rimmington */    72098, 72099, 72100,
                /* Lumbridge */     3027, 3229,
                /* Yanille */       11937,
                /* Edgeville */     29230, 29231),
        TIN         (438, 439, "Tin ore rocks", new BankLocations[]{BankLocations.VARROCK_WEST, BankLocations.FALADOR, BankLocations.YANILLE},
                /* Al Kharid */     11933,
                /* Varrock West */  11957, 11958, 11959,
                /* Rimmington */    72092, 72094,
                /* Lumbridge */     3038, 3245,
                /* Barbarian */     11933, 11934, 11935,
                /* Yanille */       11933, 11934, 11935,
                /* Edgeville */     29227, 29229),
        CLAY        (434, 435, "Clay rocks", new BankLocations[]{BankLocations.VARROCK_WEST, BankLocations.FALADOR, BankLocations.YANILLE},
                /* Varrock West */  15504, 15505,
                /* Rimmington */    72075, 72077,
                /* Yanille */       10577, 10578),
        IRON        (440, 441, "Iron ore rocks", new BankLocations[]{BankLocations.YANILLE, BankLocations.VARROCK_WEST, BankLocations.FALADOR},
                /* Al Kharid */     37309, 37308, 37307,
                /* Varrock West */  11956, 11954,
                /* Varrock East */  11955,
                /* Rimmington */    72081, 72082, 72083,
                /* Yanille */       37307, 37308, 37309,
                /* Edgeville */     29221, 29222, 29223,
                /* Wilderness */    14856, 14857, 14858,
                /* NE Ardougne */   2092, 2093,
                /* SE Ardougne */   21281, 21282),
        SILVER      (442, 443, "Silver ore rocks", new BankLocations[]{BankLocations.VARROCK_WEST},
                /* Al Kharid */     37304, 37306,
                /* Varrock West */  11950, 11949, 11948,
                /* Fally Resource */29224, 29225, 29226,
                /* Edgeville */     229224, 29225, 29226),
        COAL        (453, 454, "Coal rocks", new BankLocations[]{BankLocations.YANILLE},
                /* Al Kharid */     11930, 11932,
                /* Draynor */       3233, 3032,
                /* Barbarian */     11930, 11931, 11932,
                /* Fally Resource */32426, 32427, 32428,
                /* Yanille */       11930,
                /* Edgeville */     29215, 29216, 29217,
                /* NE Ardougne */   2096, 2097,
                /* SE Ardougne */   21287,
                /* Coal Trucks */   14850, 14851),
        GOLD        (444, 445, "Gold ore rocks", null,
                /* Al Kharid */     37310, 37312,
                /* Brimhaven */     2098, 2099),
        MITHRIL     (447, 448, "Mithril ore rocks", new BankLocations[]{BankLocations.YANILLE},
                /* Al Kharid */     11942, 11944,
                /* Draynor */       3041, 3280,
                /* Fally Resource */32428, 32429, 332440,
                /* Yanille */       11942, 11943),
        ADAMANTITE  (449, 450, "Adamantite ore rocks", null,
                /* Al Kharid */     11939, 11941,
                /* Draynor */       3273, 3040,
                /* Edgeville */     29233, 29235);

        private final int id;
        private final int notedId;
        private final String name;
        private final int[] rocks;
        private final BankLocations[] banks;


        Ore(final int id, final int notedId, final String name, final BankLocations[] banks, final int... rocks) {
            this.id = id;
            this.notedId = id;
            this.name = name;
            this.banks = banks;
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

        public BankLocations[] getBanks() {
            return this.banks;
        }
    }

    public enum BankLocations {
        AL_KHARID(null, null, null, null),
        YANILLE(
        /* Bank Path */   new TilePath(new Tile[]{
                            new Tile(2626, 3140, 0), new Tile(2623, 3136, 0), new Tile(2622, 3131, 0), new Tile(2622, 3126, 0), new Tile(2621, 3121, 0),
                            new Tile(2621, 3116, 0), new Tile(2618, 3112, 0), new Tile(2615, 3108, 0), new Tile(2612, 3104, 0), new Tile(2609, 3100, 0),
                            new Tile(2605, 3097, 0), new Tile(2604, 3092, 0), new Tile(2609, 3093, 0)
                          }),
        /* Rock Path */   new TilePath(new Tile[]{
                            new Tile(2610, 3093, 0), new Tile(2605, 3093, 0), new Tile(2605, 3098, 0), new Tile(2609, 3101, 0), new Tile(2613, 3104, 0),
                            new Tile(2616, 3108, 0), new Tile(2618, 3113, 0), new Tile(2620, 3118, 0), new Tile(2621, 3123, 0), new Tile(2622, 3128, 0),
                            new Tile(2623, 3133, 0), new Tile(2625, 3138, 0), new Tile(2625, 3142, 0)
                          }),
        /* Bank Area */     new Area(new Tile(2607, 3097, 0), new Tile(2616, 3097, 0), new Tile(2616, 3087, 0), new Tile(2607, 3087, 0)),
        /* Rock Area */     new Area(new Tile(2620, 3138, 0), new Tile(2632, 3138, 0), new Tile(2632, 3147, 0), new Tile(2620, 3147, 0))),
        VARROCK_WEST    (new TilePath(new Tile[] {
        /* Bank Path */     new Tile(3178, 3367, 0), new Tile(3176, 3362, 0), new Tile(3171, 3361, 0), new Tile(3167, 3364, 0), new Tile(3166, 3369, 0),
                            new Tile(3168, 3374, 0), new Tile(3169, 3379, 0), new Tile(3170, 3384, 0), new Tile(3170, 3389, 0), new Tile(3170, 3394, 0),
                            new Tile(3170, 3399, 0), new Tile(3170, 3403, 0), new Tile(3170, 3408, 0), new Tile(3170, 3413, 0), new Tile(3171, 3418, 0),
                            new Tile(3171, 3423, 0), new Tile(3171, 3428, 0), new Tile(3176, 3429, 0), new Tile(3181, 3429, 0), new Tile(3185, 3432, 0),
                            new Tile(3185, 3436, 0)
                          }),
        /* Rock Path */   new TilePath(new Tile[] {
                            new Tile(3184, 3435, 0), new Tile(3184, 3430, 0), new Tile(3179, 3428, 0), new Tile(3174, 3427, 0), new Tile(3170, 3424, 0),
                            new Tile(3169, 3419, 0), new Tile(3169, 3414, 0), new Tile(3169, 3409, 0), new Tile(3169, 3404, 0), new Tile(3170, 3399, 0),
                            new Tile(3170, 3394, 0), new Tile(3169, 3389, 0), new Tile(3170, 3384, 0), new Tile(3170, 3379, 0), new Tile(3169, 3374, 0),
                            new Tile(3169, 3369, 0), new Tile(3170, 3364, 0), new Tile(3174, 3361, 0), new Tile(3178, 3364, 0), new Tile(3179, 3369, 0)
                          }),
        /* Bank Area */   new Area(new Tile(3177, 3448, 0), new Tile(3195, 3448, 0), new Tile(3195, 3430, 0), new Tile(3177, 3430, 0)),
        /* Rock Area */   new Area( new Tile(3177, 3380, 0), new Tile(3186, 3380, 0), new Tile(3183, 3369, 0), new Tile(3177, 3363, 0), new Tile(3168, 3363, 0))),
        FALADOR         (new TilePath(new Tile[] {
                            new Tile(2973, 3240, 0), new Tile(2973, 3245, 0), new Tile(2973, 3250, 0), new Tile(2973, 3255, 0), new Tile(2974, 3260, 0),
                            new Tile(2975, 3265, 0), new Tile(2975, 3270, 0), new Tile(2977, 3275, 0), new Tile(2979, 3280, 0), new Tile(2983, 3283, 0),
                            new Tile(2987, 3286, 0), new Tile(2991, 3289, 0), new Tile(2996, 3291, 0), new Tile(3001, 3293, 0), new Tile(3006, 3295, 0),
                            new Tile(3005, 3300, 0), new Tile(3006, 3305, 0), new Tile(3006, 3310, 0), new Tile(3006, 3315, 0), new Tile(3007, 3320, 0),
                            new Tile(3007, 3325, 0), new Tile(3007, 3330, 0), new Tile(3007, 3335, 0), new Tile(3006, 3340, 0), new Tile(3007, 3345, 0),
                            new Tile(3007, 3350, 0), new Tile(3007, 3355, 0), new Tile(3006, 3360, 0), new Tile(3010, 3363, 0), new Tile(3013, 3359, 0),
                            new Tile(3013, 3355, 0)
                        }),
                        new TilePath(new Tile[] {
                                new Tile(3011, 3356, 0), new Tile(3011, 3361, 0), new Tile(3006, 3361, 0), new Tile(3006, 3356, 0), new Tile(3007, 3351, 0),
                                new Tile(3007, 3346, 0), new Tile(3007, 3341, 0), new Tile(3006, 3336, 0), new Tile(3006, 3331, 0), new Tile(3005, 3326, 0),
                                new Tile(3005, 3321, 0), new Tile(3004, 3316, 0), new Tile(3001, 3312, 0), new Tile(2998, 3308, 0), new Tile(2995, 3304, 0),
                                new Tile(2992, 3300, 0), new Tile(2988, 3297, 0), new Tile(2985, 3293, 0), new Tile(2985, 3288, 0), new Tile(2983, 3283, 0),
                                new Tile(2979, 3280, 0), new Tile(2976, 3276, 0), new Tile(2974, 3271, 0), new Tile(2973, 3266, 0), new Tile(2974, 3261, 0),
                                new Tile(2972, 3256, 0), new Tile(2970, 3251, 0), new Tile(2971, 3246, 0), new Tile(2971, 3241, 0), new Tile(2973, 3239, 0)
                        }),
                        new Area(new Tile(3007, 3359, 0), new Tile(3007, 3352, 0), new Tile(3021, 3352, 0), new Tile(3021, 3357, 0), new Tile(3018, 3357, 0), new Tile(3018, 3359, 0) ),
                        new Area(new Tile(2968, 3243, 0), new Tile(2980, 3243, 0), new Tile(2983, 3234, 0), new Tile(2977, 3227, 0), new Tile(2965, 3227, 0), new Tile(2962, 3236, 0))),
        UNSUPPORTED(null, null, null, null);

        private final TilePath bankPath;
        private final TilePath rockPath;
        private final Area bankArea;
        private final Area rockArea;

        BankLocations(final TilePath bankPath, final TilePath rockPath, final Area bankArea, final Area rockArea) {
            this.bankPath = bankPath;
            this.rockPath = rockPath;
            this.bankArea = bankArea;
            this.rockArea = rockArea;
        }

        public Area getRockArea() {
            return this.rockArea;
        }

        public Area getBankArea() {
            return this.bankArea;
        }

        public TilePath getBankPath() {
            return this.bankPath;
        }

        public TilePath getRockPath() {
            return this.rockPath;
        }
    }
}
