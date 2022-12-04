package parser.inter;

import parser.lexer.Token;
import parser.symbols.Type;

public class Arith extends Op { //�������ʽ

	   public Expr expr1, expr2;
	   /* tok���������*/
	   public Arith(Token tok, Expr x1, Expr x2)  {
	      super(tok, null); //null����ռλ��
	      expr1 = x1; expr2 = x2;
	      type = Type.max(expr1.type, expr2.type); //ȷ������
	      if (type == null ) error("type error");
	   }

	   public Expr gen() {
	      return new Arith(op, expr1.reduce(), expr2.reduce());
	   }

	   public String toString() {
	      return expr1.toString()+" "+op.toString()+" "+expr2.toString();
	   }
	}
