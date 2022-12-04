package parser.inter;

import parser.lexer.Num;
import parser.lexer.Token;
import parser.lexer.Word;
import parser.symbols.Type;

public class For extends Stmt {
    // bool���ʽ����ʾfor�еڶ������ʽ
    Expr expr;
    // ��ʾfor�е�һ������
    Stmt first;
    // ��ʾfor�е��������ʽ
    Stmt second;
    // ��ʾѭ���������
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