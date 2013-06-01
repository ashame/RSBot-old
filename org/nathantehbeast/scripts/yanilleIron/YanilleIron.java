package org.nathantehbeast.scripts.yanilleIron;

import org.nathantehbeast.api.framework.XScript;
import org.nathantehbeast.api.tools.Utilities;
import org.powerbot.core.script.Script;
import org.powerbot.game.api.Manifest;

import javax.swing.*;
import java.awt.*;

@Manifest(
        authors             = "NathanTehBeast",
        name                = "YanilleIron",
        description         = "Support for this script has been discontinued. See Nathan's AIO Miner.",
        version             = 1.3,
        instances           = 10,
        website             = "http://www.powerbot.org/community/topic/976673-yanilleiron-actionbar-dropping-powermining-support-banking-support/"
)

public class YanilleIron extends XScript implements Script {

    @Override
    protected boolean setup() {
        final int confirm = JOptionPane.showConfirmDialog(null, "Support for this script has officially been discontinued. Would you like to " +
                "view the thread of the script replacing it?", "Notice", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            Utilities.openURL("http://www.powerbot.org/community/topic/1012215-free-nathans-aio-miner-powermining-actionbar-dropping/");
        }
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void paint(Graphics g) {
    }

    @Override
    public void poll() {
    }

    @Override
    public void exit() {
    }
}