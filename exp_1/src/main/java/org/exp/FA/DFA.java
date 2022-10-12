package org.exp.FA;

import java.util.HashMap;

/**
 * DFA: a subclass of class FA; holds the mapping of DFA state to NFA state set
 * @author xuyang
 */
public class DFA extends FA{
    //private State startState;
    //private HashMap<Integer,State> acceptStates = new HashMap<>();
    //private DirectedPseudograph<State, RelationshipEdge> transitTable = new DirectedPseudograph<>(RelationshipEdge.class);
    //private HashMap<Integer,HashMap<Integer,State>> dfa2nfaSates; //holds the map between DFA states and NFA state sets
    private HashMap<State,HashMap<Integer,State>> dfa2nfaSates = new HashMap<>(); //holds the map between DFA states and NFA state sets
    public DFA(){}

    public DFA(State startState){
        this.startState = startState;
        this.getTransitTable().addVertex(this.startState);
    }

//    public void setStartState(State startState) {
//        this.startState = startState;
//    }

//    public State getStartState() {
//        return startState;
//    }

    public void setDFA2NFAStates(State s, HashMap<Integer,State> nfaStates){
        this.dfa2nfaSates.put(s,nfaStates);
    }

    public HashMap<State, HashMap<Integer, State>> getDfa2nfaSates() {
        return dfa2nfaSates;
    }

//    public DirectedPseudograph<State, RelationshipEdge> getTransitTable(){
//        return this.transitTable;
//    }

//    public void showDFA(){
//        String stateInfo = "Start State:" + this.startState.getId() ;
//        System.out.println(stateInfo);
//        System.out.println("the transitTable is: \r");
//        String edgeInfo;
//        for (RelationshipEdge edge : transitTable.edgeSet()){
//            edgeInfo = "(" + this.transitTable.getEdgeSource(edge).getId() + ':' +  this.transitTable.getEdgeSource(edge).getType() +  "->" + this.transitTable.getEdgeTarget(edge).getId() + ':' + this.transitTable.getEdgeTarget(edge).getType() + " @ " + edge.getLabel() + ")\r";
//            System.out.println(edgeInfo);
//        }
//    }
}
