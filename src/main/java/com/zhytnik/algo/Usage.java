package com.zhytnik.algo;

import com.zhytnik.algo.data.DefaultVariableSearch;
import com.zhytnik.algo.math.Variable;

import java.util.HashSet;
import java.util.Set;

//TODO: adaptive error
//TODO: rename to complexity
//TODO: recursive elimination
public class Usage {

    public static void main(String[] args) {
        var allVariables = new DefaultVariableSearch().search(Usage.class.getResourceAsStream("/sub.json"));
        var desiredVariables = desiredVariables(allVariables, Set.of("BasicEPS"));
        var calculator = new Calculator(allVariables, 4, 0.000001, 0.000000001);

        for (var desired : desiredVariables) {
            var expressions = calculator.calculate(desired);
            System.out.println("Total: " + expressions.size());

            for (var expression : expressions) {
                System.out.println(desired + "=" + expression + " = " + expression.value());
            }
        }
    }

    private static Set<Variable> desiredVariables(Set<Variable> all, Set<String> desiredNames) {
        Set<Variable> result = new HashSet<>();

        for (var desired : desiredNames) {
            for (var variable : all) {
                if (variable.writtenFormat().endsWith(desired)) {
                    result.add(variable);
                }
            }
        }
        return result;
    }
}
