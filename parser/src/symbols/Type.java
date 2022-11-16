package symbols;

import lexer.Tag;
import lexer.Word;

public class Type extends Word { //���Ͷ���
	public int width = 0;
	public Type(String s, int tag, int w) { super(s,tag); width=w; }
	public static final Type //decl��ʹ�õ�Tag
		Int   = new Type( "int",   Tag.BASIC, 4 ),
	    Float = new Type( "float", Tag.BASIC, 8 ),
	    Char  = new Type( "char",  Tag.BASIC, 1 ),
	    Bool  = new Type( "bool",  Tag.BASIC, 1 );
	
	public static boolean numeric(Type p)	{ //�ж������Ƿ�Ϊbool�����Ƿ���true
		if(p==Type.Char||p==Type.Int||p==Type.Float) return true;
		else return false;
	}
	
	public static Type max(Type p1, Type p2){ //�������ȼ�
		if(!numeric(p1)||!numeric(p2)) return null;
		else if(p1==Type.Float||p2==Type.Float) return Type.Float;
		else if(p1==Type.Int||p2==Type.Int) return Type.Int;
		else return Type.Char;
	}
	
}
