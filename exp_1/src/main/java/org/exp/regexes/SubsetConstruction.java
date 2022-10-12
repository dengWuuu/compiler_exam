package org.exp.regexes;


import org.exp.FA.DFA;
import org.exp.FA.State;
import org.exp.FA.TNFA;
import org.exp.utils.RelationshipEdge;
import org.jgrapht.graph.DirectedPseudograph;

import java.util.ArrayDeque;
import java.util.HashMap;

import static org.exp.Test.symbols;


public class SubsetConstruction {

    public HashMap<Integer, State> epsilonClosure(State s, DirectedPseudograph<State, RelationshipEdge> tb) {
        if (!tb.vertexSet().contains(s)) { //if vertex s not in the transition table
            return null;
        }

        HashMap<Integer, State> nfaStates = new HashMap<>();

        ArrayDeque<State> queue = new ArrayDeque<>();
        queue.add(s);
        while (!queue.isEmpty()) {
            State state = queue.poll();
            if (nfaStates.get(state.getId()) == null) {
                nfaStates.put(state.getId(), state);
            }

            for (RelationshipEdge e : tb.edgeSet()) {
                if (tb.getEdgeSource(e).getId() == state.getId() && e.getLabel() == 'ε') {
                    queue.add(tb.getEdgeTarget(e));
                }
            }
        }
        return nfaStates;
    }

    public HashMap<Integer, State> move(State s, char ch, DirectedPseudograph<State, RelationshipEdge> tb) {
        HashMap<Integer, State> nfaStates = new HashMap<>();
        for (RelationshipEdge e : tb.edgeSet()) {
            if (tb.getEdgeSource(e) == s && e.getLabel() == ch) {
                nfaStates.put(tb.getEdgeTarget(e).getId(), tb.getEdgeTarget(e));
            }
        }
        return nfaStates;
    }

    public DFA subSetConstruct(TNFA tnfa) {

        DirectedPseudograph<State, RelationshipEdge> tb = tnfa.getTransitTable();
        State startState = tnfa.getStartState();
        State acceptState = tnfa.getAcceptState();

        HashMap<Integer, State> StartDFAStates = new HashMap<Integer, State>();
        StartDFAStates = epsilonClosure(startState, tb); // DFA start state is a NFA state set constructed by epsilonClosure operation on NFA start state

        DFA dfa = new DFA(); //build a DFA

        ArrayDeque<HashMap<Integer, State>> queue = new ArrayDeque<>(); //hold NFA state sets (i.e. the DFA states) produced by epsilonClosure(move)
        queue.add(StartDFAStates);

        HashMap<Integer, State> nfaSateSet;
        while (!queue.isEmpty()) {
            nfaSateSet = queue.poll();

            //take the nfaStateSet as a DFA state
            State SourceDfaState = new State();
            //judge the dfaState whether is start state, middle state or accepting state
            if (nfaSateSet.get(startState.getId()) != null) {
                SourceDfaState.setType(0);
            } else if (nfaSateSet.get(acceptState.getId()) != null) {
                SourceDfaState.setType(2);
            } else {
                SourceDfaState.setType(1);
            }

            //add to map
            dfa.setDFA2NFAStates(SourceDfaState, nfaSateSet);
            //add to transit table
            dfa.getTransitTable().addVertex(SourceDfaState);

            HashMap<Integer, State> moveNfaSet = new HashMap<>();
            for (char ch : symbols) {
                HashMap<Integer, State> aNfaStateSet = new HashMap<>();
                for (State s : nfaSateSet.values()) {
                    moveNfaSet = move(s, ch, tb); //move operation
                    HashMap<Integer, State> epsilonClosureNfaSet = new HashMap<>();
                    for (State ns : moveNfaSet.values()) {
                        aNfaStateSet.putAll(epsilonClosure(ns, tb));
                    }
                }

                /**
                 *
                 * if aNfaStateSet is not in the DFA.DFA2NFAStates, then add a new state into DFA.transitTable and an edge with
                 */
                State targetDfaState = new State();

                boolean bool = false; // aNfaStateSet not exists in the DFA.DFA2NFAStates.
                for (HashMap<Integer, State> hs : dfa.getDfa2nfaSates().values()) {
                    if (hs.equals(aNfaStateSet)) { //aNfaStateSet already exists
                        bool = true;
                        aNfaStateSet = hs;
                        break;
                    }
                }

                if (!bool) {
                    queue.add(aNfaStateSet);
                    //judge the dfaState whether is start state, middle state or accepting state
                    if (nfaSateSet.get(startState.getId()) != null) {
                        targetDfaState.setType(0);
                    } else if (nfaSateSet.get(acceptState.getId()) != null) {
                        targetDfaState.setType(2);
                    } else {
                        targetDfaState.setType(1);
                    }
                    //add to map
                    dfa.setDFA2NFAStates(targetDfaState, aNfaStateSet);
                    //add to transit table
                    dfa.getTransitTable().addVertex(targetDfaState);
                } else {
                    for (State s : dfa.getDfa2nfaSates().keySet()) {
                        if (dfa.getDfa2nfaSates().get(s) == aNfaStateSet) {
                            targetDfaState = s;
                            break;
                        }
                    }
                }
                dfa.getTransitTable().addEdge(SourceDfaState, targetDfaState, new RelationshipEdge(ch));
            }
        }
        return dfa;
    }
}
