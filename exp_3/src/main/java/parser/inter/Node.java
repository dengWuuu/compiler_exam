package parser.inter;

import parser.lexer.Lexer;
import parser.lexer.Num;
import parser.lexer.Token;
import parser.lexer.Word;
import parser.symbols.Type;

public class Node { //�﷨���ڵ�
	int lexline = 0; //���ڱ�����¼�ýڵ��Դ�����к�
	
	Node() {lexline= Lexer.line;}
	
	void error(String s) { throw new Error("near line "+lexline+": "+s); }
	
	static int labels = 0;

	//�����±����䣨����ţ�
	public int newlabel() { return ++labels;} 
	
	//���������
	public void emitlabel(int i) { System.out.print("L"+i+":"); } 
	
	//����м����	
	public void emit(String s) { System.out.println("\t"+s); }
}
