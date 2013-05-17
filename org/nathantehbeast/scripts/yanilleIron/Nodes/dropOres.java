package org.nathantehbeast.scripts.yanilleIron.Nodes;

import org.powerbot.core.script.job.state.Node;
import org.powerbot.core.script.methods.Game;
import org.powerbot.game.api.methods.input.Keyboard;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.wrappers.node.Item;

import static org.nathantehbeast.scripts.yanilleIron.YanilleIron.*;

public class dropOres extends Node {

    @Override
    public boolean activate() {
        return Game.getClientState() == Game.INDEX_MAP_LOADED && Inventory.isFull() && pm;
    }

    @Override
    public void execute() {
        while (Inventory.contains(IRON, SAPPHIRE, EMERALD, RUBY, DIAMOND)) {
            for (int i = 0; i < Inventory.getCount(IRON); i++) {
                Keyboard.sendKey('1');
                sleep(50, 150);
            }
            for (Item i : Inventory.getItems(dropFilter)) {
                if (i != null && !toDrop.contains(i)) {
                    toDrop.add(i);
                    if (debug) {
                        System.out.println("Added " + i.getName() + " to drop list.");
                    }
                }
            }
            for (int i = 0; i < toDrop.size(); i++) {
                if (Inventory.getItems(dropFilter).length == 0) {
                    break;
                }
                toDrop.get(i).getWidgetChild().interact("Drop");
                sleep(300);
                if (debug) {
                    System.out.println("Dropped " + toDrop.get(i).getName());
                }
                if (debug) {
                    System.out.println("Items Remaining: " + Inventory.getItems(dropFilter).length);
                }
            }
        }
        toDrop.clear();
    }
}