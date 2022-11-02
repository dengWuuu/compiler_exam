package org.my_exp.main;

import java.util.Arrays;
import java.util.Scanner;

import org.my_exp.dfa.DFA;
import org.my_exp.mfa.MFA;
import org.my_exp.nfa.NFA;


public class test {

    public static void main(String[] args) throws Exception {
        NFA nfa = null;
        try {
            Scanner in = new Scanner(System.in);
            System.out.println("input a regular expression");
            String re = in.nextLine();
            System.out.println("re:" + re);
            //初始化NFA
            nfa = new NFA(re);
            //转换成NFA
            nfa.re2nfa();
            //打印
            nfa.print();

            // NFA转成DFA

            DFA dfa = new DFA(nfa.getPair(), nfa.getLetters());
            dfa.createDFA();
            dfa.printDFA();

            MFA mfa = new MFA(dfa.getDFA(), dfa.getEndState(), dfa.getLetter());
            mfa.minimize();
            mfa.merge();
            mfa.printMFA();

            System.out.println();
            System.out.println("re:" + re);
            System.out.println("input test string, Q to exit");
            while (in.hasNextLine()) {
                String string = in.nextLine();
                if (string.equals("Q"))
                    break;
                else
                    mfa.valid(string);
                System.out.println();
                System.out.println("input test string, Q to exit");
            }

        } catch (Exception e) {
            System.out.println("输入正则表达式有问题");
        }
    }
}




