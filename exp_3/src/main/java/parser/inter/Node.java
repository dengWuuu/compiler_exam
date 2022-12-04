package parser.inter;

import parser.lexer.Lexer;
import parser.lexer.Num;
import parser.lexer.Token;
import parser.lexer.Word;
import parser.symbols.Type;

public class Node { //语法树节点
	int lexline = 0; //用于报错，记录该节点的源代码行号
	
	Node() {lexline= Lexer.line;}
	
	void error(String s) { throw new Error("near line "+lexline+": "+s); }
	
	static int labels = 0;

	//创建新标号语句（最大标号）
	public int newlabel() { return ++labels;} 
	
	//输出标号语句
	public void emitlabel(int i) { System.out.print("L"+i+":"); } 
	
	//输出中间代码	
	public void emit(String s) { System.out.println("\t"+s); }
}
