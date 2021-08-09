package com.zhytnik.algo.brand.filter.ast;

import java.util.Comparator;

public final class NodeComparator implements Comparator<Node> {

    public static final NodeComparator INSTANCE = new NodeComparator();

    @Override
    public int compare(Node x, Node y) {
        int left = x.variables();
        int right = y.variables();

        if (left != right) {
            return Integer.compare(right, left);
        }

        var l = x.description();
        var r = y.description();

        if (l.length() != r.length()) {
            return Integer.compare(r.length(), l.length());
        }
        return l.compareTo(r);
    }
}
