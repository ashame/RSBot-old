package org.nathantehbeast.scripts.yanilleIron.Nodes;

import org.powerbot.core.randoms.Login;
import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Environment;
import org.powerbot.game.api.methods.Game;
import org.powerbot.game.api.methods.Widgets;

import static org.nathantehbeast.scripts.yanilleIron.YanilleIron.world;

/**
 * Created with IntelliJ IDEA.
 * User: Nathan
 * Date: 5/11/13
 * Time: 3:15 AM
 * To change this template use File | Settings | File Templates.
 */
public class LoginHandler extends Node {
    @Override
    public boolean activate() {
        return Widgets.get(550).getChild(18).getText().substring(26) != null && Integer.parseInt(Widgets.get(550).getChild(18).getText().substring(26)) != world;
    }

    @Override
    public void execute() {
        getContainer().submit(new Task() {
            @Override
            public void execute() {
                sleep(5000);
                Environment.enableRandom(Login.class, false);
            }
        });
        Game.logout(true);
    }
}
