package com.zhytnik.algo;

import com.zhytnik.algo.error.AbsoluteErrorThreshold;
import com.zhytnik.algo.error.BiDoublePredicate;
import com.zhytnik.algo.error.RelativeErrorThreshold;
import com.zhytnik.algo.math.Expression;
import com.zhytnik.algo.math.Variable;
import com.zhytnik.algo.transform.AstFiltration;
import com.zhytnik.algo.transform.Combinations;
import com.zhytnik.algo.transform.GroupTransformation;
import com.zhytnik.algo.transform.MathExpressions;
import com.zhytnik.algo.transform.Permutations;
import com.zhytnik.algo.transform.Transformation;
import com.zhytnik.algo.transform.UniqueExpressions;
import com.zhytnik.algo.transform.VariableElimination;
import com.zhytnik.algo.transform.ast.AstNormalization;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Calculator {

    private static final int OPS_PER_SEC = 33_000_000;

    private final int complexity;
    private final Set<Variable> variables;
    private final BiDoublePredicate equal;

    private final Transformation mathExpressions;
    private final Transformation uniqueExpressions;
    private final Transformation normalizedFiltration;
    private final VariableElimination variableElimination;
    private final GroupTransformation<Set<Expression>, Set<Expression>> combinations;
    private final GroupTransformation<Collection<Expression>, List<Expression>> permutations;

    public Calculator(Set<Variable> variables, int complexity, double relativeError, double absoluteAccuracy) {
        if (variables.size() <= 1) {
            throw new IllegalArgumentException("Value source should have at least two elements! Actual size is " + variables.size());
        }
        this.complexity = complexity;
        this.variables = new HashSet<>(variables);
        this.permutations = new Permutations();
        this.mathExpressions = new MathExpressions();
        this.combinations = new Combinations(complexity);
        this.uniqueExpressions = new UniqueExpressions();
        this.normalizedFiltration = new AstFiltration(new AstNormalization());
        this.equal = new AbsoluteErrorThreshold(absoluteAccuracy).and(new RelativeErrorThreshold(relativeError));
        this.variableElimination = new VariableElimination(equal);
    }

    public List<Expression> calculate(Variable var) {
        long now = System.currentTimeMillis();

        var result = expressions(var);

        long duration = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - now);

        System.out.println("Time: " + duration + "s");
        return result;
    }

    private List<Expression> expressions(Variable var) {
        List<Expression> valid = new ArrayList<>();
        Set<Expression> variableSubSet = variables.stream().filter(variable -> variable.value() != var.value()).collect(Collectors.toSet());

        System.out.println("ETC: " + totalExpressions(variableSubSet.size(), complexity) / OPS_PER_SEC + "s");

        long total = 0;
        //TODO: parallel execution
        for (var combination : combinations.apply(variableSubSet)) {
            for (var permutation : permutations.apply(combination)) {
                for (var expression : mathExpressions.apply(permutation)) {
//                    total++;
                    validate(valid, expression, var);
                }
            }
        }

        System.out.println(total);

        long now = System.currentTimeMillis();
        var unique = uniqueExpressions.apply(valid);
        System.out.println(TimeUnit.MILLISECONDS.toMicros(System.currentTimeMillis() - now));
        valid.clear();

        var significant = variableElimination.apply(unique);
        var normalized = normalizedFiltration.apply(significant);

        for (var expression : normalized) {
            validate(valid, expression, var);
        }
        return valid;
    }

    private void validate(List<Expression> valid, Expression source, Variable target) {
        if (equal.test(source.value(), target.value())) {
            valid.add(source);
        }
    }

    private long totalExpressions(int variables, int complexity) {
        return combinations.expectedCount(variables) * permutations.expectedCount(complexity) * mathExpressions.expectedCount(complexity);
    }
}