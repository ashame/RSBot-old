package org.nathantehbeast.scripts.braceletCrafter.Nodes;


import org.nathantehbeast.api.framework.Condition;
import org.nathantehbeast.api.tools.Utilities;
import org.nathantehbeast.scripts.braceletCrafter.BraceletCrafter;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Bank;
import org.powerbot.game.api.wrappers.Entity;

public class BankItems extends Node {
    //Scene Entities

    private final int GOLD_ID = 2357;
    private final int BRACELET_ID = 11069;

    @Override
    public boolean activate() {
        Entity bank = Bank.getNearest();
        return (Inventory.isFull() && !Inventory.contains(GOLD_ID)) && bank != null;
    }

    @Override
    public void execute() {
        Entity bank = Bank.getNearest();
        if (bank != null && Bank.open()) {
            BraceletCrafter.crafted += Inventory.getCount(BRACELET_ID);
            if (Bank.depositInventory()) {
                Utilities.waitFor(new Condition() {
                    @Override
                    public boolean validate() {
                        return !Inventory.contains(BRACELET_ID);
                    }
                }, 2000);
                if (Bank.withdraw(GOLD_ID, Bank.Amount.ALL)) {
                    Utilities.waitFor(new Condition() {
                        @Override
                        public boolean validate() {
                            return Inventory.contains(GOLD_ID);
                        }
                    }, 2000);
                    Bank.close();
                }
            }
        }
    }
}