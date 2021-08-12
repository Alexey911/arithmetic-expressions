package com.zhytnik.algo.brand.compute;

import com.zhytnik.algo.brand.data.Expression;
import com.zhytnik.algo.brand.filter.AstUniqueExpressions;
import com.zhytnik.algo.brand.filter.CommutativeDuplicateRemoval;
import com.zhytnik.algo.brand.filter.ExpressionElimination;
import com.zhytnik.algo.brand.filter.MathErrorVerification;
import com.zhytnik.algo.brand.gen.MathExpressionsEquations;
import com.zhytnik.algo.brand.settings.FeatureSettings;
import com.zhytnik.algo.brand.threshold.Threshold;

import java.util.Arrays;
import java.util.List;

public class EvaluationPipeline {

    private final List<Transformation> steps;

    public EvaluationPipeline(Threshold acceptance, int complexity, Expression target) {
        this(acceptance, complexity, target, FeatureSettings.defaultSettings());
    }

    public EvaluationPipeline(Threshold acceptance, int complexity, Expression target, FeatureSettings settings) {
        steps = Arrays.asList(
                new MathExpressionsEquations(target, complexity, acceptance, settings),
                asLogging(new CommutativeDuplicateRemoval(complexity)),
                asLogging(new ExpressionElimination(complexity, acceptance, settings)),
                asLogging(new AstUniqueExpressions(settings)),
                new MathErrorVerification(target, acceptance)
        );
    }

    public long totalExpressionsCount(int sourceSize) {
        long count = 0;

        for (var step : steps) {
            count += step.complexity(sourceSize);
        }
        return count;
    }

    public List<Expression> evaluate(List<Expression> source) {
        List<Expression> result = source;

        for (var step : steps) {
            result = step.apply(result);
        }
        return result;
    }

    private Transformation asLogging(Transformation source) {
        return new LoggingTransformation(source);
    }
}
