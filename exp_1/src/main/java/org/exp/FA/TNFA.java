package org.exp.FA;

/**
 * need to be refactored as a subclass of class FA!!!
 *
 */

public class TNFA extends FA{

    //private State startState;
    private State acceptState;
    //private DirectedPseudograph<State, RelationshipEdge> transitTable = new DirectedPseudograph<>(RelationshipEdge.class);

    public TNFA(){
        this.startState = new State(0);
        this.acceptState = new State(2);
        transitTable.addVertex(this.startState);
        transitTable.addVertex(this.acceptState);
    }

//    public DirectedPseudograph<State, RelationshipEdge> getTransitTable() {
//        return this.transitTable;
//    }

//    public State getStartState() {
//        return this.startState;
//    }

    public State getAcceptState() {
        return this.acceptState;
    }

    public void setAcceptState(State acceptState) {
        this.acceptState = acceptState;
    }

//    public void setStartState(State startState) {
//        this.startState = startState;
//    }

//    public void merge(TNFA aTnfa) {
//        //Collection<RelationshipEdge> edges = aTnfa.getTransitTable().edgeSet();
//        for (RelationshipEdge edge : aTnfa.getTransitTable().edgeSet()) {
//            this.transitTable.addVertex(aTnfa.getTransitTable().getEdgeTarget(edge));
//            this.transitTable.addVertex(aTnfa.getTransitTable().getEdgeSource(edge));
//            this.transitTable.addEdge(aTnfa.getTransitTable().getEdgeSource(edge),aTnfa.getTransitTable().getEdgeTarget(edge), new RelationshipEdge(edge.getLabel()));
//        }
//    }

//    public void showNFA(){
//        String stateInfo = "Start State:" + this.startState.getId() + "<>" + "Accepting State:" + this.acceptState.getId();
//        System.out.println(stateInfo);
//        System.out.println("the transitTable is: \r");
//        String edgeInfo;
//        for (RelationshipEdge edge : transitTable.edgeSet()){
//            edgeInfo = "(" + this.transitTable.getEdgeSource(edge).getId() + ":" + this.transitTable.getEdgeTarget(edge).getId() + " @ " + edge.getLabel() + ")\r";
//            System.out.println(edgeInfo);
//        }
//    }
}
