package org.nathantehbeast.scripts.braceletCrafter.Nodes;

import org.nathantehbeast.api.framework.Condition;
import org.nathantehbeast.api.framework.XNode;
import org.nathantehbeast.api.tools.Calc;
import org.nathantehbeast.api.tools.Utilities;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.wrappers.Area;
import org.powerbot.game.api.wrappers.node.SceneObject;

/**
 * Created with IntelliJ IDEA.
 * User: Nathan
 * Date: 5/26/13
 * Time: 4:29 PM
 * To change this template use File | Settings | File Templates.
 */

public class Crafting implements XNode {

    private Area furnaceArea;
    private int goldId;
    private int furnaceId;

    public Crafting(Area furnaceArea, int goldId, int furnaceId) {
        this.furnaceArea = furnaceArea;
        this.goldId = goldId;
        this.furnaceId = furnaceId;
    }

    @Override
    public boolean activate() {
        return (Inventory.contains(goldId) && furnaceArea.contains(Players.getLocal().getLocation())) && !Widgets.get(1251).validate();
    }

    @Override
    public void execute() {
        final SceneObject FURNACE = SceneEntities.getNearest(furnaceId);
        if (!Widgets.get(1370).validate() && FURNACE != null && Calc.isOnScreen(FURNACE) && FURNACE.interact("Smelt")) {
            Utilities.waitFor(new Condition() {
                @Override
                public boolean validate() {
                    return Widgets.get(1370).validate();
                }
            }, 5000);
        }
        if (Widgets.get(1370).validate()) { //Crafting Widget
            if (!Widgets.get(1370, 56).getText().equals("Gold bracelet")) { //Title of item in crafting widget
                Widgets.get(1370, 44).click(true); //Gold bracelet
                Utilities.waitFor(new Condition() {
                    @Override
                    public boolean validate() {
                        return Widgets.get(1370, 56).getText().equals("Gold bracelet");
                    }
                }, 2000);
            }
            Widgets.get(1370, 37).click(true); //Smelt button
            Utilities.waitFor(new Condition() {
                @Override
                public boolean validate() {
                    return Players.getLocal().getAnimation() != -1;
                }
            }, 1500);
        }
    }
}

