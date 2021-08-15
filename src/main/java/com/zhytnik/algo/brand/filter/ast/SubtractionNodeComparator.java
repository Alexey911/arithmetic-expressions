package com.zhytnik.algo.brand.filter.ast;

import java.util.Comparator;

public final class SubtractionNodeComparator implements Comparator<Node> {

    public static final SubtractionNodeComparator INSTANCE = new SubtractionNodeComparator();

    private static final String SPLITERATOR = ",";

    @Override
    public int compare(Node x, Node y) {
        int leftSize = x.left.variables() + y.left.variables();
        int rightSize = x.right.variables() + y.right.variables();

        if (leftSize != rightSize) {
            return Integer.compare(rightSize, leftSize);
        }

        var l = concat(x.left, y.left);
        var r = concat(x.right, y.right);

        if (l.length() != r.length()) {
            return Integer.compare(r.length(), l.length());
        }

        return Double.compare(toUnifiedScore(l), toUnifiedScore(r));
    }

    private StringBuilder concat(Node first, Node second) {
        return first.description(SPLITERATOR).append(SPLITERATOR).append(second.description(SPLITERATOR));
    }

    private double toUnifiedScore(StringBuilder description) {
        double score = 1;

        var variables = description.toString().split(SPLITERATOR);

        for (var variable : variables) {
            int sum = 0;

            for (int i = 0, size = variable.length(); i < size; i++) {
                sum += variable.charAt(i);
            }
            score *= sum;
        }
        return score;
    }
}
