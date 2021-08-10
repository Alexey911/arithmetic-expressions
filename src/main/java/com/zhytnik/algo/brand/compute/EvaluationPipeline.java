package com.zhytnik.algo.brand.compute;

import com.zhytnik.algo.brand.data.Expression;
import com.zhytnik.algo.brand.data.Variable;
import com.zhytnik.algo.brand.filter.AstUniqueExpressions;
import com.zhytnik.algo.brand.filter.CommutativeDuplicateRemoval;
import com.zhytnik.algo.brand.filter.MathErrorVerification;
import com.zhytnik.algo.brand.filter.ExpressionElimination;
import com.zhytnik.algo.brand.gen.MathExpressionsEquations;
import com.zhytnik.algo.brand.threshold.Threshold;

import java.util.Arrays;
import java.util.List;

public class EvaluationPipeline {

    private final List<Transformation> steps;

    public EvaluationPipeline(Threshold acceptance, int complexity, Variable target) {
        steps = Arrays.asList(
                new MathExpressionsEquations(target, complexity, acceptance),
                new CommutativeDuplicateRemoval(complexity),
                new ExpressionElimination(complexity, acceptance),
                new AstUniqueExpressions(),
                new MathErrorVerification(target, acceptance)
        );
    }

    public long totalExpressionsCount(int inputSize) {
        long count = 0;

        for (var step : steps) {
            count += step.complexity(inputSize);
        }
        return count;
    }

    public List<Expression> evaluate(List<Expression> input) {
        List<Expression> result = input;

        for (var step : steps) {
            result = step.apply(result);
        }
        return result;
    }
}
