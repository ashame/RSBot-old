package org.nathantehbeast.scripts.wineGrabber.Nodes;

import org.nathantehbeast.api.framework.XNode;
import org.nathantehbeast.api.tools.Lodestone;
import org.nathantehbeast.scripts.wineGrabber.Constants;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.tab.Inventory;

/**
 * Created with IntelliJ IDEA.
 * User: Nathan
 * Date: 5/28/13
 * Time: 11:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class TraverseBank implements XNode {

    @Override
    public boolean activate() {
        return Inventory.isFull() && !Constants.BANK.contains(Players.getLocal().getLocation());
    }

    @Override
    public void execute() {
        if (Constants.TEMPLE.contains(Players.getLocal().getLocation())) {
            Lodestone.teleport(Lodestone.Location.FALADOR);
        } else {
            Constants.LODESTONE_TO_BANK.traverse();
        }

    }
}
