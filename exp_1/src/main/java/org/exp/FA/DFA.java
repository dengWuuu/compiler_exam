package org.exp.FA;

import java.util.HashMap;

/**
 * DFA: a subclass of class FA; holds the mapping of DFA state to NFA state set
 *
 * @author xuyang
 */
public class DFA extends FA {
    private final HashMap<State, HashMap<Integer, State>> dfa2nfaSates = new HashMap<>(); //holds the map between DFA states and NFA state sets

    public DFA() {
    }

    public DFA(State startState) {
        this.startState = startState;
        this.getTransitTable().addVertex(this.startState);
    }
    public void setDFA2NFAStates(State s, HashMap<Integer, State> nfaStates) {
        this.dfa2nfaSates.put(s, nfaStates);
    }

    public HashMap<State, HashMap<Integer, State>> getDfa2nfaSates() {
        return dfa2nfaSates;
    }
}
