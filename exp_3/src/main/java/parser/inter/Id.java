package parser.inter;

import parser.lexer.Num;
import parser.lexer.Token;
import parser.lexer.Word;
import parser.symbols.Type;

public class Id extends Expr { // Ҷ�ӽڵ㣬��һ����ַ
	
	public int offset; //id ����Ե�ַ
	// id,p ��Ӧ Expr��op, type
	public Id(Word id, Type p, int b) { super(id,p); offset=b; }
}
