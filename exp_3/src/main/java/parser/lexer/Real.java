package parser.lexer;

public class Real extends Token {
	public final float lue;
	public Real(float v) { super(Tag.REAL); lue = v; }
	public String toString() { return ""+lue; }

}
