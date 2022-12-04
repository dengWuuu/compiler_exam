package parser.inter;

import parser.lexer.Num;
import parser.lexer.Token;
import parser.lexer.Word;
import parser.symbols.Type;

public class If extends Stmt {

   Expr expr; Stmt stmt;

   public If(Expr x, Stmt s) { // if(E)S
      expr = x;  stmt = s;
      if( expr.type != Type.Bool ) expr.error("boolean required in if");
   }

   public void gen(int b, int a) {}
   
   public void display(){
	   emit("stmt : if begin");
	   stmt.display();
	   emit("stmt : if end");
   }
}
