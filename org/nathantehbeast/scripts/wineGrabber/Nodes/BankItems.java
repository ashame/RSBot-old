package org.nathantehbeast.scripts.wineGrabber.Nodes;

import org.nathantehbeast.api.framework.Condition;
import org.nathantehbeast.api.framework.XNode;
import org.nathantehbeast.api.tools.Utilities;
import org.nathantehbeast.scripts.wineGrabber.Constants;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Bank;
import org.powerbot.game.api.wrappers.Entity;

/**
 * Created with IntelliJ IDEA.
 * User: Nathan
 * Date: 5/29/13
 * Time: 12:15 AM
 * To change this template use File | Settings | File Templates.
 */
public class BankItems implements XNode {

    private Entity bank;

    @Override
    public boolean activate() {
        return (bank = Bank.getNearest()) != null && Inventory.isFull() && Constants.BANK.contains(Players.getLocal().getLocation());
    }

    @Override
    public void execute() {
        if (Bank.open()) {
            Utilities.waitFor(new Condition() {
                @Override
                public boolean validate() {
                    return Bank.isOpen();
                }
            }, 5000);
            Bank.deposit(Constants.WINE_ID, Bank.Amount.ALL);
            Utilities.waitFor(new Condition() {
                @Override
                public boolean validate() {
                    return !Inventory.contains(Constants.WINE_ID);
                }
            }, 3000);
        }
    }
}
