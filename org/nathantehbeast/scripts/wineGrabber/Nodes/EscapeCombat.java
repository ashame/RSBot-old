package org.nathantehbeast.scripts.wineGrabber.Nodes;

import org.nathantehbeast.api.framework.XNode;
import org.nathantehbeast.scripts.wineGrabber.Constants;
import org.powerbot.core.script.methods.Players;
import org.powerbot.game.api.methods.interactive.NPCs;
import org.powerbot.game.api.util.Filter;
import org.powerbot.game.api.wrappers.interactive.NPC;

/**
 * Created with IntelliJ IDEA.
 * User: Nathan
 * Date: 5/29/13
 * Time: 12:07 AM
 * To change this template use File | Settings | File Templates.
 */
public class EscapeCombat implements XNode {

    private Filter<NPC> aggressive = new Filter<NPC>() {
        @Override
        public boolean accept(NPC npc) {
            return !npc.getInteracting().equals(Players.getLocal());
        }
    };

    @Override
    public boolean activate() {
        return Players.getLocal().isInCombat() && NPCs.getLoaded(aggressive).length > 0;
    }

    @Override
    public void execute() {
        while (NPCs.getLoaded(aggressive).length > 0) {
            Constants.ESCAPE_TEMPLE.traverse();
        }
        Constants.RETURN_TO_TEMPLE.traverse();
    }
}
