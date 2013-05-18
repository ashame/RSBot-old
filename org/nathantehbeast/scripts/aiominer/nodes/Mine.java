package org.nathantehbeast.scripts.aiominer.nodes;

import org.nathantehbeast.api.framework.Condition;
import org.nathantehbeast.api.tools.Calculations;
import org.nathantehbeast.api.tools.Utilities;
import org.nathantehbeast.scripts.aiominer.Main;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.core.script.methods.Players;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.wrappers.node.SceneObject;


/**
 * Created with IntelliJ IDEA.
 * User: Nathan
 * Date: 5/13/13
 * Time: 5:59 PM
 * To change this template use File | Settings | File Templates.
 */
public final class Mine extends Node {
    @Override
    public boolean activate() {
        final SceneObject ROCK = SceneEntities.getNearest(Main.getOre().rocks);
        return ROCK != null && !Inventory.isFull() && Players.getLocal().getAnimation() == -1;
    }

    @Override
    public void execute() {
        final SceneObject ROCK = SceneEntities.getNearest(Main.getOre().rocks);
        if (ROCK != null && Calculations.isOnScreen(ROCK) && ROCK.getLocation().isOnMap() && ROCK.interact("Mine")) {
            Utilities.waitFor(new Condition() {
                @Override
                public boolean validate() {
                    return Players.getLocal().getAnimation() != -1;
                }
            }, 3000);
        }
        if (ROCK != null && !Calculations.isOnScreen(ROCK) && ROCK.getLocation().isOnMap()) {
            Camera.turnTo(ROCK);
        }
    }
}
