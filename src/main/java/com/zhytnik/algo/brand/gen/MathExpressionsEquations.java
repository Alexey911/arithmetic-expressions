package com.zhytnik.algo.brand.gen;

import com.google.common.collect.Collections2;
import com.google.common.collect.Sets;
import com.google.common.math.LongMath;
import com.zhytnik.algo.brand.compute.Transformation;
import com.zhytnik.algo.brand.data.Expression;
import com.zhytnik.algo.brand.settings.FeatureSettings;
import com.zhytnik.algo.brand.threshold.Threshold;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.zhytnik.algo.brand.gen.AllArithmeticPermutations.computeAllPermutations;

public class MathExpressionsEquations implements Transformation {

    private final int complexity;
    private final Expression target;
    private final Threshold threshold;
    private final boolean useSameValues;

    public MathExpressionsEquations(Expression target, int complexity, Threshold threshold) {
        this(target, complexity, threshold, FeatureSettings.defaultSettings());
    }

    public MathExpressionsEquations(Expression target, int complexity, Threshold threshold, FeatureSettings settings) {
        this(target, complexity, threshold, settings.useSameValues());
    }

    public MathExpressionsEquations(Expression target, int complexity, Threshold threshold, boolean useSameValues) {
        if (complexity <= 0) {
            throw new IllegalArgumentException("Complexity should be positive! Actual is " + complexity);
        }
        this.complexity = complexity;
        this.useSameValues = useSameValues;
        this.target = Objects.requireNonNull(target);
        this.threshold = Objects.requireNonNull(threshold);
    }

    @Override
    public long complexity(int sourceSize) {
        return LongMath.binomial(sourceSize, complexity) *
                LongMath.factorial(complexity) *
                AllArithmeticPermutations.permutationsCount(complexity);
    }

    @Override
    public List<Expression> apply(List<Expression> source) {
        var unique = useSameValues ? new HashSet<>(source) : uniqueValues(source);

        if (unique.size() < complexity) {
            return List.of();
        }

        var valid = new ValidExpressions(target, threshold);
        generate(unique, valid);
        return valid.verified;
    }

    private Set<Expression> uniqueValues(List<Expression> source) {
        return source.stream()
                .filter(e -> e.value() != target.value())
                .collect(Collectors.toSet());
    }

    //TODO: add parallel execution
    @SuppressWarnings("UnstableApiUsage")
    private void generate(Set<Expression> source, Consumer<Expression> expressions) {
        for (var combination : Sets.combinations(source, complexity)) {
            for (var permutation : Collections2.permutations(combination)) {
                for (var expression : computeAllPermutations(permutation)) {
                    expressions.accept(expression);
                }
            }
        }
    }

    private static final class ValidExpressions implements Consumer<Expression> {

        private static final int DEFAULT_INITIAL_CAPACITY = 16_000;

        final double expected;
        final Threshold threshold;
        final List<Expression> verified;

        ValidExpressions(Expression target, Threshold threshold) {
            this.threshold = threshold;
            this.expected = target.value();
            this.verified = new ArrayList<>(DEFAULT_INITIAL_CAPACITY);
        }

        @Override
        public void accept(Expression probe) {
            if (threshold.isAcceptable(probe.value(), expected)) {
                verified.add(probe);
            }
        }
    }
}
