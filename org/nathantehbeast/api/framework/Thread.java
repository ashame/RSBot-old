package org.nathantehbeast.api.framework;

/**
 * Created with IntelliJ IDEA.
 * User: Nathan
 * Date: 5/7/13
 * Time: 3:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class Thread implements Runnable {
    Node node;

    public Thread(Node n) {
        this.node = n;
    }

    @Override
    public void run() {
        node.start();
        node.getMethod().execute();
        node.stop();
    }
}
