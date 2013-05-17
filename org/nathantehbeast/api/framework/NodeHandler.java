package org.nathantehbeast.api.framework;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: Nathan
 * Date: 5/7/13
 * Time: 3:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class NodeHandler {
    private static final List<Node> nodes = new LinkedList<Node>();

    private final int corePoolSize = 4;
    private final int maxPoolSize = 4;
    private final long timeout = 5000;
    private ExecutorService es = new ThreadPoolExecutor(
            corePoolSize,
            maxPoolSize,
            timeout,
            TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>()
    );

    public final void provide(Node... jobs) {
        for (final Node job : jobs) {
            if (!nodes.contains(job)) {
                nodes.add(job);
            }
        }
    }

    public void executeNode(Node n) {
        Thread t = new Thread(n);
        es.execute(t);
    }

    public List<Node> getNodes() {
        return nodes;
    }

}
