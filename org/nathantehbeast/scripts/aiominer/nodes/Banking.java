package org.nathantehbeast.scripts.aiominer.nodes;

import org.nathantehbeast.api.framework.XNode;
import org.nathantehbeast.api.tools.Calc;
import org.nathantehbeast.api.tools.Logger;
import org.nathantehbeast.api.tools.MCamera;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Bank;
import org.powerbot.game.api.util.Filter;
import org.powerbot.game.api.wrappers.Area;
import org.powerbot.game.api.wrappers.Entity;
import org.powerbot.game.api.wrappers.Locatable;
import org.powerbot.game.api.wrappers.node.Item;
import sk.general.TimedCondition;

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
    private static final List<Integer> BANK_LIST = Collections.synchronizedList(new ArrayList<Integer>());
    private Entity bank;

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
        return (bank = Bank.getNearest()) != null && Inventory.getItems(FILTER).length > 0 && bankArea.contains(Players.getLocal().getLocation());
    }

    @Override
    public void execute() {
        if (!Calc.isOnScreen(bank)) {
            MCamera.turnTo((Locatable) bank, 50);
            new TimedCondition(1500) {
                @Override
                public boolean isDone() {
                    return Calc.isOnScreen(bank);
                }
            }.waitStop();
        }
        if (Bank.open()) {
            synchronized (BANK_LIST) {
                for (Item item : Inventory.getItems(FILTER)) {
                    if (!BANK_LIST.contains(item.getId())) {
                        BANK_LIST.add(item.getId());
                        Logger.log("Added " + item.getId());
                    }
                }
                Logger.log("List: " + BANK_LIST.toString());
                for (final int i : BANK_LIST) {
                    if (Bank.deposit(i, Bank.Amount.ALL)) {
                        Logger.log("Banking " + i);
                        new TimedCondition(1500) {
                            @Override
                            public boolean isDone() {
                                return !Inventory.contains(i);
                            }
                        }.waitStop();
                    }
                }
                BANK_LIST.clear();
                Logger.log("Cleared list");
            }
        }
    }
}
