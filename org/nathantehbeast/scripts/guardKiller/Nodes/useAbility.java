package org.nathantehbeast.scripts.guardKiller.Nodes;

import org.nathantehbeast.scripts.guardKiller.GuardKiller;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Game;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.util.Filter;
import sk.action.ActionBar;
import sk.action.BarNode;

import static org.nathantehbeast.scripts.guardKiller.GuardKiller.useAbilities;

public class useAbility extends Node {

    @Override
    public boolean activate() {
        return useAbilities && Players.getLocal().isInCombat() && Players.getLocal().getInteracting() != null && Game.getClientState() == Game.INDEX_MAP_LOADED;
    }

    @Override
    public void execute() {
        GuardKiller.currentNode = this;
        if (!ActionBar.isOpen()) {
            ActionBar.makeReady();
        }
        final BarNode ability = ActionBar.getNode(new Filter<BarNode>() {
            @Override
            public boolean accept(BarNode barNode) {
                return barNode.canUse() && barNode.isValid();
            }
        });
        if (ability != null) {
            System.out.println("Using "+ ability.toString());
            ability.use();

        }
    }
}