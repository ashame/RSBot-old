package org.nathantehbeast.scripts.guardKiller.Nodes;

import org.nathantehbeast.api.tools.MCamera;
import org.nathantehbeast.api.tools.Utilities;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Calculations;
import org.powerbot.game.api.methods.interactive.NPCs;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.GroundItems;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.wrappers.interactive.NPC;
import org.powerbot.game.api.wrappers.node.GroundItem;

import static org.nathantehbeast.scripts.guardKiller.GuardKiller.*;

public class fightGuards extends Node {

    @Override
    public boolean activate() {
        NPC guard = NPCs.getNearest(GUARDS);
        GroundItem grape = GroundItems.getNearest(GRAPES);
        if (guard != null && Players.getLocal().getHealthPercent() > 50 && Players.getLocal().getInteracting() == null) {
            if (grape != null && Calculations.distanceTo(grape) < 12) {
                return false;
            }
            if (grape != null && Calculations.distanceTo(grape) > 8) {
                return true;
            }
            if (foodId == 0 && !Inventory.isFull()) {
                return true;
            }
            if (foodId != 0 && Inventory.getCount(GRAPES) < 28) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void execute() {
        NPC guard = NPCs.getNearest(GUARDS);
        if (guard != null) {
            if (org.nathantehbeast.api.tools.Calculations.isOnScreen(guard)) {
                try {
                    if (guard.interact("Attack")) {
                        Utilities.waitFor(Players.getLocal().getInteracting() != null, 800);
                    }
                } catch (NullPointerException ne) {
                    System.out.println("Error while attacking guard (possibly already dead?): " + ne.getMessage());
                }
            } else {
                MCamera.turnTo(guard, 5);
            }
        }
        guard = null;
    }
}