package org.my_exp.nfa;

import lombok.Data;
import org.my_exp.print.ConsoleTable;
import org.my_exp.simple.Cell;
import org.my_exp.simple.Pair;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

/**
 * NFA类
 *
 * @author Wu
 */
@Data
public class NFA {

    private int restate = 0;

    private final String re;
    private String reJoined;
    private String rePostfix;

    private final String[] letters;
    private Pair pair;

    private final ConsoleTable table;

    /**
     * 初始化NFA
     *
     * @param re
     */
    public NFA(String re) {
        this.re = re;
        reJoined = null;
        rePostfix = null;
        Set<Character> temp = new HashSet<>();
        //记录正则表达式的字母
        for (int i = 0; i < this.re.length(); i++) {
            if (is_letter(this.re.charAt(i))) {
                temp.add(this.re.charAt(i));
            }
        }

        //下面这些是为了构造出输出方便debug+展示结果
        letters = new String[temp.size() + 2];
        Object[] tempObj = temp.toArray();
        int i = 0;

        //空为了构造第0个空白格.....
        letters[i] = "";
        for (; i < tempObj.length; i++) {
            letters[i + 1] = (char) tempObj[i] + "";
        }
        letters[i + 1] = "EPSILON";

        //第一行表示通过这些letters中的一个letter可以到达哪个点
        table = new ConsoleTable(letters.length, true);
        table.appendRow();
        for (String string : letters) {
            table.appendColum(string);
        }
        //TODO 分析这两是干嘛的
        this.addJoinSymbol();
        this.postfix();
    }

    public void addJoinSymbol() {
        int length = re.length();
        if (length == 1) {
            System.out.println("add join symbol:" + re);
            reJoined = re;
            return;
        }
        int return_string_length = 0;
        char[] return_string = new char[2 * length + 2];
        char first, second = '0';
        for (int i = 0; i < length - 1; i++) {
            first = re.charAt(i);
            second = re.charAt(i + 1);
            return_string[return_string_length++] = first;
            if (first != '(' && first != '|' && is_letter(second)) {
                return_string[return_string_length++] = '.';
            } else if (second == '(' && first != '|' && first != '(') {
                return_string[return_string_length++] = '.';
            }
        }
        return_string[return_string_length++] = second;
        String rString = new String(return_string, 0, return_string_length);
        System.out.println("add join symbol:" + rString);
        System.out.println();
        reJoined = rString;
    }

    private boolean is_letter(char check) {
        return check >= 'a' && check <= 'z' || check >= 'A' && check <= 'Z';
    }

    public void postfix() {
        reJoined = reJoined + "#";

        Stack<Character> s = new Stack<>();
        char ch = '#', ch1, op;
        s.push(ch);
        String out_string = "";
        int read_location = 0;
        ch = reJoined.charAt(read_location++);
        while (!s.empty()) {
            if (is_letter(ch)) {
                out_string = out_string + ch;
                ch = reJoined.charAt(read_location++);
            } else {
                ch1 = s.peek();
                if (isp(ch1) < icp(ch)) {
                    s.push(ch);
                    ch = reJoined.charAt(read_location++);
                } else if (isp(ch1) > icp(ch)) {
                    op = s.pop();
                    out_string = out_string + op;
                } else {
                    op = s.pop();
                    if (op == '(')
                        ch = reJoined.charAt(read_location++);
                }
            }
        }
        System.out.println("postfix:" + out_string);
        System.out.println();
        rePostfix = out_string;
    }

    private int isp(char c) {
        return switch (c) {
            case '#' -> 0;
            case '(' -> 1;
            case '*', '+' -> 7;
            case '.' -> 5;
            case '|' -> 3;
            case ')' -> 8;
            default -> -1;
        };
    }

    private int icp(char c) {
        return switch (c) {
            case '#' -> 0;
            case '(' -> 8;
            case '*', '+' -> 6;
            case '.' -> 4;
            case '|' -> 2;
            case ')' -> 1;
            default -> -1;
        };
    }

    public void re2nfa() {
        pair = new Pair();
        Pair temp;
        Pair right, left;
        NfaConstructor constructor = new NfaConstructor();
        char[] ch = rePostfix.toCharArray();
        Stack<Pair> stack = new Stack<>();
        for (char c : ch) {
            switch (c) {
                case '|' -> {
                    right = stack.pop();
                    left = stack.pop();
                    pair = constructor.constructNfaForOR(left, right);
                    stack.push(pair);
                }
                case '*' -> {
                    temp = stack.pop();
                    pair = constructor.constructStarClosure(temp);
                    stack.push(pair);
                }
                case '+' -> {
                    temp = stack.pop();
                    pair = constructor.constructPlusClosure(temp);
                    stack.push(pair);
                }
                case '.' -> {
                    right = stack.pop();
                    left = stack.pop();
                    pair = constructor.constructNfaForConnector(left, right);
                    stack.push(pair);
                }
                default -> {
                    pair = constructor.constructNfaForSingleCharacter(c);
                    stack.push(pair);
                }
            }
        }
    }

    public void print() {
        restate(this.pair.startNode);
        revisit(this.pair.startNode);
        System.out.println("--------NFA--------");
        table.appendRow();
        printNfa(this.pair.startNode);
        System.out.print(table);
        revisit(this.pair.startNode);
        System.out.println("--------NFA--------");
        System.out.println("start state: " + (this.pair.startNode.getState()));
        System.out.println("end state: " + (this.pair.endNode.getState()));
    }

    private void restate(Cell startNfa) {
        if (startNfa == null || startNfa.isVisited()) {
            return;
        }
        startNfa.setVisited();
        startNfa.setState(restate++);
        restate(startNfa.next);
        restate(startNfa.next2);
    }

    private void revisit(Cell startNfa) {
        if (startNfa == null || !startNfa.isVisited()) {
            return;
        }
        startNfa.setUnVisited();
        revisit(startNfa.next);
        revisit(startNfa.next2);
    }

    private void printNfa(Cell startNfa) {
        if (startNfa == null || startNfa.isVisited()) {
            return;
        }

        startNfa.setVisited();

        printNfaNode(startNfa);
        if (startNfa.next != null) {
            table.appendRow();
        }
        printNfa(startNfa.next);
        printNfa(startNfa.next2);
    }

    private void printNfaNode(Cell node) {
        if (node.next != null) {
            table.appendColum(node.getState());
            if (node.getType() == -1) {
                for (int i = 0; i < letters.length - 2; i++) {
                    table.appendColum(" ");
                }
                if (node.next2 != null)
                    table.appendColum("{" + node.next.getState() + "," + node.next2.getState() + "}");
                else
                    table.appendColum("{" + node.next.getState() + "}");
            } else {
                int index = getIndex("" + (char) node.getType());
                for (int i = 0; i < letters.length - 1; i++) {
                    if (i != index)
                        table.appendColum(" ");
                    else {
                        if (node.next2 != null)
                            table.appendColum("{" + node.next.getState() + "," + node.next2.getState() + "}");
                        else
                            table.appendColum("{" + node.next.getState() + "}");
                    }
                }
            }
        } else {
            table.appendColum(node.getState());
            table.appendColum(" ");
            table.appendColum(" ");
            table.appendRow();
        }
    }

    //“”,a,b,EPS
    private int getIndex(String ch) {
        for (int i = 0; i < letters.length; i++) {
            if (letters[i].equals(ch))
                return i - 1;
        }
        return -1;
    }

}
