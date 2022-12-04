package parser.lexer;

public class Num extends Token {
	public final int lue;
	public Num(int v) { super(Tag.NUM); lue = v; }
	public String toString() { return ""+lue; }
}
