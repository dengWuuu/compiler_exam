package parser.parser;

import parser.inter.*;

import java.io.IOException;

import parser.lexer.*;
import parser.symbols.Array;
import parser.symbols.Env;
import parser.symbols.Type;

public class Parser {

    private final Lexer lex;    // lexical analyzer for this parser
    private Token look;   // lookahead token
    Env top = null;       // current or top symbol table
    int used = 0;         // storage used for declarations

    //���캯��������һ��Lexer���󣬻�ȡnext token
    public Parser(Lexer l) throws IOException {
        lex = l;
        move();
    }

    void move() throws IOException { // next token
        look = lex.scan();
    } //next token

    void error(String s) {
        throw new Error("near line " + Lexer.line + ": " + s);
    }

    void match(int t) throws IOException {
        //����ƥ����ֹ��
        if (look.tag == t) move(); //look token�Ƿ�ƥ��ָ����token����, ���ƥ����next token
        else error("syntax error");
    }

    public void program() throws IOException {  // program -> block
        // build the syntax tree
        Stmt s = block();
        // display the syntax tree
        // only display the stmts, without expr
        s.display();
    }

    Stmt block() throws IOException {  // block -> { decls stmts }
        match('{');
        Env savedEnv = top;  // ��¼��һ��Env
        top = new Env(top); //��ǰEnv
        decls();
        Stmt s = stmts();
        match('}');
        top = savedEnv; //������һ��Env
        return s;
    }

    void decls() throws IOException { //decls ->decls decl | epslon
        while (look.tag == Tag.BASIC) {   // decl -> type ID ;
            Type p = type();
            Token tok = look;
            match(Tag.ID);
            match(';');
            Id id = new Id((Word) tok, p, used); //����ID node
            top.put(tok, id); // ��id������ű�
            used = used + p.width;
        }
    }

    Type type() throws IOException {
        Type p = (Type) look;            // expect look.tag == Tag.BASIC
        match(Tag.BASIC);
        if (look.tag != '[') return p; // T -> basic
        else return dims(p);            // return array type
    }

    Type dims(Type p) throws IOException { //array -> array[num]
        match('[');
        Token tok = look;
        match(Tag.NUM);
        match(']');
        if (look.tag == '[') //����Ƕ�ά���飬�ݹ����
            p = dims(p);
//	      return new Array(((Num)tok).value, p);
        return new Array(((Num) tok).lue, p);
    }

    Stmt stmts() throws IOException { //stmts -> stmts stmt | epslon
        if (look.tag == '}') return Stmt.Null;
        else return new Seq(stmt(), stmts());
    }

    Stmt stmt() throws IOException {
        Expr x;
        Stmt s, s1, s2;
        Stmt savedStmt;         // save enclosing loop for breaks

        switch (look.tag) {

            case ';': // stmt-> ;
                move();
                return Stmt.Null;

            case Tag.IF: // stmt -> if (bool) stmt | if (bool) stmt else stmt
                match(Tag.IF);
                match('(');
                x = bool();
                match(')');
                s1 = stmt();
                if (look.tag != Tag.ELSE) return new If(x, s1);
                match(Tag.ELSE);
                s2 = stmt();
                return new Else(x, s1, s2);

            case Tag.WHILE: //stmt->while(bool) stmt
                While whilenode = new While();
                savedStmt = Stmt.Enclosing;
                Stmt.Enclosing = whilenode;
                match(Tag.WHILE);
                match('(');
                x = bool();
                match(')');
                s1 = stmt();
                whilenode.init(x, s1);
                Stmt.Enclosing = savedStmt;  // reset Stmt.Enclosing
                return whilenode;

            case Tag.DO:
                Do donode = new Do();
                savedStmt = Stmt.Enclosing;
                Stmt.Enclosing = donode;
                match(Tag.DO);
                s1 = stmt();
                match(Tag.WHILE);
                match('(');
                x = bool();
                match(')');
                match(';');
                donode.init(s1, x);
                Stmt.Enclosing = savedStmt;  // reset Stmt.Enclosing
                return donode;

            case Tag.BREAK:
                match(Tag.BREAK);
                match(';');
                return new Break();
            // ���������Tag��for
            case Tag.FOR:
                // ��new һ��For����
                For fornode = new For();
                // ����֮ǰ��Stmt
                savedStmt = Stmt.Enclosing;
                Stmt.Enclosing = fornode;
                // ƥ�䵽For��tag
                match(Tag.FOR);
                // Ȼ��ƥ��'('
                match('(');
                // ƥ��for�ڵ�һ�����ʽ
                s1 = assignFor();
                // Ȼ��ƥ��';'
                match(';');
                // ��ƥ��ڶ������ʽ��Ϊbool���ʽ
                x = bool();
                // Ȼ��ƥ��';'
                match(';');
                // ƥ��for�ڵ��������ʽ
                s2 = assignFor();
                // Ȼ��ƥ��')'
                match(')');
                // ƥ��for�ĺ���������
                s = stmt();
                // ��ʼ��for����
                fornode.init(x, s1, s2, s);
                // ��֮ǰ�����Stmt���¸�ֵ��Stmt.Enclosing
                Stmt.Enclosing = savedStmt;
                // ���ظ�for����
                return fornode;

            case '{':
                return block();

            default:
                return assign();
        }
    }

