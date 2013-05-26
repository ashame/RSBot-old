package org.nathantehbeast.scripts.guardKiller.Nodes;

import org.nathantehbeast.scripts.guardKiller.GuardKiller;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Game;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.wrappers.widget.WidgetChild;
import sk.action.Ability;
import sk.action.ActionBar;
import sk.action.book.AbilityType;
import sk.action.book.BookAbility;
import sk.general.TimedCondition;

import static org.nathantehbeast.scripts.guardKiller.GuardKiller.useAbilities;

public class useAbility extends Node {

    private static BookAbility ability = null;

    @Override
    public boolean activate() {
        return useAbilities && Players.getLocal().isInCombat() && Players.getLocal().getInteracting() != null && Game.getClientState() == Game.INDEX_MAP_LOADED;
    }

    @Override
    public void execute() {
        GuardKiller.currentNode = this;
        if (getAdrenaline() < 50) {
            ability = getAbility(AbilityType.BASIC);
        }
        if (getAdrenaline() >= 50 && getAdrenaline() < 100) {
            ability = getAbility(AbilityType.THRESHOLD);
        }
        if (getAdrenaline() == 100) {
            ability = getAbility(AbilityType.ULTIMATE);
        }
        final WidgetChild chat = Widgets.get(137, 56);
        if (ability != null) {
            if (chat.getText().equals("[Press Enter to Chat]")) {
                new TimedCondition(1500) {
                    @Override
                    public boolean isDone() {
                        return ActionBar.useAbility(ability);
                    }
                }.waitStop();
            } else {
                new TimedCondition(1500) {
                    @Override
                    public boolean isDone() {
                        return ActionBar.getItemChild(ActionBar.findAbility(ability)).click(true);
                    }
                }.waitStop();
            }
        }
    }

    private final BookAbility getAbility(AbilityType type) {
        BookAbility ability = null;
        for (int i = 11; i >= 0; i--) {
            Ability a = ActionBar.getAbilityInSlot(i);
            if (a != null && a instanceof BookAbility && ActionBar.isReady(i) && ((BookAbility) a).getType() == type) {
                ability = (BookAbility) a;
            }
        }
        return ability;
    }

    private final int getAdrenaline() {
        return ActionBar.getAdrenaline() / 10;
    }
}