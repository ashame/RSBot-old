package org.nathantehbeast.scripts.guardKiller.Nodes;

import org.nathantehbeast.api.tools.Logger;
import org.nathantehbeast.scripts.guardKiller.Constants;
import org.nathantehbeast.scripts.guardKiller.GuardKiller;
import org.powerbot.core.script.job.LoopTask;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.wrappers.widget.WidgetChild;
import sk.action.Ability;
import sk.action.ActionBar;
import sk.action.book.AbilityType;
import sk.action.book.BookAbility;
import sk.general.TimedCondition;

/**
 * Created with IntelliJ IDEA.
 * User: Nathan
 * Date: 5/30/13
 * Time: 5:42 AM
 * To change this template use File | Settings | File Templates.
 */
public class AbilityHandler extends LoopTask {

    private BookAbility ability;

    @Override
    public int loop() {
        if (GuardKiller.useAbilities && Players.getLocal().getInteracting() != null && Constants.CASTLE_AREA.contains(Players.getLocal())) {
            if (!ActionBar.isExpanded()) {
                ActionBar.setExpanded(true);
            }
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
                Logger.log("Using Ability: " + ability);
            }
        }
        return 150;
    }

    private BookAbility getAbility(AbilityType type) {
        BookAbility ability = null;
        for (int i = 11; i >= 0; i--) {
            Ability a = ActionBar.getAbilityInSlot(i);
            if (a != null && a instanceof BookAbility && ActionBar.isReady(i) && ((BookAbility) a).getType() == type) {
                ability = (BookAbility) a;
            }
        }
        return ability;
    }

    private int getAdrenaline() {
        return ActionBar.getAdrenaline() / 10;
    }
}
