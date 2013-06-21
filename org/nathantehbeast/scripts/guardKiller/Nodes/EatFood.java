package org.nathantehbeast.scripts.guardKiller.Nodes;

import org.nathantehbeast.api.framework.XNode;
import org.powerbot.core.script.job.Task;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.wrappers.widget.WidgetChild;

/**
 * Created with IntelliJ IDEA.
 * User: Nathan
 * Date: 5/31/13
 * Time: 12:00 AM
 * To change this template use File | Settings | File Templates.
 */
public class EatFood implements XNode {

    private final int foodId;
    private WidgetChild food;

    public EatFood(final int foodId) {
        this.foodId = foodId;
    }

    @Override
    public boolean activate() {
        return foodId > 0 && (food = Inventory.getItem(foodId).getWidgetChild()) != null && Players.getLocal().getHealthPercent() < 60;
    }

    @Override
    public void execute() {
        food.interact("Eat");
        Task.sleep(1000);
    }
}
