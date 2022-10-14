package org.exp.regexes;


import org.exp.utils.PatternTree;
import org.exp.utils.PatternTreeNode;

import java.util.ArrayDeque;
import java.util.Stack;

/**
 * Convert regex to regex tree
 * 1) regex: basic case- 26 English alphabet and ε ; operators：| union；- concatenation ；* kleene closure; ()
 * 2）regex tree: leaf- basic case; non-leaf - operator
 *
 * @author xuyang
 */

public class ParseRegex {
    private final ArrayDeque<Character> queue = new ArrayDeque<>();

    public ParseRegex(String regex) {
        char[] ch = regex.toCharArray();
        for (char c : ch) {
            this.queue.add(c);
        }
        //%-end of the regex
        this.queue.add('%');
    }

    /**
     * 1) For convenience, the char '%' representing the end of the input is appended to the input regex.
     * 2) Set a stack to hold the nodes of regex tree
     * 3) The variable "look" hold the next input char
     *
     * @return the regex tree
     * @author xuyang
     */
    public PatternTree parse() {
        System.out.println("\t2.1) Convert the regex into a tree.");
        if (this.queue.isEmpty()) return null;
        PatternTree tree = new PatternTree();
        Stack<PatternTreeNode> stack = new Stack<>();

        //lookahead char
        char look = this.queue.poll();

        if (!Character.isLetter(look) && look != 'ε' && look != '(') {
            //The first char must be a letter, ε or '('
            System.out.println("not a legal regex!(It must begin with a letter,ε or (.)");
            return null;
        } else if (look == '%') {
            System.out.println("a NULL regex!");
            return null;
        }

        int t;
        if (Character.isLetter(look) || look == 'ε')
            t = 0;
        else t = 4;
        PatternTreeNode node = new PatternTreeNode(look, t, null, null);
        stack.push(node);

        look = this.queue.poll();
        while (look != '%') {
            if (look == '*') {
                PatternTreeNode knode;
                t = stack.peek().getType();
                if (t == 0) {// is basic before *
                    knode = new PatternTreeNode('*', 3, stack.pop(), null);
                } else if (t == 5) { //is a ')' before *
                    stack.pop(); //pop ')'
                    knode = new PatternTreeNode('*', 3, stack.pop(), null);
                    stack.pop(); //pop '('
                } else { //is other char before *, not legal
                    System.out.println("not a legal regex!(It must be ')");
                    return null;
                }
                stack.push(knode);
            } else if (look == '(') {
                PatternTreeNode lnode = new PatternTreeNode('(', 4, null, null);
                stack.push(lnode);
                //how about the case of "...(..." (right parenthesis is missing)
            } else if (look == ')') {
                if (stack.isEmpty()) {
                    return null;
                }

                //t = stack.peek().getType();
                if (stack.peek().getType() == 4) { // case: ()
                    stack.pop();
                } else {
                    Stack<PatternTreeNode> rstack = new Stack<>();
                    while (!stack.isEmpty() && stack.peek().getType() != 4) {
                        if (stack.peek().getType() == 0 || stack.peek().getType() == 1 || stack.peek().getType() == 3) {//basic,kleene or concatenation(the case of conca exist?)
                            rstack.push(stack.pop());
                        } else if (stack.peek().getType() == 2) {//union, case (stack|?)
                            if (rstack.size() == 0) { //case (stack|)
                                System.out.println("not a legal regex '|)'");
                                return null;
                            }

                            // case (?|...)
                            PatternTreeNode unode = stack.pop();
                            if (rstack.size() > 1) { //case (stack|...)
                                PatternTreeNode cnode = new PatternTreeNode('-', 1, null, null);
                                cnode.mergeStackAsOneChild(rstack);//
                                unode.getLastChild().setNextSibling(cnode);
                            } else { //size = 1, case (stack|.)
                                unode.getLastChild().setNextSibling(rstack.pop());
                            }
                            rstack.push(unode);

                        } else if (stack.peek().getType() == 5) { //case ...(stack))
                            stack.pop(); //pop ')'
                            rstack.push(stack.pop());
                            stack.pop(); // pop '('
                        }
                    }

                    if (stack.isEmpty()) {
                        System.out.println("not a legal regex ('(' is missing.)");
                        return null;
                    } else if (stack.peek().getType() == 4) { //case (rstack)
                        // 1) convert the nodes in rstack into one node
                        // 2) push the converted node and the right-parenthesis node
                        if (!rstack.isEmpty()) {
                            if (rstack.size() > 1) {
                                PatternTreeNode cnode = new PatternTreeNode('-', 1, null, null);
                                cnode.mergeStackAsOneChild(rstack);
                                stack.push(cnode);

                            } else { //rstack.size = 1
                                stack.push(rstack.pop());
                            }
                            PatternTreeNode rnode = new PatternTreeNode(')', 5, null, null);
                            stack.push(rnode);
                        } else {
                            stack.pop();
                        }
                    }
                }
            } else if (look == '|') {
                t = stack.peek().getType();
                if (t == 4 || t == 2) {
                    System.out.println("not a legal regex('(| or ||').");
                    return null;
                }

                PatternTreeNode unode;
                Stack<PatternTreeNode> ustack = new Stack<>();
                while (!stack.isEmpty() && stack.peek().getType() != 2 && stack.peek().getType() != 4) {
                    ustack.push(stack.pop());
                }

                if (stack.isEmpty() || stack.peek().getType() == 4) {
                    unode = new PatternTreeNode('|', 2, null, null);
                    PatternTreeNode firstChildNode;
                    if (ustack.size() == 1) {
                        firstChildNode = ustack.pop();
                    } else if (ustack.size() > 1) {
                        PatternTreeNode cnode = new PatternTreeNode('-', 1, null, null);
                        cnode.mergeStackAsOneChild(ustack);
                        firstChildNode = cnode;
                    } else {
                        System.out.println("not a legal regex(in considering look='|').");
                        return null;
                    }
                    unode.setFirstChild(firstChildNode);
                } else { //type=2
                    unode = stack.pop();
                    PatternTreeNode lastNode = unode.getLastChild();
                    if (ustack.size() == 1) {
                        lastNode.setNextSibling(ustack.pop());
                    } else {
                        PatternTreeNode cnode = new PatternTreeNode('-', 1, null, null);
                        cnode.mergeStackAsOneChild(ustack);
                        lastNode.setNextSibling(cnode);
                    }
                }
                stack.push(unode);
            } else if (Character.isLetter(look) || look == 'ε') {
                PatternTreeNode bnode = new PatternTreeNode(look, 0, null, null);
                stack.push(bnode);
            }

            look = this.queue.poll();
        }

        //if look == '%'
        if (stack.isEmpty()) {
            return null;
        }
        Stack<PatternTreeNode> pstack = new Stack<>();
        while (!stack.isEmpty() && stack.peek().getType() != 2) {
            pstack.push(stack.pop());
        }

        if (stack.isEmpty()) {
            while (!pstack.isEmpty())
                stack.push(pstack.pop());
        } else if (stack.peek().getType() == 2) {
            if (pstack.peek().getType() == 5) {
                while (!pstack.isEmpty()) {
                    stack.push(pstack.pop());
                }
            } else {
                PatternTreeNode unode;
                unode = stack.pop();
                if (pstack.size() == 1) {
                    unode.getLastChild().setNextSibling(pstack.pop());
                } else {
                    PatternTreeNode cnode = new PatternTreeNode('-', 1, null, null);
                    cnode.mergeStackAsOneChild(pstack);
                    unode.getLastChild().setNextSibling(cnode);
                }
                stack.push(unode);
            }
        }

        // merge the nodes in stack as one node
        Stack<PatternTreeNode> treeStack = new Stack<>();
        while (!stack.isEmpty()) {
            if (stack.peek().getType() == 4 || stack.peek().getType() == 5) {
                stack.pop();
            } else
                treeStack.push(stack.pop());
        }

        if (treeStack.isEmpty()) {
            return null;
        }

        if (treeStack.size() > 1) {
            PatternTreeNode treeNode = new PatternTreeNode('-', 1, null, null);
            treeNode.mergeStackAsOneChild(treeStack);
            tree.setRoot(treeNode);
        } else {
            tree.setRoot(treeStack.pop());
        }

        return tree;
    }
}
