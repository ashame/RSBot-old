package org.nathantehbeast.scripts.guardKiller.Nodes;

import org.nathantehbeast.api.framework.XNode;
import org.nathantehbeast.api.tools.Calc;
import org.nathantehbeast.api.tools.MCamera;
import org.nathantehbeast.scripts.guardKiller.Constants;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.interactive.NPCs;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.GroundItems;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.util.Filter;
import org.powerbot.game.api.wrappers.Area;
import org.powerbot.game.api.wrappers.interactive.NPC;
import sk.general.TimedCondition;

/**
 * Created with IntelliJ IDEA.
 * User: Nathan
 * Date: 5/30/13
 * Time: 10:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class FightGuards implements XNode {

    private static NPC guard;
    private final Area castleArea;
    private final Filter<NPC> guardFilter;
    private final int foodId;
    private final int grapeId = Constants.GRAPE_ID;

    public FightGuards(final Area castleArea, final Filter<NPC> guardFilter, final int foodId) {
        this.castleArea = castleArea;
        this.guardFilter = guardFilter;
        this.foodId = foodId;
    }

    @Override
    public boolean activate() {
        return (guard = NPCs.getNearest(guardFilter)) != null
                && (!Inventory.isFull() || (Inventory.isFull() && foodId > 0 && Inventory.contains(foodId)))
                && Players.getLocal().getInteracting() == null
                && castleArea.contains(Players.getLocal().getLocation())
                && Players.getLocal().getHealthPercent() > 50
                && GroundItems.getNearest(20, grapeId) == null;
    }

    @Override
    public void execute() {
        if (Players.getLocal().getInteracting() == null) {
            if (!Calc.isOnScreen(guard))
                MCamera.turnTo(guard, 50);
            if (!Calc.isOnScreen(guard) && guard.getLocation().isOnMap())
                Walking.walk(guard);
            if (guard.interact("Attack")) {
                new TimedCondition(2100) {
                    @Override
                    public boolean isDone() {
                        return Players.getLocal().getInteracting() != null;
                    }
                }.waitStop();
            }
        }
    }
}
