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
import org.powerbot.game.api.wrappers.widget.WidgetChild;

import java.awt.*;

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
    private SceneObject furnace;

    public Crafting(Area furnaceArea, int goldId, int furnaceId) {
        this.furnaceArea = furnaceArea;
        this.goldId = goldId;
        this.furnaceId = furnaceId;
    }

    private static final Condition CRAFTING_INTERFACE_OPEN = new Condition() {
        @Override
        public boolean validate() {
            return Widgets.get(1370).validate();
        }
    };

    private static final Condition GOLD_BRACELET_SELECTED = new Condition() {
        @Override
        public boolean validate() {
            return Widgets.get(1370, 56).getText().equals("Gold bracelet");
        }
    };

    private static final Condition CRAFTING = new Condition() {
        @Override
        public boolean validate() {
            return Players.getLocal().getAnimation() != -1;
        }
    };

    @Override
    public boolean activate() {
        return (furnace = SceneEntities.getNearest(furnaceId)) != null && Inventory.contains(goldId) && furnaceArea.contains(Players.getLocal().getLocation()) && !Widgets.get(1251).validate();
    }

    @Override
    public void execute() {
        if (!Widgets.get(1370).validate() && Calc.isOnScreen(furnace) && furnace.interact("Smelt")) {
            Utilities.waitFor(CRAFTING_INTERFACE_OPEN, 5000);
        }
        if (Widgets.get(1370).validate()) { //Crafting Widget
            if (!Widgets.get(1370, 56).getText().equals("Gold bracelet")) { //Title of item in crafting widget
                if (!isSlotVisible(Widgets.get(1371, 44).getChild(9)))
                    Widgets.scroll(Widgets.get(1371, 44).getChild(9), Widgets.get(1371, 47));
                Widgets.get(1371, 44).getChild(9).click(true); //Gold bracelet
                Utilities.waitFor(GOLD_BRACELET_SELECTED, 5000);
            }
            Widgets.get(1370, 37).click(true); //Smelt button
            Utilities.waitFor(CRAFTING, 1500);
        }
    }

    private static boolean isSlotVisible(final WidgetChild slot) {
        final WidgetChild slots = Widgets.get(1370, 22);
        final Rectangle visibleBounds = new Rectangle(
                slots.getAbsoluteLocation(),
                new Dimension(
                        slots.getWidth() - slot.getWidth(), slots.getHeight() - slot.getHeight()
                )
        );
        return visibleBounds.contains(slot.getAbsoluteLocation());
    }
}

