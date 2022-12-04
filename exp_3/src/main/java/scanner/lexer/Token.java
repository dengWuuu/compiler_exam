package scanner.lexer;

public class Token {

	public final int tag;

	// 每个Token有一个相应的Tag
	public Token(int t) {
		tag = t;
	}

	public String toString() {
		return "" + (char) tag;
	}
}
