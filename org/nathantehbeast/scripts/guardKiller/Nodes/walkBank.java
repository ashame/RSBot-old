package org.nathantehbeast.scripts.guardKiller.Nodes;

import org.nathantehbeast.api.tools.Utilities;
import org.nathantehbeast.scripts.guardKiller.GuardKiller;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Game;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.tab.Inventory;

import static org.nathantehbeast.scripts.guardKiller.GuardKiller.*;

public class walkBank extends Node {

    @Override
    public boolean activate() {
        if (foodId > 0 && Inventory.getCount(GRAPES) == 28 && Game.getClientState() == Game.INDEX_MAP_LOADED) {
            return true;
        }
        if (foodId == 0 && (Inventory.isFull() || Players.getLocal().getHealthPercent() < 30)) {
            return true;
        }
        return false;
    }

    @Override
    public void execute() {
        GuardKiller.currentNode = this;
        Utilities.walkPath(PATH, true, true);
    }
}