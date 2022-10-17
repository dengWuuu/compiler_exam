package org.my_exp.nfa;

import lombok.Data;
import org.my_exp.node.Node;
import org.my_exp.print.ConsoleTable;
import org.my_exp.node.Pair;

import java.util.*;

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
    public NFA(String re) throws Exception {
        this.re = re;
        reJoined = null;
        rePostfix = null;
        Set<Character> temp = new HashSet<>();
        //记录正则表达式的字母
        for (int i = 0; i < this.re.length(); i++) {
            if (isLetter(this.re.charAt(i))) {
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
        //如果两个符号是union的就用.来表示
        this.addUnionSymbol();
        //获取这个字符串的后缀表达式
        this.postfix();
    }

    /**
     * 符号之间加入.
     */
    public void addUnionSymbol() {
        int length = re.length();
        if (length == 1) {
            System.out.println("add join symbol:" + re);
            reJoined = re;
            return;
        }
        int return_string_length = 0;
        //字符与字符之间union的话用.来表示的字符串
        char[] reJoinStr = new char[2 * length + 2];
        char first, second = '0';
        //遍历加入如果union就加入.
        for (int i = 0; i < length - 1; i++) {
            first = re.charAt(i);
            second = re.charAt(i + 1);
            reJoinStr[return_string_length++] = first;
            if (first != '(' && first != '|' && isLetter(second)) {
                reJoinStr[return_string_length++] = '.';
            } else if (second == '(' && first != '|' && first != '(') {
                reJoinStr[return_string_length++] = '.';
            }
        }
        reJoinStr[return_string_length++] = second;
        String rString = new String(reJoinStr, 0, return_string_length);
        System.out.println("add join symbol:" + rString);
        reJoined = rString;
    }

    private boolean isLetter(char check) {
        return check >= 'a' && check <= 'z' || check >= 'A' && check <= 'Z';
    }

    /**
     * 将字符串变成后缀表达式 get the str postfix
     */
    public void postfix() throws Exception {
        reJoined = reJoined + "#";
        //双向队列模拟栈，转化为后缀表达式的工具
        Deque<Character> s = new LinkedList<>();
        char ch = '#', ch1, op; //特殊符号表示截止
        s.push(ch);
        StringBuilder postfixStrBuilder = new StringBuilder();
        int idx = 0;
        ch = reJoined.charAt(idx++);
        //开始构造后缀表达式
        while (!s.isEmpty()) {
            //是字符直接加入后缀表达式
            if (isLetter(ch)) {
                postfixStrBuilder.append(ch);
                ch = reJoined.charAt(idx++);
            } else {
                ch1 = s.peek();
                if (isp(ch1) < icp(ch)) {
                    s.push(ch);
                    ch = reJoined.charAt(idx++);
                } else if (isp(ch1) > icp(ch)) {
                    op = s.pop();
                    postfixStrBuilder.append(op);
                } else {
                    op = s.pop();
                    //如果是（还可以继续 如果是#表示结束
                    if (op == '(') ch = reJoined.charAt(idx++);
                }
            }
            //错误处理
            if (postfixStrBuilder.length() >= 2 && postfixStrBuilder.charAt(postfixStrBuilder.length() - 1) == '*' && postfixStrBuilder.charAt(postfixStrBuilder.length() - 2) == '*') {
                System.out.println("正则表达式错误");
                throw new Exception();
            }
        }
        for (int i = 0; i < postfixStrBuilder.length(); i++) {
            if (postfixStrBuilder.charAt(i) == '(' || postfixStrBuilder.charAt(i) == ')') {
                System.out.println("正则表达式错误");
                throw new Exception();
            }
        }
        System.out.println("postfix:" + postfixStrBuilder);
        System.out.println();
        rePostfix = postfixStrBuilder.toString();
    }

    /**
     * 网上借鉴的判断优先级的简单方法 judge priority
     */
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
        //Stack用来存储每一个NFA首尾节点
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

    private void restate(Node startNfa) {
        if (startNfa == null || startNfa.isVisited()) {
            return;
        }
        startNfa.setVisited();
        startNfa.setState(restate++);
        restate(startNfa.next1);
        restate(startNfa.next2);
    }

    private void revisit(Node startNfa) {
        if (startNfa == null || !startNfa.isVisited()) {
            return;
        }
        startNfa.setUnVisited();
        revisit(startNfa.next1);
        revisit(startNfa.next2);
    }

    private void printNfa(Node startNfa) {
        if (startNfa == null || startNfa.isVisited()) {
            return;
        }

        startNfa.setVisited();

        printNfaNode(startNfa);
        if (startNfa.next1 != null) {
            table.appendRow();
        }
        printNfa(startNfa.next1);
        printNfa(startNfa.next2);
    }

    private void printNfaNode(Node node) {
        if (node.next1 != null) {
            table.appendColum(node.getState());
            if (node.getType() == -1) {
                for (int i = 0; i < letters.length - 2; i++) {
                    table.appendColum(" ");
                }
                if (node.next2 != null)
                    table.appendColum("{" + node.next1.getState() + "," + node.next2.getState() + "}");
                else
                    table.appendColum("{" + node.next1.getState() + "}");
            } else {
                int index = getIndex("" + (char) node.getType());
                for (int i = 0; i < letters.length - 1; i++) {
                    if (i != index)
                        table.appendColum(" ");
                    else {
                        if (node.next2 != null)
                            table.appendColum("{" + node.next1.getState() + "," + node.next2.getState() + "}");
                        else
                            table.appendColum("{" + node.next1.getState() + "}");
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
