package org.nathantehbeast.scripts.aiominer.nodes;

import org.nathantehbeast.api.framework.Condition;
import org.nathantehbeast.api.tools.Calc;
import org.nathantehbeast.api.tools.MCamera;
import org.nathantehbeast.api.tools.Utilities;
import org.nathantehbeast.scripts.aiominer.Constants.Ore;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.core.script.methods.Players;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.util.Filter;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.node.SceneObject;


/**
 * Created with IntelliJ IDEA.
 * User: Nathan
 * Date: 5/13/13
 * Time: 5:59 PM
 * To change this template use File | Settings | File Templates.
 */

public final class Mine extends Node {

    private Ore ore;
    private Tile center;
    private int radius;
    private static SceneObject rock;
    private static SceneObject previousRock;

    /**
     * @param ore    Constants.Ore to mine within the radius
     * @param center org.powerbot.game.api.wrappers.Tile as the center point of calculations
     * @param radius The radius from the center tile to mine in
     */
    public Mine(Ore ore, Tile center, int radius) {
        this.ore = ore;
        this.center = center;
        this.radius = radius;
    }

    private final Filter<SceneObject> FILTER = new Filter<SceneObject>() {
        @Override
        public boolean accept(SceneObject object) {
            return object != null && Calc.isInArea(center, object, radius) && Utilities.contains(ore.getRocks(), object.getId());
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
        return (rock = SceneEntities.getNearest(FILTER)) != null && !Inventory.isFull() && Utilities.isIdle() && !Players.getLocal().isMoving();
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
