package org.nathantehbeast.scripts.aiominer.nodes;

import org.nathantehbeast.api.framework.Condition;
import org.nathantehbeast.api.framework.XNode;
import org.nathantehbeast.api.tools.Logger;
import org.nathantehbeast.api.tools.Utilities;
import org.powerbot.game.api.methods.Calculations;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.interactive.Player;

/**
 * Created with IntelliJ IDEA.
 * User: Nathan
 * Date: 5/28/13
 * Time: 10:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class EscapeCombat implements XNode {

    private Tile startTile;
    private Player player;

    public EscapeCombat(final Tile startTile) {
        this.startTile = startTile;
    }

    @Override
    public boolean activate() {
        return (player = Players.getLocal()) != null && player.isInCombat();
    }

    @Override
    public void execute() {
        Logger.log("Uh oh, we're in combat. Ain't nobody got time fo' dat!");
        final Timer t = new Timer(30000);
        while (Players.getLocal().isInCombat() && t.isRunning()) {
            try {
                Walking.walk(new Tile(player.getLocation().getX() + Random.nextInt(3, 6), player.getLocation().getY() + Random.nextInt(3, 6), player.getLocation().getPlane()));
            } catch (Exception e) {
                Logger.log("An error occurred while fleeing.");
            }
        }
        if (!Players.getLocal().isInCombat()) {
            Logger.log("Successfully fled from combat.");
            Walking.walk(startTile);
            Utilities.waitFor(new Condition() {
                @Override
                public boolean validate() {
                    return Calculations.distanceTo(startTile) < 3;
                }
            }, 15000);
        }
    }
}
