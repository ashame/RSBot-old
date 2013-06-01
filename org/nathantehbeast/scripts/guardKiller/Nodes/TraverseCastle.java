package org.nathantehbeast.scripts.guardKiller.Nodes;

import org.nathantehbeast.api.framework.XNode;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.wrappers.Area;
import org.powerbot.game.api.wrappers.map.TilePath;

/**
 * Created with IntelliJ IDEA.
 * User: Nathan
 * Date: 5/30/13
 * Time: 11:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class TraverseCastle implements XNode {

    private final Area castleArea;
    private final TilePath path;

    public TraverseCastle(final Area castleArea, final TilePath path) {
        this.castleArea = castleArea;
        this.path = path;
    }

    @Override
    public boolean activate() {
        return !Inventory.isFull()
                && !castleArea.contains(Players.getLocal().getLocation())
                && Players.getLocal().getHealthPercent() > 50;
    }

    @Override
    public void execute() {
        path.traverse();
    }
}
