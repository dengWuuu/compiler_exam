package org.exp.FA;

import org.exp.utils.RelationshipEdge;
import org.jgrapht.graph.DirectedPseudograph;

public class FA {
    protected State startState;
    protected DirectedPseudograph<State, RelationshipEdge> transitTable = new DirectedPseudograph<>(RelationshipEdge.class);

    public FA() {
        this.startState = new State(0);
    }

    public State getStartState() {
        return startState;
    }

    public void setStartState(State startState) {
        this.startState = startState;
    }

    public DirectedPseudograph<State, RelationshipEdge> getTransitTable() {
        return this.transitTable;
    }

    public void merge(FA fa) {
        for (RelationshipEdge edge : fa.getTransitTable().edgeSet()) {
            this.transitTable.addVertex(fa.getTransitTable().getEdgeTarget(edge));
            this.transitTable.addVertex(fa.getTransitTable().getEdgeSource(edge));
            this.transitTable.addEdge(fa.getTransitTable().getEdgeSource(edge), fa.getTransitTable().getEdgeTarget(edge), new RelationshipEdge(edge.getLabel()));
        }
    }

    public void showNFA() {
        String stateInfo = "Start State:" + this.startState.getId();
        System.out.println(stateInfo);
        System.out.println("the transitTable is: \r");
        String edgeInfo;
        for (RelationshipEdge edge : transitTable.edgeSet()) {
            edgeInfo = "(" + this.transitTable.getEdgeSource(edge).getId() + ":" + this.transitTable.getEdgeSource(edge).getType()
                    + "->"
                    + this.transitTable.getEdgeTarget(edge).getId() + ":" + this.transitTable.getEdgeTarget(edge).getType() + " @ " + edge.getLabel() + ")";
            System.out.println(edgeInfo);
        }
    }
}
