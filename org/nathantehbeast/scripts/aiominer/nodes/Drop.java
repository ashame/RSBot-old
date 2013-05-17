package org.nathantehbeast.scripts.aiominer.nodes;

import org.nathantehbeast.api.framework.Condition;
import org.nathantehbeast.api.tools.Utilities;
import org.nathantehbeast.scripts.aiominer.Main;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.util.Filter;
import org.powerbot.game.api.wrappers.node.Item;
import sk.action.ActionBar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Nathan
 * Date: 5/13/13
 * Time: 6:28 PM
 * To change this template use File | Settings | File Templates.
 */
public final class Drop extends Node {

    final Filter<Item> FILTER = new Filter<Item>() {
        @Override
        public boolean accept(Item item) {
            return !item.getName().toLowerCase().contains("pickaxe") && !item.getName().toLowerCase().contains("adze") && item.getId() != Main.getOre().id;
        }
    };

    @Override
    public boolean activate() {
        return Main.getPowermine() && Inventory.isFull();
    }

    @Override
    public void execute() {
        final List<Item> toDrop = new ArrayList<>();
        if (!ActionBar.isOpen()) {
            ActionBar.makeReady();
            Utilities.waitFor(new Condition() {
                @Override
                public boolean validate() {
                    return ActionBar.isOpen();
                }
            }, 1500);
        }
        for (int i = 0 ; i < Inventory.getItems(new Filter<Item>() {
            @Override
            public boolean accept(Item item) {
                return item.getId() == Main.getOre().id;
            }
        }).length; i++) {
            ActionBar.getNode(0).spam();
        }
        for (Item item : Inventory.getItems(FILTER)) {
            if (!toDrop.contains(item)) {
                toDrop.add(item);
            }
        }
        for (Item item : toDrop) {
            if (item.getWidgetChild().interact("drop")) {
                toDrop.remove(item);
                sleep(150);
            }
        }
    }
}
