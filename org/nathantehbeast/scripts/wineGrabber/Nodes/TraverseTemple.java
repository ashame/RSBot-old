package org.nathantehbeast.scripts.wineGrabber.Nodes;

import org.nathantehbeast.api.framework.XNode;
import org.nathantehbeast.scripts.wineGrabber.Constants;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.tab.Inventory;

/**
 * Created with IntelliJ IDEA.
 * User: Nathan
 * Date: 5/29/13
 * Time: 12:05 AM
 * To change this template use File | Settings | File Templates.
 */
public class TraverseTemple implements XNode {
    @Override
    public boolean activate() {
        return !Inventory.isFull() && !Players.getLocal().isInCombat() && Inventory.contains(Constants.LAW_ID) && !Constants.TEMPLE.contains(Players.getLocal().getLocation());
    }

    @Override
    public void execute() {
        Constants.BANK_TO_TEMPLE.traverse();
    }
}
