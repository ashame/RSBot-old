package org.nathantehbeast.scripts.aiominer.nodes;

import org.nathantehbeast.api.framework.XNode;
import org.nathantehbeast.api.tools.Calc;
import org.nathantehbeast.api.tools.MCamera;
import org.nathantehbeast.api.tools.Utilities;
import org.nathantehbeast.scripts.aiominer.Constants.BankLocations;
import org.nathantehbeast.scripts.aiominer.Constants.Ore;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.util.Filter;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.node.SceneObject;
import sk.general.TimedCondition;


/**
 * Created with IntelliJ IDEA.
 * User: Nathan
 * Date: 5/13/13
 * Time: 5:59 PM
 * To change this template use File | Settings | File Templates.
 */

public final class Mine implements XNode {

    private final Ore ore;
    private final BankLocations bankLocation;
    private final Tile center;
    private final int radius;
    private static SceneObject rock;


    /**
     * @param ore    Constants.Ore to mine within the radius
     * @param center org.powerbot.game.api.wrappers.Tile as the center point of calculations
     * @param radius The radius from the center tile to mine in
     */
    public Mine(Ore ore, Tile center, int radius, BankLocations bankLocation) {
        this.ore = ore;
        this.center = center;
        this.radius = radius;
        this.bankLocation = bankLocation;
    }

    private final Filter<SceneObject> FILTER = new Filter<SceneObject>() {
        @Override
        public boolean accept(SceneObject object) {
            if (bankLocation != null)
                return object != null && (radius > 0 ? Calc.isInArea(center, object, radius) : Calc.isInArea(bankLocation.getRockArea(), object)) && Utilities.contains(ore.getRocks(), object.getId());
            return object != null && Calc.isInArea(center, object, radius) && Utilities.contains(ore.getRocks(), object.getId());
        }
    };

    @Override
    public boolean activate() {
        if (Inventory.isFull()) return false;
        if (bankLocation != null && !bankLocation.getRockArea().contains(Players.getLocal())) return false;
        return (rock = SceneEntities.getNearest(FILTER)) != null && ((bankLocation != null && bankLocation.getRockArea().contains(Players.getLocal())) || (bankLocation == null && Calc.isInArea(center, Players.getLocal(), radius)));
    }

    @Override
    public void execute() {
        if (!Calc.isOnScreen(rock)) {
            MCamera.turnTo(rock, 50);
        }
        if (Calc.isOnScreen(rock) && rock.getLocation().isOnMap() && rock.interact("Mine")) {
            new TimedCondition(5000) {
                @Override
                public boolean isDone() {
                    return !rock.validate();
                }
            }.waitStop();
        }
    }
}
