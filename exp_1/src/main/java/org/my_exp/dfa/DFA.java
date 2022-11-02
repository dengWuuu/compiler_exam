package org.my_exp.dfa;

import org.my_exp.node.Node;
import org.my_exp.print.ConsoleTable;
import org.my_exp.node.Pair;

import java.util.*;
import java.util.Map.Entry;


public class DFA {
    private final Pair pair;
    private final String[] letter;
    private final ConsoleTable table;
    //通过集合快速找到对应状态
    private final Map<Set<Integer>, Integer> map;
    private Set<Integer> tempSet;
    private final Queue<Integer> queue = new LinkedList<>();
    //每一次划分出的不同DFA状态记录
    private final List<Character[]> dfa = new ArrayList<>();
    //接收状态
    private final List<Character> endState = new ArrayList<>();

    //转成DFA的初始状态
    private int state = 'A';

    /**
     * 初始化DFA
     *
     * @param pair   NFA的结束状态和开始状态
     * @param letter 表达式
     */
    public DFA(Pair pair, String[] letter) {
        this.pair = pair;
        this.letter = letter;
        table = new ConsoleTable(letter.length - 1, true);
        table.appendRow();
        //构造输出表格
        for (int i = 0; i < letter.length - 1; i++) {
            table.appendColum(letter[i]);
        }
        map = new HashMap<>();
    }

    public List<Character[]> getDFA() {
        List<Character[]> redfa = new ArrayList<>();
        for (Character[] ch : dfa) {
            if (getSet(ch[0]) == null || Objects.requireNonNull(getSet(ch[0])).isEmpty()) {
                continue;
            } else {
                Character[] newCh = new Character[ch.length];
                for (int i = 0; i < ch.length; i++) {
                    if (ch[i] == null) continue;
                    Set<Integer> set = getSet(ch[i]);
                    if (set == null || set.isEmpty()) newCh[i] = null;
                    else newCh[i] = ch[i];
                }
                redfa.add(newCh);
            }
        }
        return redfa;
    }

    public List<Character> getEndState() {
        return endState;
    }

    public String[] getLetter() {
        return letter;
    }

    public void printDFA() {
        System.out.println();
        System.out.println("--------DFA--------");
        System.out.print(table);
        for (Entry<Set<Integer>, Integer> entry : map.entrySet()) {
            if (entry.getValue() == -1) continue;
            System.out.println((char) entry.getValue().intValue() + " = " + entry.getKey() + (isStart(entry.getKey()) ? " START " : "") + (isEnd(entry.getKey()) ? " END " : ""));
        }
        System.out.println("--------DFA--------");
    }

    private boolean isStart(Set<Integer> set) {
        for (Integer integer : set) {
            if (integer == pair.startNode.getState()) return true;
        }
        return false;
    }

    private boolean isEnd(Set<Integer> set) {
        for (Integer integer : set) {
            if (integer == pair.endNode.getState()) {
                endState.add(((char) getCharacter(set).intValue()));
                return true;
            }
        }
        return false;
    }

    public void createDFA() {
        tempSet = new HashSet<>();
        //寻找空集合闭包作为DFA开始状态, -1代表空状态
        Set<Integer> start = move(pair.startNode, -1);
        //集合放入HashMap中储存
        map.put(start, state);
        //队列储存已有状态
        queue.add(state++);

        while (!queue.isEmpty()) {
            Character[] dfaLine = new Character[letter.length - 1]; //记录有什么状态
            int character = queue.poll();
            table.appendRow();
            table.appendColum((char) character);
            dfaLine[0] = (char) character;
            Set<Integer> set = getSet(character);
            //判断每个字母可以到达的状态
            for (int i = 1; i < letter.length - 1; i++) {
                tempSet = new HashSet<>();
                Set<Integer> midSet = new HashSet<>();
                assert set != null;
                for (Integer state : set) {
                    //获取从startNode通过state能到的节点
                    Node node = getNode(pair.startNode, state);
                    //深度优先搜索后要状态清零
                    revisit();
                    if (node == null) {
                        continue;
                    } else if ((char) node.getType() == letter[i].charAt(0)) {
                        midSet.add(node.next1.getState());
                    }
                }
                for (Integer integer : midSet) {
                    Node node = getNode(pair.startNode, integer);
                    revisit();
                    //找空闭包
                    move(node, -1);
                }
                Integer c = getCharacter(tempSet);
                //不存在此状态则创建新状态
                if (c == null) {
                    if (tempSet.isEmpty()) {
                        map.put(tempSet, -1);
                        table.appendColum("null");
                        dfaLine[i] = null;
                    } else {
                        queue.add(state);
                        table.appendColum((char) state);
                        dfaLine[i] = (char) state;
                        map.put(tempSet, state++);
                    }
                } else {
                    if (c == -1) {
                        table.appendColum("null");
                        dfaLine[i] = null;
                    } else {
                        dfaLine[i] = (char) c.intValue();
                        table.appendColum((char) c.intValue());
                    }
                }
            }
            dfa.add(dfaLine);
        }
    }

    /**
     * 对应书本的move操作并且找到空集合闭包
     * @param startNode
     * @param i
     * @return
     */
    private Set<Integer> move(Node startNode, int i) {
        closure(startNode, i);
        revisit();
        return tempSet;
    }

    private void closure(Node node, int i) {
        if (node == null || node.isVisited())
            return;
        node.setVisited();
        tempSet.add(node.getState());
        if (node.getType() == -1 || node.getType() == i) {
            closure(node.next1, i);
            closure(node.next2, i);
        }
    }

    private Node getNode(Node node, int startsState) {
        if (node == null || node.isVisited()) return null;
        node.setVisited();
        if (node.getState() == startsState) return node;
        if (node.getState() > startsState) return null;
        Node temp1 = getNode(node.next1, startsState);
        Node temp2 = getNode(node.next2, startsState);
        if (temp1 != null) return temp1;
        return temp2;
    }

    private Integer getCharacter(Set<Integer> set) {
        return map.get(set);
    }

    private Set<Integer> getSet(int character) {
        for (Entry<Set<Integer>, Integer> m : map.entrySet()) {
            if (m.getValue() == character) return m.getKey();
        }
        return null;
    }

    private void revisit(Node node) {
        if (node == null || !node.isVisited()) {
            return;
        }
        node.setUnVisited();
        revisit(node.next1);
        revisit(node.next2);
    }

    private void revisit() {
        pair.startNode.setUnVisited();
        revisit(pair.startNode.next1);
        revisit(pair.startNode.next2);
    }

    @Override
    public String toString() {
        return tempSet.toString();
    }
}
