package inter;

import symbols.Type;
import lexer.Token;

public class Unary extends Op { //单目运算符

   public Expr expr;

   public Unary(Token tok, Expr x) {   // -x，注意！是通过Not实现的
      super(tok, null);  expr = x;
      type = Type.max(Type.Int, expr.type);
      if (type == null ) error("type error");
   }

   public Expr gen() { return new Unary(op, expr.reduce()); }

   public String toString() { return op.toString()+" "+expr.toString(); }
}

