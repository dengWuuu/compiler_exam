package parser.inter;

import parser.lexer.Num;
import parser.lexer.Token;
import parser.lexer.Word;
import parser.symbols.Type;

public class Temp extends Expr{
	static int count = 0; 
	int number = 0;//用于记录临时地址的编号
	// 构造 t 时确定其 类型 p
	public Temp(Type p) { super(Word.temp,p); number = ++count; }
	
	public String toString() {return "t"+number;}
}
