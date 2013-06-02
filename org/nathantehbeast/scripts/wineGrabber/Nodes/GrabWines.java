package org.nathantehbeast.scripts.wineGrabber.Nodes;

import org.nathantehbeast.api.framework.Condition;
import org.nathantehbeast.api.framework.XNode;
import org.nathantehbeast.api.tools.Calc;
import org.nathantehbeast.api.tools.MCamera;
import org.nathantehbeast.api.tools.Utilities;
import org.nathantehbeast.scripts.wineGrabber.Constants;
import org.powerbot.game.api.methods.Calculations;
import org.powerbot.game.api.methods.Game;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.GroundItems;
import org.powerbot.game.api.methods.node.Menu;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.tab.Magic;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.node.GroundItem;
import org.powerbot.game.api.wrappers.node.SceneObject;
import sk.action.ActionBar;

import java.awt.*;


/**
 * Created with IntelliJ IDEA.
 * User: Nathan
 * Date: 5/29/13
 * Time: 12:10 AM
 * To change this template use File | Settings | File Templates.
 */
public class GrabWines implements XNode {

    private SceneObject table;
    private GroundItem wine;
    private Tile hover = new Tile(2952, 3473, 0);

    @Override
    public boolean activate() {
        return ((wine = GroundItems.getNearest(5, Constants.WINE_ID)) != null  && !Inventory.isFull() && Inventory.contains(Constants.LAW_ID) && Constants.TEMPLE.contains(Players.getLocal().getLocation()));
    }

    @Override
    public void execute() {
        if (wine != null && Magic.isSpellSelected()) {
            if (!Calc.isOnScreen(wine)) {
                MCamera.turnTo(wine, 50);
            }
            final Point point = Calculations.groundToScreen((int) ((hover.getX() - Game.getBaseX() + 0.5D) * 512.0D), (int) ((hover.getY() - Game.getBaseY() + 0.5D) * 512.0D), 1, 500);
            if (!Players.getLocal().isMoving() && Mouse.move(point)) {
                if (Mouse.click(false)) {
                    if (Menu.select("Cast", "Wine of Zamorak")) {
                        Utilities.waitFor(new Condition() {
                            @Override
                            public boolean validate() {
                                return !wine.validate();
                            }
                        }, 2000, 5000);
                    } else {
                        Menu.select("Cancel");
                    }
                }
            }
            Mouse.move(point);
        }
        if (Calculations.distanceTo(hover) > 2) {
            Walking.walk(hover);
        }
        if (table == null) {
            table = SceneEntities.getNearest(Constants.TABLE_ID);
        }
        if (!Magic.isSpellSelected()) {
            ActionBar.useSlot(0);
            Utilities.waitFor(new Condition() {
                @Override
                public boolean validate() {
                    return Magic.isSpellSelected();
                }
            }, 600, 1500);
        }

    }
}
