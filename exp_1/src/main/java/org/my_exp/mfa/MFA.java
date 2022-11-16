package org.my_exp.mfa;

import org.my_exp.print.ConsoleTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;


public class MFA {
    private final List<Character[]> dfa;
    private final List<Character[]> mfa = new ArrayList<>();
    private final String[] letter;
    private final List<Character> endState;
    private final ConsoleTable table;
    private final Set<Set<Character>> totalSet = new HashSet<>();

    private final Map<Character, Character> map = new HashMap<>();

    public MFA(List<Character[]> dfa, List<Character> endState, String[] letter) {
        this.dfa = dfa;
        this.endState = endState;
        this.letter = letter;
        table = new ConsoleTable(letter.length - 1, true);
        table.appendRow();
        for (int i = 0; i < letter.length - 1; i++) {
            table.appendColum(letter[i]);
        }
    }

    /**
     * 最小化
     */
    public void minimize() {
        init(totalSet); //划分终止状态和非终止状态
        int count = 0;
        while (true) {
            if (count == totalSet.size()) break; //都不可分割直接就是最小化了已经
            else count = 0;
            Set<Set<Character>> copyOfTotalSet = new HashSet<>(totalSet);
            for (Set<Character> set : copyOfTotalSet) {
                if (isIndivisible(set)) count++;
                else minimize(set); //划分子集
            }
        }
        merge();//合并
    }

    private void minimize(Set<Character> state) {
        totalSet.remove(state);
        Map<String, String> map = new HashMap<>();
        for (Character character : state) {
            StringBuilder builder = new StringBuilder(); //构建当前状态通过每个字母能到达的状态
            for (int i = 1; i < letter.length - 1; i++) {
                builder.append(move(character, letter[i].charAt(0)));
            }
            //把每个能到相同地方的状态存起来
            String tempSet = map.get(builder.toString());
            if (tempSet == null) {
                map.put(builder.toString(), character + "");
            } else {
                tempSet += character;
                map.put(builder.toString(), tempSet);
            }
        }
        //划分子集的状态加入总状态
        for (String str : map.values()) {
            Set<Character> set = new HashSet<>();
            for (int i = 0; i < str.length(); i++) set.add(str.charAt(i));
            totalSet.add(set);
        }
        System.out.println("这一轮划分的状态：" + totalSet);
    }

    private boolean inTotalSet(Set<Character> temp) {
        if (temp.isEmpty()) return true;
        Set<Integer> indexs = new HashSet<>();
        for (Character character : temp) {
            indexs.add(getSetNumber(character));
        }
        return indexs.size() == 1;
    }

    private int getSetNumber(Character character) {
        int i = 0;
        for (Set<Character> a : totalSet) {
            for (Character b : a) {
                if (b.equals(character))
                    return i;
            }
            i++;
        }
        return -1;
    }

    /**
     * 划分终止状态和非终止状态
     *
     * @param totalSet
     */
    private void init(Set<Set<Character>> totalSet) {
        Set<Character> terminal = new HashSet<>();
        Set<Character> nonTerminal = new HashSet<>();
        for (Character[] characters : dfa) {
            if (isEndState(characters[0])) terminal.add(characters[0]);
            else nonTerminal.add(characters[0]);
        }
        totalSet.add(nonTerminal);
        System.out.println("非接受状态" + nonTerminal);
        totalSet.add(terminal);
        System.out.println("接受状态" + terminal);
    }

    private boolean isEndState(Character character) {
        for (Character state : endState) {
            if (state.equals(character)) return true;
        }
        return false;
    }

    /**
     * 判断子集是否能进行分割
     *
     * @param set
     * @return
     */
    private boolean isIndivisible(Set<Character> set) {
        if (set.size() == 1) return true;
        else {
            for (int i = 1; i < letter.length - 1; i++) {
                Set<Character> temp = new HashSet<>();
                for (Character c : set) {
                    temp.add(move(c, letter[i].charAt(0)));
                }
                if (!inTotalSet(temp)) return false;
            }
        }
        return true;
    }

    public void printMFA() {
        for (Character[] characters : mfa) {
            table.appendRow();
            for (Character character : characters) {
                table.appendColum(character);
            }
        }
        System.out.println("--------MFA--------");
        System.out.print(table);
        System.out.println("start state: [A]");
        System.out.println("end state: " + endState);
        System.out.println("--------MFA--------");
    }

    public void merge() {
        for (Set<Character> characters : totalSet) {
            if (characters.size() != 1) {
                int i = 0;
                char key = 0;
                for (Character ch : characters) {
                    if (i++ == 0) key = ch;
                    else map.put(ch, key);
                }
            }
        }
        List<Character[]> tempMFA = new ArrayList<>();
        for (Character[] characters : dfa) {
            if (ignore(characters[0])) endState.remove(characters[0]);
            else {
                Character[] newChar = new Character[characters.length];
                int i = 0;
                for (Character ch : characters) {
                    if (needReplace(ch)) newChar[i] = map.get(ch);
                    else newChar[i] = ch;
                    i++;
                }
                tempMFA.add(newChar);
            }
        }
        List<Character> finalState = new ArrayList<>();
        for (Character[] ch : tempMFA) {
            if (!finalState.contains(ch[0])) {
                finalState.add(ch[0]);
                mfa.add(ch);
            }
        }
    }

    private boolean needReplace(Character ch) {
        Character value = map.get(ch);
        return value != null;
    }


    private boolean ignore(Character character) {
        for (Entry<Character, Character> m : map.entrySet()) {
            if (m.getKey().equals(character)) return true;
        }
        return false;
    }

    /**
     * move操作
     *
     * @param oriState
     * @param input
     * @return
     */
    private Character move(Character oriState, char input) {
        for (Character[] characters : dfa) {
            if (characters[0] == oriState) {
                int index = getIndex(input);
                return characters[index] == null ? null : characters[index];
            }
        }
        return null;
    }

    private int getIndex(char input) {
        for (int i = 1; i < letter.length - 1; i++) {
            if (letter[i].charAt(0) == input) return i;
        }
        return -1;
    }

    private Character move(char oriState, int index) {
        for (Character[] characters : mfa) {
            if (characters[0] == oriState) return characters[index];
        }
        return null;
    }

    public void valid(String str) {
        Character reachableState = 'A';
        for (int i = 0; i < str.length(); i++) {
            int index = getIndex(str.charAt(i));
            StringBuilder error = new StringBuilder();
            if (index == -1) {
                error.append(str);
                error.append(" is incorrect \r\n");
                for (int q = 0; q < i; q++)
                    error.append(" ");
                error.append("↑");
                System.err.println(error);
                return;
            } else {
                reachableState = move(reachableState, index);
                if (reachableState == null) {
                    error.append(str);
                    error.append(" is incorrect \r\n");
                    error.append(" ".repeat(i));
                    error.append("↑");
                    System.err.println(error);
                    return;
                }
            }
        }
        if (isEndState(reachableState)) {
            System.out.println(str + " is correct");
        } else {
            System.err.println(str + " is incorrect，for the input of ungrammatical symbols \"" + str.charAt(str.length() - 1) + "\"(not finish)");
        }
    }
}
