package parser.inter;

import parser.lexer.Num;
import parser.lexer.Token;
import parser.lexer.Word;
import parser.symbols.Type;

public class Id extends Expr { // 叶子节点，仅一个地址
	
	public int offset; //id 的相对地址
	// id,p 对应 Expr的op, type
	public Id(Word id, Type p, int b) { super(id,p); offset=b; }
}
