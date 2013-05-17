/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nathantehbeast.scripts.yanilleIron.Nodes;

import org.nathantehbeast.api.tools.Utilities;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.core.script.methods.Game;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Bank;
import org.powerbot.game.api.wrappers.Entity;
import org.powerbot.game.api.wrappers.node.Item;

import static org.nathantehbeast.scripts.yanilleIron.YanilleIron.*;

public class bankOres extends Node {

    @Override
    public boolean activate() {
        Entity bank = Bank.getNearest();
        if (Game.getClientState() == Game.INDEX_MAP_LOADED && bank != null) {
            if (!pm && Inventory.isFull() && bank.isOnScreen()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void execute() {
        while (!Bank.isOpen() && Inventory.isFull()) {
            Bank.open();
            Utilities.waitFor(Bank.isOpen(), 3000);
        }
        while (Bank.isOpen() && Inventory.getItems(itemFilter).length > 0) {
            for (Item item : Inventory.getItems(itemFilter)) {
                if (item != null && !toBank.contains(item.getId())) {
                    toBank.add(item.getId());
                    if (debug) {
                        System.out.println("Added " + item.getName() + " to array.");
                    }
                }
            }
            for (int i = 0; i < toBank.size(); i++) {
                if (Inventory.getItems(itemFilter).length == 0) {
                    break;
                }
                Bank.deposit(toBank.get(i), Bank.Amount.ALL);
                if (debug) {
                    System.out.println("Banked " + toBank.get(i) + ".");
                }
                Utilities.waitFor(!Inventory.contains(toBank.get(i)), 3000);
                if (debug) {
                    System.out.println("Items remaining: " + Inventory.getItems(itemFilter).length);
                }
                sleep(300);
            }
            toBank.clear();
        }
    }
}