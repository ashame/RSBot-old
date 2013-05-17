package org.nathantehbeast.api.framework;

/**
 * Created with IntelliJ IDEA.
 * User: Nathan
 * Date: 5/7/13
 * Time: 3:18 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class Node implements Condition {
    private Condition condition;
    private Method method;
    private boolean active;

    public Node() {
        this.condition = this;
        if (this instanceof Method) {
            this.method = (Method) this;
        } else {
            throw new IllegalArgumentException("Doesn't implement Method.");
        }
    }

    public Node(final Condition c, final Method m) {
        this.condition = c;
        this.method = m;
    }

    public Condition getCondition() {
        return this.condition;
    }

    public Method getMethod() {
        return this.method;
    }

    public boolean isActive() {
        return active;
    }

    public boolean start() {
        return active = true;
    }

    public boolean stop() {
        return active = false;
    }

}
