package parser.inter;

import parser.lexer.Token;
import parser.symbols.Type;

public class Arith extends Op { //算术表达式

	   public Expr expr1, expr2;
	   /* tok算术运算符*/
	   public Arith(Token tok, Expr x1, Expr x2)  {
	      super(tok, null); //null类型占位符
	      expr1 = x1; expr2 = x2;
	      type = Type.max(expr1.type, expr2.type); //确定类型
	      if (type == null ) error("type error");
	   }

	   public Expr gen() {
	      return new Arith(op, expr1.reduce(), expr2.reduce());
	   }

	   public String toString() {
	      return expr1.toString()+" "+op.toString()+" "+expr2.toString();
	   }
	}
