package org.nathantehbeast.scripts.aiominer.nodes;

import org.nathantehbeast.api.framework.XNode;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.util.Filter;
import org.powerbot.game.api.wrappers.Area;
import org.powerbot.game.api.wrappers.map.TilePath;
import org.powerbot.game.api.wrappers.node.Item;

/**
 * Created with IntelliJ IDEA.
 * User: Nathan
 * Date: 5/30/13
 * Time: 4:14 AM
 * To change this template use File | Settings | File Templates.
 */
public class WalkToOres implements XNode {

    private final TilePath rockPath;
    private final Area rockArea;

    final Filter<Item> FILTER = new Filter<Item>() {
        @Override
        public boolean accept(Item item) {
            return !item.getName().toLowerCase().contains("pickaxe") && !item.getName().toLowerCase().contains("adze");
        }
    };

    public WalkToOres(TilePath rockPath, Area rockArea) {
        this.rockPath = rockPath;
        this.rockArea = rockArea;
    }

    @Override
    public boolean activate() {
        return Inventory.getItems(FILTER).length == 0 && !rockArea.contains(Players.getLocal().getLocation());
    }

    @Override
    public void execute() {
        rockPath.traverse();
    }
}
