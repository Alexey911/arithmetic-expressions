package com.zhytnik.algo.brand.compute;

import com.zhytnik.algo.brand.data.Expression;
import com.zhytnik.algo.brand.data.Variable;
import com.zhytnik.algo.brand.filter.AstUniqueExpressions;
import com.zhytnik.algo.brand.filter.UniqueExpressions;
import com.zhytnik.algo.brand.filter.ValueFiltration;
import com.zhytnik.algo.brand.filter.VariableElimination;
import com.zhytnik.algo.brand.gen.MathExpressionsEquations;
import com.zhytnik.algo.brand.threshold.Threshold;

import java.util.Arrays;
import java.util.List;

public class EvaluationPipeline {

    private final List<Transformation> steps;

    public EvaluationPipeline(Threshold acceptance, int complexity, Variable target) {
        steps = Arrays.asList(
                new MathExpressionsEquations(complexity, target, acceptance),
                new UniqueExpressions(),
                new VariableElimination(complexity, acceptance),
                new AstUniqueExpressions(),
                new ValueFiltration(target.value(), acceptance)
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
