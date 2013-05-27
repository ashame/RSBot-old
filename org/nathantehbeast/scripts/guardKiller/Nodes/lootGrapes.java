package org.nathantehbeast.scripts.guardKiller.Nodes;

import org.nathantehbeast.api.tools.Calc;
import org.nathantehbeast.api.tools.MCamera;
import org.nathantehbeast.scripts.guardKiller.GuardKiller;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Calculations;
import org.powerbot.game.api.methods.node.GroundItems;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.wrappers.node.GroundItem;

import static org.nathantehbeast.scripts.guardKiller.GuardKiller.GRAPES;
import static org.nathantehbeast.scripts.guardKiller.GuardKiller.grapesLooted;

public class lootGrapes extends Node {

    @Override
    public boolean activate() {
        GroundItem grape = GroundItems.getNearest(GRAPES);
        return grape != null && !Inventory.isFull() && Calculations.distanceTo(grape) < 8;
    }

    @Override
    public void execute() {
        GuardKiller.currentNode = this;
        System.out.println("Looting Grapes.");
        GroundItem grape = GroundItems.getNearest(GRAPES);
        if (grape != null) {
            if (Calc.isOnScreen(grape)) {
                int gB = Inventory.getCount(GRAPES);
                if (grape.interact("Take", "Grapes")) {
                    while (Inventory.getCount(GRAPES) < gB) {
                        sleep(300);
                    }
                    grapesLooted++;
                }
            } else {
                MCamera.turnTo(grape, 5);
            }
        }
        grape = null;
    }
}
