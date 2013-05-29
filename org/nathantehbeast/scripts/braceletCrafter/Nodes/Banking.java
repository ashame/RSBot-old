package org.nathantehbeast.scripts.braceletCrafter.Nodes;

import org.nathantehbeast.api.framework.Condition;
import org.nathantehbeast.api.framework.XNode;
import org.nathantehbeast.api.tools.Utilities;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Bank;
import org.powerbot.game.api.wrappers.Area;
import org.powerbot.game.api.wrappers.Entity;

/**
 * Created with IntelliJ IDEA.
 * User: Nathan
 * Date: 5/26/13
 * Time: 4:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class Banking implements XNode {

    private int goldId;
    private int braceletId;
    private Area bankArea;
    private Entity bank;

    public Banking(int goldId, int braceletId, Area bankArea) {
        this.goldId = goldId;
        this.braceletId = braceletId;
        this.bankArea = bankArea;
    }

    private final Condition DEPOSITED = new Condition() {
        @Override
        public boolean validate() {
            return !Inventory.contains(braceletId);
        }
    };

    private final Condition HAS_GOLD = new Condition() {
        @Override
        public boolean validate() {
            return Inventory.contains(goldId);
        }
    };

    @Override
    public boolean activate() {
        return (bank = Bank.getNearest()) != null && !Inventory.contains(goldId) && bankArea.contains(Players.getLocal().getLocation());
    }

    @Override
    public void execute() {
        if (Bank.open()) {
            if (Bank.depositInventory()) {
                Utilities.waitFor(DEPOSITED, 5000);
                if (Bank.withdraw(goldId, Bank.Amount.ALL)) {
                    Utilities.waitFor(HAS_GOLD, 5000);
                    Bank.close();
                }
            }
        }
    }

}
