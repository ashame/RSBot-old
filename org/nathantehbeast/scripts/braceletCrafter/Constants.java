package org.nathantehbeast.scripts.braceletCrafter;

import org.powerbot.game.api.wrappers.Area;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.map.TilePath;

/**
 * Created with IntelliJ IDEA.
 * User: Nathan
 * Date: 5/26/13
 * Time: 3:56 PM
 * To change this template use File | Settings | File Templates.
 */
public final class Constants {
    public static final int GOLD_ID = 2357;
    public static final int BRACELET_ID = 11069;
    public static final int FURANCE_ID = 26814;

    public static final Area BANK_AREA = new Area(new Tile(3089, 3500, 0), new Tile(3089, 3487, 0), new Tile(3098, 3487, 0),
            new Tile(3098, 3500, 0));
    public static final Area FURNACE_AREA = new Area(new Tile(3104, 3503, 0), new Tile(3104, 3496, 0), new Tile(3111, 3496, 0),
            new Tile(3111, 3503, 0));

    public static final TilePath FURNACE_PATH = new TilePath(new Tile[]{new Tile(3095, 3497, 0), new Tile(3098, 3497, 0), new Tile(3101, 3498, 0),
            new Tile(3104, 3499, 0)});

    public static final TilePath BANK_PATH = new TilePath(new Tile[]{new Tile(3104, 3499, 0), new Tile(3101, 3498, 0), new Tile(3098, 3497, 0),
            new Tile(3095, 3497, 0)});
}
