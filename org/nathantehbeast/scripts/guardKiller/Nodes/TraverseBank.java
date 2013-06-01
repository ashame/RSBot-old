package org.nathantehbeast.scripts.guardKiller.Nodes;

import org.nathantehbeast.api.framework.XNode;
import org.powerbot.core.script.methods.Players;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.wrappers.Area;
import org.powerbot.game.api.wrappers.map.TilePath;

/**
 * Created with IntelliJ IDEA.
 * User: Nathan
 * Date: 5/30/13
 * Time: 11:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class TraverseBank implements XNode {

    private final TilePath path;
    private final Area bankArea;
    private final int foodId;

    public TraverseBank(final TilePath path, final Area bankArea, final int foodId) {
        this.path = path;
        this.bankArea = bankArea;
        this.foodId = foodId;
    }

    @Override
    public boolean activate() {
        return (Inventory.isFull() || Players.getLocal().getHealthPercent() < 50) && !(foodId > 0 && Inventory.contains(foodId));
    }

    @Override
    public void execute() {
        path.traverse();
    }
}