    // ƥ��for()�ڵı��ʽ
    Stmt assignFor() throws IOException {
        Stmt stmt;
        Token t = look;
        match(Tag.ID);
        Id id = top.get(t);
        if (id == null) error(t.toString() + " undeclared");

        if (look.tag == '=') {       // S -> id = E ;
            move();
            stmt = new Set(id, bool());
        } else {                        // S -> L = E ;
            Access x = offset(id);
            match('=');
            stmt = new SetElem(x, bool());
        }
        return stmt;
    }

    Stmt assign() throws IOException {
        Stmt stmt;
        Token t = look;
        match(Tag.ID);
        Id id = top.get(t);
        if (id == null) error(t.toString() + " undeclared");

        if (look.tag == '=') {       // S -> id = E ;
            move();
            stmt = new Set(id, bool());
        } else {                        // S -> L = E ;
            Access x = offset(id);
            match('=');
            stmt = new SetElem(x, bool());
        }
        match(';');
        return stmt;
    }


    Expr bool() throws IOException {
        Expr x = join();
        while (look.tag == Tag.OR) {
            Token tok = look;
            move();
            x = new Or(tok, x, join());
        }
        return x;
    }

    Expr join() throws IOException {
        Expr x = equality();
        while (look.tag == Tag.AND) {
            Token tok = look;
            move();
            x = new And(tok, x, equality());
        }
        return x;
    }

    Expr equality() throws IOException {
        Expr x = rel();
        while (look.tag == Tag.EQ || look.tag == Tag.NE) {
            Token tok = look;
            move();
            x = new Rel(tok, x, rel());
        }
        return x;
    }

    Expr rel() throws IOException {
        Expr x = expr();
        switch (look.tag) {
            case '<':
            case Tag.LE:
            case Tag.GE:
            case '>':
                Token tok = look;
                move();
                return new Rel(tok, x, expr());
            default:
                return x;
        }
    }

    Expr expr() throws IOException {
        Expr x = term();
        while (look.tag == '+' || look.tag == '-') {
            Token tok = look;
            move();
            x = new Arith(tok, x, term());
        }
        return x;
    }

    Expr term() throws IOException {
        Expr x = unary();
        while (look.tag == '*' || look.tag == '/') {
            Token tok = look;
            move();
            x = new Arith(tok, x, unary());
        }
        return x;
    }

    Expr unary() throws IOException {
        if (look.tag == '-') {
            move();
            return new Unary(Word.minus, unary());
        } else if (look.tag == '!') {
            Token tok = look;
            move();
            return new Not(tok, unary());
        } else return factor();
    }

    Expr factor() throws IOException {
        Expr x = null;
        switch (look.tag) {
            case '(':
                move();
                x = bool();
                match(')');
                return x;
            case Tag.NUM:
                x = new Constant(look, Type.Int);
                move();
                return x;
            case Tag.REAL:
                x = new Constant(look, Type.Float);
                move();
                return x;
            case Tag.TRUE:
                x = Constant.True;
                move();
                return x;
            case Tag.FALSE:
                x = Constant.False;
                move();
                return x;
            default:
                error("syntax error");
                return x;
            case Tag.ID:
                String s = look.toString();
                Id id = top.get(look);
                if (id == null) error(look.toString() + " undeclared");
                move();
                if (look.tag != '[') return id;
                else return offset(id);
        }
    }

    Access offset(Id a) throws IOException {   // I -> [E] | [E] I
        Expr i;
        Expr w;
        Expr t1, t2;
        Expr loc;  // inherit id

        Type type = a.type;
        match('[');
        i = bool();
        match(']');     // first index, I -> [ E ]
        type = ((Array) type).of;
        w = new Constant(type.width);
        t1 = new Arith(new Token('*'), i, w);
        loc = t1;
        while (look.tag == '[') {      // multi-dimensional I -> [ E ] I
            match('[');
            i = bool();
            match(']');
            type = ((Array) type).of;
            w = new Constant(type.width);
            t1 = new Arith(new Token('*'), i, w);
            t2 = new Arith(new Token('+'), loc, t1);
            loc = t2;
        }

        return new Access(a, loc, type);
    }
}
