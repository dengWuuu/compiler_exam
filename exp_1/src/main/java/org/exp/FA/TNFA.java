package org.exp.FA;

/**
 * need to be refactored as a subclass of class FA!!!
 *
 */
public class TNFA extends FA{
    private State acceptState;

    public TNFA(){
        this.startState = new State(0);
        this.acceptState = new State(2);
        transitTable.addVertex(this.startState);
        transitTable.addVertex(this.acceptState);
    }


    public State getAcceptState() {
        return this.acceptState;
    }

    public void setAcceptState(State acceptState) {
        this.acceptState = acceptState;
    }

}
