package org.my_exp.main;

import java.util.Scanner;

import org.my_exp.nfa.NFA;


public class test {

	public static void main(String[] args) {
		try {
			Scanner in = new Scanner(System.in);
			System.out.println("input a regular expression");
			String re = in.nextLine();
			System.out.println("re:" + re);
			//初始化NFA
			NFA nfa = new NFA(re);
			//转换成NFA
			nfa.re2nfa();
			//打印
			nfa.print();
			in.close();
		}catch (Exception e){
			System.out.println("输入正则表达式有问题");
		}
	}
}




//		DFA dfa = new DFA(nfa.getPair(),nfa.getLetter());
//		dfa.createDFA();
//		dfa.printDFA();
//
//		MFA mfa = new MFA(dfa.getDFA(),dfa.getEndState(),dfa.getLetter());
//		mfa.minimize();
//		mfa.merge();
//		mfa.printMFA();
//
//		System.out.println();
//		System.out.println("re:" + re);
//		System.out.println("input test string, Q to exit");
//		while(in.hasNextLine()) {
//			String string = in.nextLine();
//			if(string.equals("Q"))
//				break;
//			else
//				mfa.valid(string);
//			System.out.println();
//			System.out.println("input test string, Q to exit");
//		}
