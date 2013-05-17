package org.nathantehbeast.scripts.guardKiller.Nodes;

import org.nathantehbeast.api.tools.Utilities;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Game;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.tab.Inventory;

import static org.nathantehbeast.scripts.guardKiller.GuardKiller.CASTLE;
import static org.nathantehbeast.scripts.guardKiller.GuardKiller.PATH;

public class walkCastle extends Node {

    @Override
    public boolean activate() {
        return !Utilities.isAt(CASTLE) && Players.getLocal().getHealthPercent() == 100 && !Inventory.isFull() && Game.getClientState() == Game.INDEX_MAP_LOADED;
    }

    @Override
    public void execute() {
        Utilities.walkPath(PATH, true, false);
    }
}