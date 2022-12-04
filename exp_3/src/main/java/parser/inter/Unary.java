package parser.inter;

import parser.lexer.Num;
import parser.lexer.Token;
import parser.lexer.Word;
import parser.symbols.Type;

public class Unary extends Op { //��Ŀ�����

   public Expr expr;

   public Unary(Token tok, Expr x) {   // -x��ע�⣡��ͨ��Notʵ�ֵ�
      super(tok, null);  expr = x;
      type = Type.max(Type.Int, expr.type);
      if (type == null ) error("type error");
   }

   public Expr gen() { return new Unary(op, expr.reduce()); }

   public String toString() { return op.toString()+" "+expr.toString(); }
}
