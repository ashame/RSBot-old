package org.nathantehbeast.scripts.braceletCrafter.Nodes;

import org.nathantehbeast.api.framework.Condition;
import org.nathantehbeast.api.tools.Utilities;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Calculations;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.wrappers.node.SceneObject;

public class CraftBracelets extends Node {

    //Widgets
    private final int CRAFTING_WIDGET0 = 1370;
    private final int CRAFTING_WIDGET0_ITEM = 56;
    private final int CRAFTING_WIDGET0_CRAFT = 37;
    private final int CRAFTING_WIDGET = 1371;
    private final int CRAFTING_WIDGET_ITEMS = 44;
    private final int CRAFTING_WIDGET_GB = 8;
    private final int FURNACE_ID = 26814;
    private final int GOLD_ID = 2357;

    @Override
    public boolean activate() {
        SceneObject furnace = SceneEntities.getNearest(FURNACE_ID);
        if (furnace != null) {
            if (furnace.getLocation().isOnMap() && Inventory.contains(GOLD_ID)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void execute() {
        final SceneObject furnace = SceneEntities.getNearest(FURNACE_ID);
        if (furnace != null) {
            if (furnace.isOnScreen()) {
                if (!Widgets.get(CRAFTING_WIDGET).validate() && !Widgets.get(1251).validate()) {
                    if (furnace.interact("Smelt")) {
                        Utilities.waitFor(new Condition() {
                            @Override
                            public boolean validate() {
                                return Widgets.get(CRAFTING_WIDGET).validate();
                            }
                        }, 5000);
                    }
                }
                if (Widgets.get(CRAFTING_WIDGET).validate()) {
                    if (!Widgets.get(CRAFTING_WIDGET0, CRAFTING_WIDGET0_ITEM).getText().equals("Gold bracelet")) {
                        Widgets.get(CRAFTING_WIDGET, CRAFTING_WIDGET_ITEMS).getChild(CRAFTING_WIDGET_GB).click(true);
                    }
                    if (Widgets.get(CRAFTING_WIDGET0, CRAFTING_WIDGET0_CRAFT).getTooltip().contains("Gold bracelet")) {
                        Widgets.get(CRAFTING_WIDGET0, CRAFTING_WIDGET0_CRAFT).click(true);
                        Utilities.waitFor(new Condition() {
                            @Override
                            public boolean validate() {
                                return Players.getLocal().getAnimation() == 3243;
                            }
                        }, 1500);
                    }
                }
            }
            if (Calculations.distanceTo(furnace) > 6) {
                Walking.walk(furnace);
            }
            if (!furnace.isOnScreen()) {
                Camera.turnTo(furnace);
            }
        }
    }
}