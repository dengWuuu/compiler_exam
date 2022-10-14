package org.exp.FA;

/**
 * 状态类
 *
 * @author xuyang
 */
public class State {
    private int id;
    /**
     * 0-start；1-middle；2-accept
     */
    private int type = 1;

    public static int STATE_ID = 0;

    public State() {
        this.id = State.STATE_ID;
        STATE_ID += 1;
    }

    public State(int type) {
        this.id = State.STATE_ID;
        this.type = type;
        STATE_ID += 1;
    }

    public int getId() {
        return id;
    }

    public int getType() {
        return type;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setType(int type) {
        this.type = type;
    }
}
