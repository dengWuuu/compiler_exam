package org.exp.regexes;


import org.exp.FA.TNFA;
import org.exp.utils.PatternTree;
import org.exp.utils.PatternTreeNode;

import java.util.ArrayDeque;

public class Pattern {
    //regex
    private String id;
    private String regex;
    private PatternTree regexTree;

    //NFA
    private TNFA nfa;

    public String getId() {
        return id;
    }

    public String getRegex() {
        return regex;
    }

    public PatternTree getRegexTree() {
        return regexTree;
    }

    public void setNfa(TNFA nfa) {
        this.nfa = nfa;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    /**
     * convert the regex into a regex tree
     *
     * @author xuyang
     */
    public void compile() {
        System.out.println("2) Convert the regex into a regex tree;");
        ParseRegex parser = new ParseRegex(this.regex);
        this.regexTree = parser.parse(); //convert regex to regex tree
        printTree(); //print regex tree.
        System.out.println("3) Convert the regex tree into a NFA.");
        toNFA();
        this.nfa.showNFA();
    }

    /**
     * Thompson construction
     *
     * @author xuyang
     */
    private void toNFA() {
        System.out.println("3.1) build the NFA by Thompson construction. ");
        ThompsonConstruction thompsonConstruction = new ThompsonConstruction();
        this.nfa = thompsonConstruction.translate(this.regexTree.getRoot());
        System.out.println("3.2) show the structure of the NFA. ");
    }

    /**
     * level-order traverse the tree to show the structure of the tree
     *
     * @author xuyang
     */
    private void printTree() {
        System.out.println("\t2.2) Show the structure of the regex tree. ");

        System.out.println("regexID:" + this.id + "\tregex:" + this.regex + "\tRegexTree:" + "\r");

        PatternTreeNode root = regexTree.getRoot();
        if (root != null) {
            ArrayDeque<PatternTreeNode> queue = new ArrayDeque<>();
            queue.add(root);
            PatternTreeNode node = queue.poll();

            while (node != null) {
                System.out.println("(" + node.getValue() + ")" + "->" + node.getType() + "\r");
                PatternTreeNode childnode = node.getFirstChild();

                if (childnode != null) {
                    System.out.println("\tfirstChild:(" + childnode.getValue() + ")" + "->" + childnode.getType() + "\r");
                    queue.add(childnode);
                    childnode = childnode.getNextSibling();

                    while (childnode != null) {
                        System.out.println("\t(" + childnode.getValue() + ")" + "->" + childnode.getType() + "\r");
                        queue.add(childnode);
                        childnode = childnode.getNextSibling();
                    }
                }
                node = queue.poll();
            }
        }
    }
}
