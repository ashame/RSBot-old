package org.nathantehbeast.scripts.braceletCrafter.Nodes;

import org.nathantehbeast.api.framework.XNode;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.wrappers.Area;
import org.powerbot.game.api.wrappers.Tile;

/**
 * Created with IntelliJ IDEA.
 * User: Nathan
 * Date: 5/26/13
 * Time: 4:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class WalkFurnace implements XNode {

    private int goldId;
    private Area furnaceArea;

    public WalkFurnace(int goldId, Area furnaceArea) {
        this.goldId = goldId;
        this.furnaceArea = furnaceArea;
    }


    @Override
    public boolean activate() {
        return Inventory.contains(goldId) && !furnaceArea.contains(Players.getLocal().getLocation());
    }

    @Override
    public void execute() {
        Walking.walk(new Tile(3108, 3501, 0));
    }
}
