package scanner.main;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import scanner.lexer.*;

public class Main {

    public static void main(String[] args) throws Exception {
        // 读取文件
        FileInputStream fis = new FileInputStream("exp_3/src/main/java/scanner/input.txt");
        InputStreamReader isr = new InputStreamReader(fis);
        // Reader读取文件流
        BufferedReader reader = new BufferedReader(isr);
        // fil 存储整个文件的内容
        StringBuilder fil = new StringBuilder();
        String line;
        // 逐行读取文件的内容，再手动添加换行符
        while ((line = reader.readLine()) != null) {
            fil.append(line);
            fil.append('\n');
        }
        // 删除最后一个换行符
        fil.delete(fil.length() - 1, fil.length());
        // 添加一个终止符号，需要该符号就停止
        fil.append('\0');
        // 将整个文件读取出来的String 赋值给Lexer.file变量
        Lexer.file = fil.toString();
        // 关闭连接
        reader.close();
        isr.close();
        fis.close();
        // new 一个Lexer()
        Lexer lexer = new Lexer();
        char c;
        do {
            Token token = lexer.scan();
            switch (token.tag) {
                // 270和272表示数字
                case 270:
                case 272:
                    System.out.println("(NUM , " + token.toString() + ")");
                    break;
                // 264表示普通id
                case 264:
                    System.out.println("(ID , " + token.toString() + ")");
                    break;
                // 256-260,265,274-276表示关键词
                case 256:
                case 257:
                case 258:
                case 259:
                case 260:
                case 265:
                case 274:
                case 275:
                case 276:
                    System.out.println("(KEY , " + token.toString() + ")");
                    break;
                case 13:
                    break;
                case 0:
                    break;
                // 默认表示运算符
                default:
                    System.out.println("(SYM , " + token.toString() + ")");
                    break;
            }
            // 当tag为终止符时，跳出循环
            if (token.tag == '\0') {
                break;
            }
            // 最近读到的字符为终止符时，跳出循环
        } while (lexer.getPeek() != '\0');
    }
}
