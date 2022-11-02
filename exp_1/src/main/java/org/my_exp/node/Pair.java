package org.my_exp.node;

import lombok.Data;

/**
 * 记录一个状态机的起始节点和终止节点
 */
@Data
public class Pair {
    public Node startNode;
    public Node endNode;
}
