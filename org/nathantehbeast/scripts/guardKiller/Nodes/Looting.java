package org.nathantehbeast.scripts.guardKiller.Nodes;

import org.nathantehbeast.api.framework.XNode;
import org.nathantehbeast.api.tools.Calc;
import org.nathantehbeast.api.tools.MCamera;
import org.powerbot.core.script.job.Task;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.GroundItems;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.util.Filter;
import org.powerbot.game.api.wrappers.Area;
import org.powerbot.game.api.wrappers.node.GroundItem;
import org.powerbot.game.api.wrappers.widget.WidgetChild;
import sk.general.TimedCondition;

/**
 * Created with IntelliJ IDEA.
 * User: Nathan
 * Date: 5/31/13
 * Time: 12:40 AM
 * To change this template use File | Settings | File Templates.
 */
public class Looting implements XNode {

    private GroundItem[] loot;
    private WidgetChild food;
    private final int grapeId;
    private final int foodId;
    private final Area castleArea;

    public Looting(final int grapeId, final int foodId, final Area castleArea) {
        this.grapeId = grapeId;
        this.foodId = foodId;
        this.castleArea = castleArea;
    }

    private final Filter<GroundItem> lootFilter = new Filter<GroundItem>() {
        @Override
        public boolean accept(GroundItem groundItem) {
            return groundItem.getId() == grapeId && castleArea.contains(groundItem.getLocation());
        }
    };

    @Override
    public boolean activate() {
        return (loot = GroundItems.getLoaded(lootFilter)) != null && Players.getLocal().getInteracting() == null && (!Inventory.isFull() || (Inventory.isFull() && Inventory.contains(foodId)));
    }

    @Override
    public void execute() {
        if (Inventory.isFull() && Inventory.contains(foodId)) {
            for (final GroundItem i : loot) {
                food = Inventory.getItem(foodId).getWidgetChild();
                if (food.interact("Eat"))
                    Task.sleep(600);
            }
        }
        if (!Calc.isOnScreen(loot[0])) {
            MCamera.turnTo(loot[0], 50);
        }
        for (final GroundItem i : loot) {
            if (i.interact("Take", "Grapes")) {
                new TimedCondition(3000) {
                    @Override
                    public boolean isDone() {
                        return !i.validate();
                    }
                }.waitStop();
            }
        }

    }
}
