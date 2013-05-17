package org.nathantehbeast.scripts.guardKiller.Nodes;

import org.nathantehbeast.api.tools.Utilities;
import org.powerbot.core.Bot;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Game;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Bank;
import org.powerbot.game.api.util.Filter;
import org.powerbot.game.api.wrappers.node.Item;
import sk.action.ActionBar;

import static org.nathantehbeast.scripts.guardKiller.GuardKiller.*;

public class bankItems extends Node {

    public static final Filter<Item> itemFilter = new Filter<Item>() {
        @Override
        public boolean accept(Item i) {
            if (i != null && !i.getName().toLowerCase().contains("rune")) {
                return true;
            }
            return false;
        }
    };

    @Override
    public boolean activate() {
        if (Game.getClientState() == Game.INDEX_MAP_LOADED) {
            if (foodId > 0 && !Inventory.contains(foodId) && Bank.isOpen()) {
                return true;
            }
            if (Inventory.contains(GRAPES) && Bank.isOpen()) {
                return true;
            }
            if (Inventory.isFull() && !Inventory.contains(foodId) && Bank.isOpen()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void execute() {
        System.out.println("Banking Items.");
        if (!Bank.isOpen()) {
            Bank.open();
        }
        if (Bank.isOpen()) {
            while (ActionBar.isOpen()) {
                ActionBar.expand(false);
                Utilities.waitFor(!ActionBar.isOpen(), 1500);
            }
            while (Inventory.getItems(itemFilter).length > 0) {
                Bank.deposit(GRAPES, Bank.Amount.ALL);
                for (Item i : Inventory.getItems(itemFilter)) {
                    if (i != null && !toBank.contains(i.getId())) {
                        toBank.add(i.getId());
                    }
                }
                for (int i = 0; i < toBank.size(); i++) {
                    if (Inventory.getItems(itemFilter).length == 0) {
                        break;
                    }
                    Bank.deposit(toBank.get(i), Bank.Amount.ALL);
                    Utilities.waitFor(!Inventory.contains(toBank.get(i)), 3000);
                    sleep(300);
                }
                Utilities.waitFor(!Inventory.contains(GRAPES), 2000);
            }
            if (foodId > 0 && !Inventory.contains(foodId)) {
                if (Bank.getItem(foodId) == null) {
                    System.out.println("Bot Stopped. Out of food.");
                    System.out.println("\007\007\007");
                    Bot.context().getScriptHandler().stop();
                }
                while (!Inventory.contains(foodId)) {
                    switch (foodAmount) {
                        case 1:
                            Bank.withdraw(foodId, Bank.Amount.ONE);
                            break;
                        case 5:
                            Bank.withdraw(foodId, Bank.Amount.FIVE);
                            break;
                        case 10:
                            Bank.withdraw(foodId, Bank.Amount.TEN);
                            break;
                        case 28:
                            Bank.withdraw(foodId, Bank.Amount.ALL);
                            break;
                        default:
                            Bank.withdraw(foodId, foodAmount);
                    }
                    Utilities.waitFor(Inventory.contains(foodId), 2000);
                }
            }
            if ((Inventory.contains(foodId) && foodId > 0 && !Inventory.contains(GRAPES))
                    || (!Inventory.contains(GRAPES)) && foodId == 0) {
                Bank.close();
            }
        }
    }
}