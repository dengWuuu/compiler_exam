package org.exp;

import org.exp.regexes.Pattern;
import org.exp.regexes.Regexes;

/**
 * An example to show how to convert a set of regexes into a DFA;
 * @author xuyang
 */
public class Test {

    //symbols - the alphabet for regular expression
    public static final char[] symbols = {'a','b','c','d','e','f','g','h','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};

    public static void main(String[] args) {
        //Here, using an Array of regexes instead of reading a regex file
        String[] regexes = new String[]{"c(a|b)*","a|b", "ab*", "d(f|e)","d(f|ea*(g|h))b","c(a|b)*"};//
        System.out.println("1) Decompose the input regex into  subRegexes. ");

        Regexes aRegexes = new Regexes(regexes);
        for(Pattern p : aRegexes.getPatterns()){ //convert each regex respectively
            p.compile();
        }

        aRegexes.toTNFA(); //convert all NFA to one NFA
        aRegexes.toDFA();  //convert NFA to DFA

        System.out.println("Bye, world!");
    }
}