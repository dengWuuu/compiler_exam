package parser.lexer;

public class Token {
	public final int tag; //token���ͱ�ʶ
	public Token(int t) { tag = t; }
	public String toString() { return ""+(char)tag; } // ��ȡ token�����tag��ʶ
}

/*
 * ����token�� Word, Num, Real��
 * */
