package org.nathantehbeast.scripts.yanilleIron.Nodes;

import org.nathantehbeast.api.tools.MCamera;
import org.nathantehbeast.api.tools.Utilities;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.core.script.methods.Game;
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

    @Override
    public boolean activate() {
        SceneObject rock = SceneEntities.getNearest(rockFilter);
        if (Game.getClientState() == Game.INDEX_MAP_LOADED && rock != null && !Inventory.isFull() && new Area(new Tile(2623, 3137, 0), new Tile(2633, 3145, 0)).contains(Players.getLocal().getLocation())) {
            return true;
        }
        return false;
    }

    @Override
    public void execute() {
        SceneObject rock = SceneEntities.getNearest(rockFilter);
        if (rock != null) {
            if (!rock.isOnScreen()) {
                MCamera.turnTo(rock, 5);
            }
            if (rock.isOnScreen() && Players.getLocal().getAnimation() == -1) {
                rock.interact("Mine", "Iron ore rocks");
                Utilities.waitFor(Players.getLocal().getAnimation() == 12188, 3000);
            }
        }
    }
}