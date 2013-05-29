package org.nathantehbeast.scripts.yanilleIron.Nodes;

import org.nathantehbeast.api.framework.Condition;
import org.nathantehbeast.api.tools.Calc;
import org.nathantehbeast.api.tools.MCamera;
import org.nathantehbeast.api.tools.Utilities;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Calculations;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.util.Filter;
import org.powerbot.game.api.wrappers.Area;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.node.SceneObject;

public class mineOres extends Node {

    public static final int[] ROCK = {37308, 37309, 72083, 72081, 72082};
    private static SceneObject rock;
    public static final Filter<SceneObject> rockFilter = new Filter<SceneObject>() {
        @Override
        public boolean accept(SceneObject t) {
            for (int i : ROCK) {
                if (t.getId() == 72083 || t.getId() == 72081 || t.getId() == 72082) return true;
                if (t.getId() == i && Calculations.distance(new Tile(2627, 3141, 0), t.getLocation()) < 5) {
                    return true;
                }
            }
            return false;
        }
    };
    private static final Condition CONDITION = new Condition() {
        @Override
        public boolean validate() {
            return Players.getLocal().getAnimation() != -1;
        }
    };

    @Override
    public boolean activate() {
        return (rock = SceneEntities.getNearest(rockFilter)) != null && !Inventory.isFull() && new Area(new Tile(2623, 3137, 0), new Tile(2633, 3145, 0)).contains(Players.getLocal().getLocation()) && Utilities.isIdle();
    }

    @Override
    public void execute() {
        if (!Calc.isOnScreen(rock)) {
            MCamera.turnTo(rock, 50);
        }
        if (Calc.isOnScreen(rock) && rock.getLocation().isOnMap() && rock.interact("Mine")) {
            Utilities.waitFor(CONDITION, 3000);
        }
    }
}