package parser.inter;

import parser.lexer.Token;

public class And extends Logical {
	public And(Token tok, Expr x1, Expr x2) { super(tok,x1,x2);}
	
	public void jumping(int t, int f){}

}
