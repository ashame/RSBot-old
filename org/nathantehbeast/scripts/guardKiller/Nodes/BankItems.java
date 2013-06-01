package org.nathantehbeast.scripts.guardKiller.Nodes;

import org.nathantehbeast.api.framework.XNode;
import org.nathantehbeast.api.tools.Logger;
import org.powerbot.core.script.job.Task;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Bank;
import org.powerbot.game.api.util.Filter;
import org.powerbot.game.api.wrappers.Area;
import org.powerbot.game.api.wrappers.Entity;
import org.powerbot.game.api.wrappers.node.Item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Created with IntelliJ IDEA.
 * User: Nathan
 * Date: 5/30/13
 * Time: 11:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class BankItems implements XNode {

    private final int foodId;
    private final Area bankArea;
    private Entity banker;
    private final List<Integer> BANKLIST = Collections.synchronizedList(new ArrayList<Integer>());
    private final Filter<Item> BANK_FILTER;

    public BankItems(final int foodId, final Area bankArea, final Filter<Item> bankFilter) {
        this.foodId = foodId;
        this.bankArea = bankArea;
        this.BANK_FILTER = bankFilter;
    }

    @Override
    public boolean activate() {
        return (banker = Bank.getNearest()) != null
                && (Inventory.isFull() || (foodId > 0 && Inventory.getCount(foodId) == 0))
                && bankArea.contains(Players.getLocal().getLocation());
    }

    @Override
    public void execute() {
        if (Bank.open()) {
            synchronized (BANKLIST) {
                for (Item i : Inventory.getItems(BANK_FILTER)) {
                    if (!BANKLIST.contains(i.getId())) {
                        BANKLIST.add(i.getId());
                        Logger.log("Added " + i.getId());
                    }
                }
                Logger.log("List: "+BANKLIST.toString());
                for (int i : BANKLIST) {
                    if (Bank.deposit(i, Bank.Amount.ALL)) {
                        Logger.log("Banking " + i);
                        Task.sleep(500);
                    }
                }
                BANKLIST.clear();
            }
        }
    }
}
