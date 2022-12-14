package parser.inter;

import parser.lexer.Num;
import parser.lexer.Token;
import parser.lexer.Word;
import parser.symbols.Array;
import parser.symbols.Type;

public class SetElem extends Stmt { //???鸳ֵ

   public Id array; public Expr index; public Expr expr;

   public SetElem(Access x, Expr y) {
      array = x.array; index = x.index; expr = y;
      if ( check(x.type, expr.type) == null ) error("type error");
   }

   public Type check(Type p1, Type p2) {
      if ( p1 instanceof Array || p2 instanceof Array ) return null;
      else if ( p1 == p2 ) return p2;
      else if ( Type.numeric(p1) && Type.numeric(p2) ) return p2;
      else return null;
   }

   public void gen(int b, int a) {}
   
   public void display(){
	   emit(" assignment ");
   }
}
