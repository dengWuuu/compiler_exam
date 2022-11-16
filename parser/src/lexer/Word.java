package lexer;

public class Word extends Token {
	public String lexeme = ""; //词素
	public Word(String s, int tag) { super(tag); lexeme = s; } //构造函数 (k,v)
	public String toString() {return lexeme; }
	
	public static final Word //定义decaf运算符，以区别实现语言所使用的这些符号
		and = new Word( "&&", Tag.AND ),  or = new Word( "||", Tag.OR ),
	    eq  = new Word( "==", Tag.EQ  ),  ne = new Word( "!=", Tag.NE ),
	    le  = new Word( "<=", Tag.LE  ),  ge = new Word( ">=", Tag.GE ),
	
	    minus  = new Word( "minus", Tag.MINUS ),
	    True   = new Word( "true",  Tag.TRUE  ),
	    False  = new Word( "false", Tag.FALSE ),
	    temp   = new Word( "t",     Tag.TEMP  );
}