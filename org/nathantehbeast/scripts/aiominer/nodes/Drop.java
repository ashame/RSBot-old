package org.nathantehbeast.scripts.aiominer.nodes;

import org.nathantehbeast.api.framework.Condition;
import org.nathantehbeast.api.framework.XNode;
import org.nathantehbeast.api.tools.Utilities;
import org.nathantehbeast.scripts.aiominer.Constants.Ore;
import org.powerbot.core.script.job.Task;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.ChatOptions;
import org.powerbot.game.api.util.Filter;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.wrappers.node.Item;
import sk.action.ActionBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;


/**
 * Created with IntelliJ IDEA.
 * User: Nathan
 * Date: 5/13/13
 * Time: 6:28 PM
 * To change this template use File | Settings | File Templates.
 */
public final class Drop implements XNode {

    private boolean powermine;
    private Ore ore;
    private final List<Item> toDrop = Collections.synchronizedList(new ArrayList<Item>());

    private final Filter<Item> FILTER = new Filter<Item>() {
        @Override
        public boolean accept(Item item) {
            return !item.getName().toLowerCase().contains("pickaxe") && !item.getName().toLowerCase().contains("adze")&& item.getId() != ore.getId();
        }
    };

    private static final Condition CAN_CONTINUE = new Condition() {
        @Override
        public boolean validate() {
            return !ChatOptions.canContinue();
        }
    };

    private static final Condition ACTIONBAR_EXPANDED = new Condition() {
        @Override
        public boolean validate() {
            return ActionBar.isExpanded();
        }
    };

    private static final Condition ACTIONBAR_CLOSED = new Condition() {
        @Override
        public boolean validate() {
            return !ActionBar.isExpanded();
        }
    };

    /**
     * @param powermine To drop or not to drop
     * @param ore       The Constants.Ore to drop
     */
    public Drop(final boolean powermine, final Ore ore) {
        this.powermine = powermine;
        this.ore = ore;
    }

    @Override
    public boolean activate() {
        return powermine && Inventory.isFull();
    }

    @Override
    public void execute() {
        final Timer t = new Timer(10000);
        if (!ActionBar.isExpanded()) {
            ActionBar.setExpanded(true);
            Utilities.waitFor(ACTIONBAR_EXPANDED, 1500);
        }
        if (ChatOptions.canContinue()) {
            ChatOptions.getContinueOption().select(true);
            Utilities.waitFor(CAN_CONTINUE, 3000);
        }
        while (t.isRunning() && Inventory.contains(ore.getId())) {
            ActionBar.useSlot(0);
            Task.sleep(80, 100);
        }
        synchronized (toDrop) {
            for (Item item : Inventory.getItems(FILTER)) {
                if (!toDrop.contains(item)) {
                    toDrop.add(item);
                }
            }
            for (ListIterator<Item> li = toDrop.listIterator() ; li.hasNext();) {
                Item i = li.next();
                if (i.getWidgetChild().interact("Drop")) {
                    Task.sleep(300);
                }
            }
            toDrop.clear();
        }
        if (Inventory.contains(ore.getId())) {
            ActionBar.setExpanded(false);
            Utilities.waitFor(ACTIONBAR_CLOSED, 2000);
            ActionBar.setExpanded(true);
        } else {
            ActionBar.setExpanded(false);
        }
    }
}
