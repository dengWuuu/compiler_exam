package parser.inter;

import parser.lexer.Num;
import parser.lexer.Token;
import parser.lexer.Word;
import parser.symbols.Type;

public class Op extends Expr{ 
	public Op(Token tok, Type p) { super(tok,p); }
	// �ṩһ��reduceʵ��
	public Expr reduce()
	{
		Expr x = gen();
		Temp t = new Temp(type);
		emit(t.toString()+" = "+x.toString());
		return t;
	}
}
