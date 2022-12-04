package parser.inter;

import parser.lexer.Num;
import parser.lexer.Token;
import parser.lexer.Word;
import parser.symbols.Type;

public class For extends Stmt {
    // bool表达式，表示for中第二个表达式
    Expr expr;
    // 表示for中第一个变量
    Stmt first;
    // 表示for中第三个表达式
    Stmt second;
    // 表示循环体的内容
    Stmt stmt;

    public For() {
    }

    public void init(Expr expr, Stmt first, Stmt second, Stmt stmt) {
        this.expr = expr;
        this.first = first;
        this.second = second;
        this.stmt = stmt;
    }

    public void display() {
        emit("stmt : for begin");
        stmt.display();
        emit("stmt : for end");
    }

}