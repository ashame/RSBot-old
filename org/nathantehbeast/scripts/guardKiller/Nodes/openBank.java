package org.nathantehbeast.scripts.guardKiller.Nodes;

import org.nathantehbeast.api.tools.MCamera;
import org.nathantehbeast.api.tools.Utilities;
import org.nathantehbeast.scripts.guardKiller.GuardKiller;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Calculations;
import org.powerbot.game.api.methods.Game;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Bank;
import org.powerbot.game.api.wrappers.Entity;
import org.powerbot.game.api.wrappers.Locatable;

import static org.nathantehbeast.scripts.guardKiller.GuardKiller.GRAPES;
import static org.nathantehbeast.scripts.guardKiller.GuardKiller.foodId;

public class openBank extends Node {

    @Override
    public boolean activate() {
        Entity bank = Bank.getNearest();
        if (bank != null && Game.getClientState() == Game.INDEX_MAP_LOADED) {
            if (Calculations.distanceTo((Locatable) bank) < 8 && !Bank.isOpen()) {
                if (Inventory.contains(GRAPES)) {
                    if (foodId > 0) {
                        if (Inventory.isFull() && !Inventory.contains(foodId)) {
                            return true;
                        }
                    }
                    if (foodId == 0) {
                        if (Inventory.isFull()) {
                            return true;
                        }
                    }
                    return true;
                }
                if (foodId > 0 && Inventory.getCount(foodId) == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void execute() {
        GuardKiller.currentNode = this;
        System.out.println("Opening Bank.");
        Entity bank = Bank.getNearest();
        if (bank != null) {
            if (org.nathantehbeast.api.tools.Calculations.isOnScreen(bank)) {
                try {
                    Bank.open();
                } catch (NullPointerException ne) {
                    System.out.println("Error while opening bank: " + ne.getMessage());
                }
                Utilities.waitFor(Bank.isOpen(), 5000);
            } else if (!org.nathantehbeast.api.tools.Calculations.isOnScreen(bank)) {
                MCamera.turnTo((Locatable) bank, 2);
            } else {
                Walking.findPath((Locatable) bank).traverse();
            }
        }
        bank = null;
    }
}