package org.nathantehbeast.scripts.yanilleIron.Nodes;

import org.nathantehbeast.api.tools.Utilities;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.core.script.methods.Game;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Bank;
import org.powerbot.game.api.wrappers.Entity;

import static org.nathantehbeast.scripts.yanilleIron.YanilleIron.PATH_Y;
import static org.nathantehbeast.scripts.yanilleIron.YanilleIron.pm;

public class walkBankY extends Node {

    @Override
    public boolean activate() {
        Entity bank = Bank.getNearest();
        if (Game.getClientState() == Game.INDEX_MAP_LOADED) {
            if (bank != null) {
                if (!bank.isOnScreen() && !pm && Inventory.isFull()) {
                    return true;
                }
            }
            if (bank == null && !pm && Inventory.isFull()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void execute() {
        Utilities.walkPath(PATH_Y, true, true);
    }
}