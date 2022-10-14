package org.exp.utils;

import java.util.Stack;

public class PatternTreeNode {
    private Character value;
    /*
        0-basic；1-concatenation；2-union； 3-kleene closure; 4-leftParenthesis; 5-rightParenthesis
     */
    private int type;
    private PatternTreeNode firstChild;
    private PatternTreeNode nextSibling;

    public PatternTreeNode(Character value, int type, PatternTreeNode firstChild, PatternTreeNode nextSibling) {
        this.value = value;
        this.type = type;
        this.firstChild = firstChild;
        this.nextSibling = nextSibling;
    }

    public Character getValue() {
        return this.value;
    }

    public void setValue(Character value) {
        this.value = value;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return this.type;
    }

    public PatternTreeNode getFirstChild() {
        return this.firstChild;
    }

    public void setFirstChild(PatternTreeNode firstChild) {
        this.firstChild = firstChild;
    }

    public PatternTreeNode getNextSibling() {
        return this.nextSibling;
    }

    public void setNextSibling(PatternTreeNode nextSibling) {
        this.nextSibling = nextSibling;
    }

    /**
     * To find the last child node of a node in the tree implemented with "firstChild" and "nextSibling"
     *
     * @return the last child node of the node n
     */
    public PatternTreeNode getLastChild() {
        PatternTreeNode theNode = this.getFirstChild();
        if (theNode != null) { //the firstChild is not the last child.
            while (theNode.getNextSibling() != null) {
                theNode = theNode.getNextSibling();
            }
        }
        return theNode;
    }

    /**
     * Make the nodes in a stack as its children.
     *
     * @param stack
     */
    public void mergeStackAsOneChild(Stack<PatternTreeNode> stack) {
        PatternTreeNode temp1 = stack.pop();
        PatternTreeNode firstChild = temp1;
        while (!stack.isEmpty()) {
            PatternTreeNode temp2 = stack.pop();
            temp1.setNextSibling(temp2);
            temp1 = temp1.getNextSibling();
        }
        if (this.firstChild == null)
            this.setFirstChild(firstChild);
        else {
            PatternTreeNode temp = this.getLastChild();
            temp.setNextSibling(firstChild);
        }
    }

}
