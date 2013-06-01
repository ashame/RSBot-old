package org.nathantehbeast.scripts.aiominer.nodes;

import org.nathantehbeast.api.framework.XNode;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.wrappers.Area;
import org.powerbot.game.api.wrappers.map.TilePath;


/**
 * Created with IntelliJ IDEA.
 * User: Nathan
 * Date: 5/30/13
 * Time: 4:11 AM
 * To change this template use File | Settings | File Templates.
 */
public class WalkToBank implements XNode {

    private final TilePath toBank;
    private final Area bankArea;

    public WalkToBank(TilePath toBank, Area bankArea) {
        this.toBank = toBank;
        this.bankArea = bankArea;
    }

    @Override
    public boolean activate() {
        return Inventory.isFull() && !bankArea.contains(Players.getLocal().getLocation());
    }

    @Override
    public void execute() {
        toBank.traverse();
    }
}
