package org.exp.regexes;


import org.exp.FA.DFA;
import org.exp.FA.TNFA;

import java.util.LinkedList;

/**
 * Class Regexes is used to represent a set of regexes
 */
public class Regexes {
    LinkedList<Pattern> patterns = new LinkedList<>(); //a set of patterns; can use other data structure

    TNFA tnfa; //corresponding to this.patterns

    DFA dfa; // corresponding to this.patterns

    public Regexes(String[] regexes) {
        for (String r : regexes) {
            Pattern p = new Pattern();
            p.setId("pattern_" + r);
            p.setRegex(r);
            this.patterns.add(p);
        }
    }

    public LinkedList<Pattern> getPatterns() {
        return patterns;
    }

    public void toTNFA() {
        System.out.println("4) Now convert the regexes to a NFA by merging the start states into one start state and the accepting states into one accepting state!");


    }

    public void toDFA() {
        System.out.println("5) Now convert the NFA to a DFA by Subset Construction!");
    }
}
