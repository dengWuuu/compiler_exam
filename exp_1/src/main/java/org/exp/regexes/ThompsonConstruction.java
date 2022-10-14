package org.exp.regexes;


import org.exp.FA.TNFA;
import org.exp.utils.PatternTreeNode;
import org.exp.utils.RelationshipEdge;

import java.util.ArrayDeque;

/**
 * An Implementation of Thompson Construction
 *
 * @Author xuyang
 */
public class ThompsonConstruction {

    public TNFA translate(PatternTreeNode node) {

        if (node == null) { //root = null
            System.out.println("The regex tree is null.");
            return null;
        }
        int type = node.getType();

        TNFA tnfa = new TNFA();
        if (type == 0) {
            //base case
            //add edge to tnfa，the label is node.getValue()
            tnfa.getTransitTable().addEdge(tnfa.getStartState(), tnfa.getAcceptState(), new RelationshipEdge(node.getValue()));
            return tnfa;
        } else if (type == 3) { //kleene, only one child in its regex tree
            TNFA kNFA = translate(node.getFirstChild());
            kNFA.getStartState().setType(1);
            kNFA.getAcceptState().setType(1);
            tnfa.merge(kNFA);
            tnfa.getTransitTable().addEdge(tnfa.getStartState(), kNFA.getStartState(), new RelationshipEdge('ε'));
            tnfa.getTransitTable().addEdge(kNFA.getAcceptState(), tnfa.getAcceptState(), new RelationshipEdge('ε'));
            tnfa.getTransitTable().addEdge(tnfa.getStartState(), tnfa.getAcceptState(), new RelationshipEdge('ε'));
            tnfa.getTransitTable().addEdge(kNFA.getAcceptState(), kNFA.getStartState(), new RelationshipEdge('ε'));
            return tnfa;
        } else { //conca or union
            ArrayDeque<TNFA> queue = new ArrayDeque<>(); //hold all NFAs of children in the regex tree
            PatternTreeNode enode = node.getFirstChild();
            while (enode != null) { //traverse all children in the regex tree, and build corresponding NFAs
                queue.add(translate(enode));
                enode = enode.getNextSibling();
            }

            if (type == 1) {//concatenation
                TNFA tempNFA1 = queue.poll();
                //tempNFA1.getStartState().setType(1); //set start state as middle state
                tempNFA1.getAcceptState().setType(1);//set accepting state as middle state
                tnfa.merge(tempNFA1);
                //tnfa.getTransitTable().addEdge(tnfa.getStartState(),tempNFA1.getStartState(),new RelationshipEdge('ε'));
                tnfa.setStartState(tempNFA1.getStartState()); //Replace the start state with the start state of the first child node
                TNFA tempNFA2;
                while (!queue.isEmpty()) { //conca the rest NFAs
                    tempNFA2 = queue.poll();
                    tempNFA2.getStartState().setType(1); //开始状态设置为中间状态
                    tempNFA2.getAcceptState().setType(1);//接受状态设置为中间状态

                    tnfa.merge(tempNFA2);
                    tnfa.getTransitTable().addEdge(tempNFA1.getAcceptState(), tempNFA2.getStartState(), new RelationshipEdge('ε'));

                    tempNFA1 = tempNFA2;
                }
                //tnfa.getTransitTable().addEdge(tempNFA1.getAcceptState(),tnfa.getAcceptState(),new RelationshipEdge('ε'));
                tempNFA1.getAcceptState().setType(2);
                tnfa.setAcceptState(tempNFA1.getAcceptState()); //Replace the accepting state with the accepting state of the last child node
            }

            if (type == 2) { //union
                TNFA tempNFA;
                while (!queue.isEmpty()) {
                    tempNFA = queue.poll();
                    tempNFA.getStartState().setType(1);
                    tempNFA.getAcceptState().setType(1);
                    tnfa.merge(tempNFA);
                    tnfa.getTransitTable().addEdge(tnfa.getStartState(), tempNFA.getStartState(), new RelationshipEdge('ε'));
                    tnfa.getTransitTable().addEdge(tempNFA.getAcceptState(), tnfa.getAcceptState(), new RelationshipEdge('ε'));
                }
            }
        }

        return tnfa;
    }
}
