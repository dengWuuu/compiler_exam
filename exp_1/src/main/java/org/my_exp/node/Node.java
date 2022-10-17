package org.my_exp.node;


import lombok.Data;

/**
 * 节点
 */
@Data
public class Node {
    public static final int EPSILON = -1;
    //什么字符能令上一个节点到这一个节点
    private int type;

    public Node next1;
    public Node next2;
    //表示是哪个节点123456
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
        next1 = next2 = null;
        state = -1;
    }

    @Override
    public String toString() {
        return (char) type + " " + state + "" + isVisited();
    }
}
