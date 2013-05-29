/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nathantehbeast.scripts.yanilleIron.Nodes;

import org.nathantehbeast.api.tools.MCamera;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Bank;
import org.powerbot.game.api.wrappers.Entity;
import org.powerbot.game.api.wrappers.Locatable;
import org.powerbot.game.api.wrappers.node.Item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import static org.nathantehbeast.scripts.yanilleIron.YanilleIron.itemFilter;
import static org.nathantehbeast.scripts.yanilleIron.YanilleIron.pm;

public class bankOres extends Node {

    private Entity bank;
    private final List<Integer> toBank = Collections.synchronizedList(new ArrayList<Integer>());
    @Override
    public boolean activate() {
        return (bank = Bank.getNearest()) != null && !pm && Inventory.isFull();
    }

    @Override
    public void execute() {
        if (!bank.isOnScreen()) {
            MCamera.turnTo((Locatable) bank, 50);
        }
        if (Bank.open()) {
            synchronized (toBank) {
                for (Item item : Inventory.getItems(itemFilter)) {
                    if (toBank.contains(item.getId())) {
                        toBank.add(item.getId());
                    }
                }
                for (ListIterator<Integer> iterator = toBank.listIterator(); iterator.hasNext();) {
                    int id = iterator.next();
                    if (Bank.deposit(id, Bank.Amount.ALL)) {
                        sleep(300);
                    }
                }
                toBank.clear();
            }
        }
    }
}