package parser.inter;

import parser.lexer.Num;
import parser.lexer.Token;
import parser.lexer.Word;
import parser.symbols.Type;
public class Constant extends Expr {

	   public Constant(Token tok, Type p) { super(tok, p); }
	   public Constant(int i) { super(new Num(i), Type.Int); }

	   public static final Constant
	      True  = new Constant(Word.True,  Type.Bool),
	      False = new Constant(Word.False, Type.Bool);

	   public void jumping(int t, int f) {}
	}

