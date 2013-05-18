package org.nathantehbeast.scripts.guardKiller.Nodes;

import org.nathantehbeast.api.tools.Utilities;
import org.nathantehbeast.scripts.guardKiller.GuardKiller;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Calculations;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.wrappers.node.Item;
import org.powerbot.game.api.wrappers.node.SceneObject;

import static org.nathantehbeast.scripts.guardKiller.GuardKiller.GRAPES;
import static org.nathantehbeast.scripts.guardKiller.GuardKiller.foodId;

public class eatFood extends Node {

    @Override
    public boolean activate() {
        SceneObject grapes = SceneEntities.getNearest(GRAPES);
        return foodId > 0 && (Players.getLocal().getHealthPercent() < 50 || (Inventory.isFull() && grapes != null && Calculations.distanceTo(grapes) < 12));
    }

    @Override
    public void execute() {
        GuardKiller.currentNode = this;
        SceneObject grapes = SceneEntities.getNearest(GRAPES);
        Item food = Inventory.getItem(foodId);
        if (Inventory.isFull() && grapes != null && Calculations.distanceTo(grapes) < 12) {
            if (org.nathantehbeast.api.tools.Calculations.isOnScreen(grapes) && food != null) {
                food.getWidgetChild().click(true);
                Utilities.waitFor(!Inventory.isFull(), 1500);
            }
        } else {
            if (food != null) {
                food.getWidgetChild().click(true);
            }
        }
        grapes = null;
        food = null;
    }
}