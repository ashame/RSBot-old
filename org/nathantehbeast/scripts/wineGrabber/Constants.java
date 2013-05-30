package org.nathantehbeast.scripts.wineGrabber;

import org.powerbot.game.api.wrappers.Area;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.map.TilePath;

/**
 * Created with IntelliJ IDEA.
 * User: Nathan
 * Date: 5/28/13
 * Time: 10:42 PM
 * To change this template use File | Settings | File Templates.
 */
public final class Constants {

    public static final int WINE_ID = 245;
    public static final int LAW_ID = 563;
    public static final int TABLE_ID = 78230;

    public static final Area BANK = new Area(
            new Tile(2940, 3375, 0), new Tile(2940, 3358, 0), new Tile(2950, 3358, 0), new Tile(2950, 3375, 0)
    );
    public static final Area TEMPLE = new Area(
            new Tile(2944, 3479, 0), new Tile(2944, 3472, 0), new Tile(2956, 3472, 0), new Tile(2956, 3479, 0)
    );

    public static final TilePath BANK_TO_TEMPLE = new TilePath(new Tile[] {
            new Tile(2945, 3370, 0), new Tile(2944, 3375, 0), new Tile(2949, 3376, 0), new Tile(2952, 3380, 0), new Tile(2957, 3381, 0), new Tile(2962, 3383, 0),
            new Tile(2964, 3388, 0), new Tile(2964, 3393, 0), new Tile(2966, 3398, 0), new Tile(2966, 3403, 0), new Tile(2965, 3408, 0), new Tile(2963, 3413, 0),
            new Tile(2959, 3416, 0), new Tile(2954, 3417, 0), new Tile(2951, 3421, 0), new Tile(2948, 3425, 0), new Tile(2947, 3430, 0), new Tile(2946, 3435, 0),
            new Tile(2946, 3440, 0), new Tile(2945, 3445, 0), new Tile(2946, 3450, 0), new Tile(2946, 3455, 0), new Tile(2949, 3459, 0), new Tile(2948, 3464, 0),
            new Tile(2948, 3469, 0), new Tile(2953, 3470, 0), new Tile(2956, 3474, 0), new Tile(2951, 3476, 0), new Tile(2951, 3474, 0)
    });

    public static final TilePath LODESTONE_TO_BANK = new TilePath(new Tile[] {
            new Tile(2965, 3401, 0), new Tile(2964, 3396, 0), new Tile(2965, 3391, 0), new Tile(2964, 3386, 0), new Tile(2961, 3382, 0), new Tile(2956, 3381, 0),
            new Tile(2951, 3380, 0), new Tile(2948, 3376, 0), new Tile(2945, 3372, 0), new Tile(2945, 3367, 0)
    });

    public static final TilePath ESCAPE_TEMPLE = new TilePath(new Tile[] {
            new Tile(2949, 3475, 0), new Tile(2954, 3475, 0), new Tile(2955, 3470, 0), new Tile(2950, 3469, 0), new Tile(2947, 3465, 0), new Tile(2947, 3460, 0),
            new Tile(2948, 3455, 0), new Tile(2948, 3452, 0)
    });

    public static final TilePath RETURN_TO_TEMPLE = new TilePath(new Tile[] {
            new Tile(2948, 3454, 0), new Tile(2948, 3459, 0), new Tile(2948, 3464, 0), new Tile(2952, 3467, 0), new Tile(2956, 3470, 0), new Tile(2957, 3475, 0),
            new Tile(2952, 3476, 0), new Tile(2951, 3474, 0)
    });

    public static final Tile F_TILE = new Tile(2966, 3392, 0);;
}
