package inter;

public class Stmt extends Node {

    public Stmt() {
    } //����ʵ�־���stmt�ڵ�

    public static Stmt Null = new Stmt(); //���������

    /*
     * gen�߼�������ʵ��
     * ���� b��begin��: ��ǰstmt�Ŀ�ʼlabel��
     * ����a ��after��: ��ǰ����ĵ�һ��ָ�����
     */
    public void gen(int b, int a) {
    }

    // ����label after������ʵ�ַ�սṹ��break,continue����ת
    int after = 0;
    // used for break, continue stmts
    public static Stmt Enclosing = Stmt.Null;

    public void display() {
    }
}
