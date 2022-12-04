package scanner.lexer;

// 表示整型
public class Num extends Token {

	public final int value;

	public Num(int v) {
		super(Tag.NUM);
		value = v;
	}

	public String toString() {
		return "" + value;
	}

}
