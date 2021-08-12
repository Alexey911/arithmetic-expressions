package com.zhytnik.algo.brand.filter;

import com.zhytnik.algo.brand.compute.Transformation;
import com.zhytnik.algo.brand.data.BinaryOperation;
import com.zhytnik.algo.brand.data.Expression;
import com.zhytnik.algo.brand.data.Variable;
import com.zhytnik.algo.brand.settings.FeatureSettings;
import com.zhytnik.algo.brand.threshold.Threshold;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class ExpressionElimination implements Transformation {

    private static final Variable ONE = new Variable("erased-value", 1.0d);
    private static final Variable ZERO = new Variable("erased-value", 0.0d);

    private final int complexity;
    private final boolean groupElimination;
    private final Threshold similarity;
    private final Threshold overallSimilarity;

    public ExpressionElimination(int complexity, Threshold overallSimilarity) {
        this(complexity, overallSimilarity, FeatureSettings.defaultSettings());
    }

    public ExpressionElimination(int complexity, Threshold overallSimilarity, FeatureSettings settings) {
        this(complexity, overallSimilarity, settings.similarityThreshold(), settings.useGroupElimination());
    }

    public ExpressionElimination(int complexity, Threshold overallSimilarity, Threshold similarity, boolean groupElimination) {
        if (complexity <= 0) {
            throw new IllegalArgumentException("Complexity should be positive! Actual is " + complexity);
        }
        this.complexity = complexity;
        this.groupElimination = groupElimination;
        this.similarity = Objects.requireNonNull(similarity);
        this.overallSimilarity = Objects.requireNonNull(overallSimilarity);
    }

    @Override
    public String shortName() {
        return "Elimination";
    }

    @Override
    public long complexity(int sourceSize) {
        return Math.multiplyFull(complexity, sourceSize);
    }

    @Override
    public List<Expression> apply(List<Expression> source) {
        var invalid = new Counter();
        var valuable = new ArrayList<Expression>();

        for (var e : source) {
            traverse(e, e, invalid);

            if (invalid.isZero()) {
                valuable.add(e);
            } else {
                invalid.reset();
            }
        }
        return valuable;
    }

    private void traverse(Expression expression, Expression source, Counter invalid) {
        if (!(expression instanceof BinaryOperation)) {
            return;
        }

        var op = (BinaryOperation) expression;

        if (op.getLeft().isUnary() || groupElimination) {
            tryToEliminateLeft(op, op.getLeft(), source, invalid);
        }

        if (invalid.isZero() && (op.getRight().isUnary() || groupElimination)) {
            tryToEliminateRight(op, op.getRight(), source, invalid);
        }

        if (invalid.isZero()) {
            traverse(op.getLeft(), source, invalid);
        }
        if (invalid.isZero()) {
            traverse(op.getRight(), source, invalid);
        }
    }

    private void tryToEliminateLeft(BinaryOperation operation, Expression target, Expression source, Counter invalid) {
        switch (operation.getOperator()) {
            case MULTIPLICATION:
                tryToEliminate(target, ONE, invalid, source);
            case ADDITION:
            case SUBTRACTION:
            case DIVISION:
                tryToEliminate(target, ZERO, invalid, source);
        }
    }

    private void tryToEliminateRight(BinaryOperation operation, Expression target, Expression source, Counter invalid) {
        switch (operation.getOperator()) {
            case DIVISION:
                tryToEliminate(target, ONE, invalid, source);
                break;
            case MULTIPLICATION:
                tryToEliminate(target, ONE, invalid, source);
            case ADDITION:
            case SUBTRACTION:
                tryToEliminate(target, ZERO, invalid, source);
        }
    }

    private void tryToEliminate(Expression elimination, Expression replacement, Counter invalid, Expression source) {
        if (elimination.isUnary() && similarity.isAcceptable(elimination.value(), replacement.value())) {
            return;
        }

        var modified = source.recalculateWith(Map.of(elimination, replacement));

        if (overallSimilarity.isAcceptable(modified.value(), source.value())) {
            invalid.increment();
        }
    }
}
