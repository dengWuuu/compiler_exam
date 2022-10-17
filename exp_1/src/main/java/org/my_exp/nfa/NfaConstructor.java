package org.my_exp.nfa;


import lombok.Data;
import org.my_exp.node.Pair;

@Data
public class NfaConstructor {
	private NfaManager nfaManager;

	public NfaConstructor() {
		nfaManager = new NfaManager();
	}

	/**
	 * 构建*
	 * @param pairIn
	 * @return
	 */
	public Pair constructStarClosure(Pair pairIn) {
		Pair pairOut = new Pair();
		pairOut.startNode = nfaManager.newNfa();
		pairOut.endNode = nfaManager.newNfa();

		pairOut.startNode.next1 = pairIn.startNode;
		pairIn.endNode.next1 = pairOut.endNode;

		pairOut.startNode.next2 = pairOut.endNode;
		pairIn.endNode.next2 = pairIn.startNode;

		pairIn.startNode = pairOut.startNode;
		pairIn.endNode = pairOut.endNode;

		return pairOut;
	}

	/**
	 * TODO 构建+（拓展）
	 * @param pairIn
	 * @return
	 */
	public Pair constructPlusClosure(Pair pairIn) {
		Pair pairOut = new Pair();

		pairOut.startNode = nfaManager.newNfa();
		pairOut.endNode = nfaManager.newNfa();

		pairOut.startNode.next1 = pairIn.startNode;
		pairIn.endNode.next1 = pairOut.endNode;

		pairIn.endNode.next2 = pairOut.startNode;

		pairIn.startNode = pairOut.startNode;
		pairIn.endNode = pairOut.endNode;

		return pairOut;
	}

	/**
	 * 构建字符
	 * @param c
	 * @return
	 */
	public Pair constructNfaForSingleCharacter(char c) {

		Pair pairOut = new Pair();
		pairOut.startNode = nfaManager.newNfa();
		pairOut.endNode = nfaManager.newNfa();
		pairOut.startNode.next1 = pairOut.endNode;
		pairOut.startNode.setType(c);

		return pairOut;
	}

	/**
	 * 构建|
	 * @param left
	 * @param right
	 * @return
	 */
	public Pair constructNfaForOR(Pair left, Pair right) {
		Pair pair = new Pair();
		pair.startNode = nfaManager.newNfa();
		pair.endNode = nfaManager.newNfa();

		pair.startNode.next1 = left.startNode;
		pair.startNode.next2 = right.startNode;

		left.endNode.next1 = pair.endNode;
		right.endNode.next1 = pair.endNode;

		return pair;
	}

	/**
	 * 构建。
	 * @param left
	 * @param right
	 * @return
	 */
	public Pair constructNfaForConnector(Pair left, Pair right) {
		Pair pairOut = new Pair();
		pairOut.startNode = left.startNode;
		pairOut.endNode = right.endNode;

		left.endNode.next1 = right.startNode;

		return pairOut;
	}
}
