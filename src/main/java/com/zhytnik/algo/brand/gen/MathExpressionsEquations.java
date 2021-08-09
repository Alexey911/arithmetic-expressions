package com.zhytnik.algo.brand.gen;

import com.google.common.collect.Collections2;
import com.google.common.collect.Sets;
import com.google.common.math.LongMath;
import com.zhytnik.algo.brand.data.Expression;
import com.zhytnik.algo.brand.data.Variable;
import com.zhytnik.algo.brand.threshold.Threshold;
import com.zhytnik.algo.brand.compute.Transformation;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.zhytnik.algo.brand.gen.AllArithmeticPermutations.computeAllPermutations;

@AllArgsConstructor
public class MathExpressionsEquations implements Transformation {

    private final int complexity;
    private final Variable target;
    private final Threshold threshold;

    @Override
    public List<Expression> apply(List<Expression> variables) {
        Set<Expression> source = valueUniqueVariables(variables);

        if (source.size() < complexity) {
            return List.of();
        }

        var theSame = new SameResultExpressions(target.value(), new ArrayList<>(32000), threshold);
        generate(source, theSame);
        return theSame.accepted;
    }

    @Override
    public long complexity(int inputSize) {
        return LongMath.binomial(inputSize, complexity) * LongMath.factorial(complexity) * AllArithmeticPermutations.outputSize(complexity);
    }

    private Set<Expression> valueUniqueVariables(List<Expression> source) {
        return source.stream()
                .filter(var -> var.value() != target.value())
                .collect(Collectors.toSet());
    }

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

    @AllArgsConstructor
    static class SameResultExpressions implements Consumer<Expression> {

        final double expected;
        final List<Expression> accepted;
        final Threshold threshold;

        @Override
        public void accept(Expression probe) {
            if (threshold.isAcceptable(probe.value(), expected)) {
                accepted.add(probe);
            }
        }
    }
}
