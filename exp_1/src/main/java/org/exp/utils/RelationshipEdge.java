package org.exp.utils;

import org.jgrapht.graph.*;

/**
 * Custom edge class labeled with relationship type.
 *
 * @Author WU
 */

public class RelationshipEdge extends DefaultEdge {
    private final char label; //String label;

    /**
     * Constructs a relationship edge
     *
     * @param label the label of the new edge.
     */
    public RelationshipEdge(char label) {
        super();
        this.label = label;
    }

    /**
     * Gets the label associated with this edge.
     *
     * @return edge label
     */
    public char getLabel()//String getLabel()
    {
        return label;
    }

    @Override
    public String toString() {
        return "(" + getSource() + " : " + getTarget() + " : " + label + ")";
    }
}
