package lexer;

public class Tag {
    public final static int //tag标识定义
            AND = 256, BASIC = 257, BREAK = 258, DO = 259, ELSE = 260,
            EQ = 261, FALSE = 262, GE = 263, ID = 264, IF = 265,
            INDEX = 266, LE = 267, MINUS = 268, NE = 269, NUM = 270,
            OR = 271, REAL = 272, TEMP = 273, TRUE = 274, WHILE = 275, FOR = 276;
}

/*
 * tag<------->token
 * 1、类型type: 分为基本类型BASIC 和数组类型INDEX
 * BASIC ----->  (int float char bool)
 * INDEX -----> []
 *
 * 2、stmt结构：
 * DO -----> do
 *  WHILE -----> while
 * BREAK -----> break
 * IF -----> if
 * ELSE -----> else
 *
 * 3、逻辑运算符
 * AND ----->&&
 * OR -----> II
 *
 * 4、关系运算符
 * EQ -----> ==
 * GE -----> >=
 * LE -----> <=
 * NE -----> !=
 *
 * 5、逻辑值
 * TRUE ----->true
 * FALSE -----> false
 *
 * 6、数值
 * NUM -----> 整数
 * REAL -----> 小数
 *
 * 7、标识符（变量）
 * ID ----->
 *
 * 8、其它
 * Minus -----> -
 * TEMP -----> ? *
 *
 * */
