package parser.lexer;

public class Tag {
	public final static int //tag��ʶ����
	AND   = 256,  BASIC = 257,  BREAK = 258,  DO   = 259, ELSE  = 260,
    EQ    = 261,  FALSE = 262,  GE    = 263,  ID   = 264, IF    = 265,
    INDEX = 266,  LE    = 267,  MINUS = 268,  NE   = 269, NUM   = 270,
    OR    = 271,  REAL  = 272,  TEMP  = 273,  TRUE = 274, WHILE = 275,
	FOR = 276;
}

/*
 * tag<------->token
 * 1������type: ��Ϊ��������BASIC ����������INDEX
 * BASIC ----->  (int float char bool)
 * INDEX -----> [] 
 * 
 * 2��stmt�ṹ��
 * DO -----> do
 *  WHILE -----> while
 * BREAK -----> break
 * IF -----> if
 * ELSE -----> else
 * 
 * 3���߼������
 * AND ----->&&
 * OR -----> II
 * 
 * 4����ϵ�����
 * EQ -----> ==
 * GE -----> >=
 * LE -----> <=
 * NE -----> !=
 * 
 * 5���߼�ֵ
 * TRUE ----->true
 * FALSE -----> false
 * 
 * 6����ֵ
 * NUM -----> ����
 * REAL -----> С��
 * 
 * 7����ʶ����������
 * ID ----->
 * 
 * 8������
 * Minus -----> -
 * TEMP -----> ? * 
 * 
 * */
