package org.nathantehbeast.scripts.aiominer.nodes;

import org.nathantehbeast.api.framework.XNode;
import org.nathantehbeast.api.tools.Logger;
import org.powerbot.core.script.job.Task;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Bank;
import org.powerbot.game.api.util.Filter;
import org.powerbot.game.api.wrappers.Area;
import org.powerbot.game.api.wrappers.node.Item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Nathan
 * Date: 5/13/13
 * Time: 10:58 PM
 * To change this template use File | Settings | File Templates.
 */
public final class Banking implements XNode {

    private Area bankArea;
    private static final List<Integer> BANKLIST = Collections.synchronizedList(new ArrayList<Integer>());

    final Filter<Item> FILTER = new Filter<Item>() {
        @Override
        public boolean accept(Item item) {
            return !item.getName().toLowerCase().contains("pickaxe") && !item.getName().toLowerCase().contains("adze");
        }
    };

    public Banking(Area bankArea) {
        this.bankArea = bankArea;
    }

    @Override
    public boolean activate() {
        return Inventory.getItems(FILTER).length > 0 && bankArea.contains(Players.getLocal().getLocation());
    }

    @Override
    public void execute() {
        if (Bank.open()) {
            synchronized (BANKLIST) {
                for (Item item : Inventory.getItems(FILTER)) {
                    if (!BANKLIST.contains(item.getId())) {
                        BANKLIST.add(item.getId());
                        Logger.log("Added " + item.getId());
                    }
                }
                Logger.log("List: "+BANKLIST.toString());
                for (final int i : BANKLIST) {
                    if (Bank.deposit(i, Bank.Amount.ALL)) {
                        Logger.log("Banking " + i);
                        Task.sleep(300);
                    }
                }
                BANKLIST.clear();
                Logger.log("Cleared list");
            }
        }
    }
}
