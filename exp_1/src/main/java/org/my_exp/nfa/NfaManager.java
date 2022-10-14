package org.my_exp.nfa;

import lombok.Data;
import org.my_exp.simple.Cell;

import java.util.Stack;

@Data
public class NfaManager {
	private final Cell[] nfaStatesArr;
    private final Stack<Cell> nfaStack;
    private int nextAlloc = 0; 
    private int nfaStates = 0; 
    
    public NfaManager()  {
		int NFA_MAX = 256;
		nfaStatesArr = new Cell[NFA_MAX];
    	for (int i = 0; i < NFA_MAX; i++) {
    		nfaStatesArr[i] = new Cell();
    	}
    	
    	nfaStack = new Stack<>();
    	
    }
    public Cell newNfa()  {
    	Cell nfa;
    	if (nfaStack.size() > 0) {
    		nfa = nfaStack.pop();
    	}
    	else {
    		nfa = nfaStatesArr[nextAlloc];
    		nextAlloc++;
    	}
    	
    	nfa.clearState();
    	nfa.setState(nfaStates++);
    	nfa.setType(Cell.EPSILON);
    	
    	return nfa;
    }
}
