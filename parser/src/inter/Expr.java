package inter;

import symbols.Type;
import lexer.Token;

public class Expr extends Node{ //表达式节点

	public Token op;  // 操作符
	public Type type; //类型
	
	Expr(Token tok, Type p) { op = tok; type = p; }
	
	public Expr gen() { return this;} //产生三地址码的右部 比如 E = E1+E2，产生return x1+x2 （x1,x2分别对应E1、E2的值的地址）
	public Expr reduce() { return this;} //返回表达式地址（常量、id或临时变量名），比如t（hold the value of E）
	
	/* boolean表达式的跳转代码实现，
	 * 参数t表示label true，f表示 label false
	 * label 0 表示布尔表达式后的第一条指令语句标号
	 */
	public void jumping(int t, int f) {} 	
	public void emitjumps(String test, int t, int f){}
	public String toString() { return op.toString(); }
	
}
