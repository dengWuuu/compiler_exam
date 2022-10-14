package org.my_exp.simple;


/**
 * 节点
 */
public class Cell {
    public static final int EPSILON = -1;

    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Cell next;
    public Cell next2;
    private int state;
    private boolean visited = false;

    public void setVisited() {
        visited = true;
    }

    public void setUnVisited() {
        visited = false;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setState(int num) {
        state = num;
    }

    public int getState() {
        return state;
    }

    public void clearState() {
        next = next2 = null;
        state = -1;
    }

    @Override
    public String toString() {
        return (char) type + " " + state + "" + isVisited();
    }
}
