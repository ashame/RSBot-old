package org.nathantehbeast.scripts.braceletCrafter.Nodes;

import org.nathantehbeast.api.framework.Condition;
import org.nathantehbeast.api.framework.XNode;
import org.nathantehbeast.api.tools.Utilities;
import org.nathantehbeast.scripts.braceletCrafter.Main;
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

    public Banking(int goldId, int braceletId, Area bankArea) {
        this.goldId = goldId;
        this.braceletId = braceletId;
        this.bankArea = bankArea;
    }

    @Override
    public boolean activate() {
        final Entity BANK = Bank.getNearest();
        return BANK != null && !Inventory.contains(goldId) && bankArea.contains(Players.getLocal().getLocation());
    }

    @Override
    public void execute() {
        final Entity BANK = Bank.getNearest();
        if (BANK != null && Bank.open()) {
            Main.addCrafted(Inventory.getCount(braceletId));
            if (Bank.depositInventory()) {
                Utilities.waitFor(new Condition() {
                    @Override
                    public boolean validate() {
                        return !Inventory.contains(braceletId);
                    }
                }, 2000);
                if (Bank.withdraw(goldId, Bank.Amount.ALL)) {
                    Utilities.waitFor(new Condition() {
                        @Override
                        public boolean validate() {
                            return Inventory.contains(goldId);
                        }
                    }, 2000);
                    Bank.close();
                }
            }
        }
    }

}
