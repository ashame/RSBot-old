package org.nathantehbeast.scripts.aiominer.nodes;

import org.nathantehbeast.api.framework.Condition;
import org.nathantehbeast.api.framework.XNode;
import org.nathantehbeast.api.tools.Logger;
import org.nathantehbeast.api.tools.Utilities;
import org.nathantehbeast.scripts.aiominer.Constants;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Bank;
import org.powerbot.game.api.util.Filter;
import org.powerbot.game.api.wrappers.node.Item;

/**
 * Created with IntelliJ IDEA.
 * User: Nathan
 * Date: 5/13/13
 * Time: 10:58 PM
 * To change this template use File | Settings | File Templates.
 */
public final class Banking implements XNode {

    final Filter<Item> FILTER = new Filter<Item>() {
        @Override
        public boolean accept(Item item) {
            return !item.getName().toLowerCase().contains("pickaxe") && !item.getName().toLowerCase().contains("adze");
        }
    };

    @Override
    public boolean activate() {
        return Bank.isOpen();
    }

    @Override
    public void execute() {
        if (Inventory.getItems(FILTER).length == 0) {
            return;
        }
        if (Bank.open()) {
            Utilities.waitFor(new Condition() {
                @Override
                public boolean validate() {
                    return Bank.isOpen();
                }
            }, 1500);
            if (loopBank()) {
                Logger.log("Finished Depositing.");
            } else {
                loopBank();
            }
        }
    }

    private boolean loopBank() {
        return Utilities.depositAllExcept(Constants.PICKAXES);
    }
}
