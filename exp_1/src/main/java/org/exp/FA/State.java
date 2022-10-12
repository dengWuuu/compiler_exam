package org.exp.FA;

public class State {
    private int id;
    private int type = 1;//0-start；1-middle；2-accept

    public static int STATEID = 0;

    public State(){
        this.id = State.STATEID;
        STATEID += 1 ;
    }

    public State(int type){
        this.id = State.STATEID;
        this.type = type;
        STATEID += 1 ;
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
