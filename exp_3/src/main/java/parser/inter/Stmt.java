package parser.inter;

public class Stmt extends Node {

    public Stmt() {
    } //子类实现具体stmt节点

    public static Stmt Null = new Stmt(); //空语句序列

    /*
     * gen逻辑在子类实现
     * 参数 b（begin）: 当前stmt的开始label；
     * 参数a （after）: 当前语句后的第一条指令语句
     */
    public void gen(int b, int a) {
    }

    // 保存label after，用于实现封闭结构中break,continue的跳转
    int after = 0;
    // used for break, continue stmts
    public static Stmt Enclosing = Stmt.Null;

    public void display() {
    }
}
