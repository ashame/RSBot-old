package org.nathantehbeast.scripts.yanilleIron.Nodes;

import org.nathantehbeast.api.tools.Utilities;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.core.script.methods.Game;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.wrappers.Area;
import org.powerbot.game.api.wrappers.Tile;

import static org.nathantehbeast.scripts.yanilleIron.YanilleIron.PATH_Y;
import static org.nathantehbeast.scripts.yanilleIron.YanilleIron.itemFilter;

public class walkOresY extends Node {

    @Override
    public boolean activate() {
        return Game.getClientState() == Game.INDEX_MAP_LOADED && Inventory.getItems(itemFilter).length == 0 && !new Area(new Tile(2623, 3137, 0), new Tile(2633, 3145, 0)).contains(Players.getLocal().getLocation());
    }

    @Override
    public void execute() {
        Utilities.walkPath(PATH_Y, true, false);
    }
}