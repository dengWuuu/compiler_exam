package lexer;

public class Token {
	public final int tag; //token类型标识
	public Token(int t) { tag = t; }
	public String toString() { return ""+(char)tag; } // 获取 token对象的tag标识
}

/*
 * 三类token： Word, Num, Real：
 * */
