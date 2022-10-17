package org.my_exp.nfa;

import lombok.Data;
import org.my_exp.node.Node;

import java.util.Stack;

@Data
public class NfaManager {
	private final Node[] nfaStatesArr;
    private final Stack<Node> nfaStack;
    private int nextAlloc = 0; 
    private int nfaStates = 0; 
    
    public NfaManager()  {
		int NFA_MAX = 256;
		nfaStatesArr = new Node[NFA_MAX];
    	for (int i = 0; i < NFA_MAX; i++) {
    		nfaStatesArr[i] = new Node();
    	}
    	
    	nfaStack = new Stack<>();
    	
    }
    public Node newNfa()  {
    	Node nfa;
    	if (nfaStack.size() > 0) {
    		nfa = nfaStack.pop();
    	}
    	else {
    		nfa = nfaStatesArr[nextAlloc];
    		nextAlloc++;
    	}
    	
    	nfa.clearState();
    	nfa.setState(nfaStates++);
    	nfa.setType(Node.EPSILON);
    	
    	return nfa;
    }
}
