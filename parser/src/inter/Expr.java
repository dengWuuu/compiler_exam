package inter;

import symbols.Type;
import lexer.Token;

public class Expr extends Node{ //���ʽ�ڵ�

	public Token op;  // ������
	public Type type; //����
	
	Expr(Token tok, Type p) { op = tok; type = p; }
	
	public Expr gen() { return this;} //��������ַ����Ҳ� ���� E = E1+E2������return x1+x2 ��x1,x2�ֱ��ӦE1��E2��ֵ�ĵ�ַ��
	public Expr reduce() { return this;} //���ر��ʽ��ַ��������id����ʱ��������������t��hold the value of E��
	
	/* boolean���ʽ����ת����ʵ�֣�
	 * ����t��ʾlabel true��f��ʾ label false
	 * label 0 ��ʾ�������ʽ��ĵ�һ��ָ�������
	 */
	public void jumping(int t, int f) {} 	
	public void emitjumps(String test, int t, int f){}
	public String toString() { return op.toString(); }
	
}
