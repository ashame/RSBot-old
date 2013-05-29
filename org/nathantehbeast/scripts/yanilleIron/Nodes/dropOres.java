package org.nathantehbeast.scripts.yanilleIron.Nodes;

import org.nathantehbeast.api.framework.Condition;
import org.nathantehbeast.api.tools.Utilities;
import org.nathantehbeast.scripts.yanilleIron.YanilleIron;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.core.script.methods.Game;
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

import static org.nathantehbeast.scripts.yanilleIron.YanilleIron.pm;

public class dropOres extends Node {

    private final List<Item> toDrop = Collections.synchronizedList(new ArrayList<Item>());

    private final Filter<Item> FILTER = new Filter<Item>() {
        @Override
        public boolean accept(Item item) {
            return !item.getName().toLowerCase().contains("pickaxe") && !item.getName().toLowerCase().contains("adze")&& item.getId() != YanilleIron.IRON;
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
    @Override
    public boolean activate() {
        return Game.getClientState() == Game.INDEX_MAP_LOADED && Inventory.isFull() && pm;
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
        while (t.isRunning() && Inventory.contains(YanilleIron.IRON)) {
            ActionBar.useSlot(0);
            sleep(80, 100);
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
                    sleep(300);
                }
            }
            toDrop.clear();
        }
        if (Inventory.contains(YanilleIron.IRON)) {
            ActionBar.setExpanded(false);
            Utilities.waitFor(ACTIONBAR_CLOSED, 2000);
            ActionBar.setExpanded(true);
        } else {
            ActionBar.setExpanded(false);
        }
    }
}