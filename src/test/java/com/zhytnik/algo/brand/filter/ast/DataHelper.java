package com.zhytnik.algo.brand.filter.ast;

import com.zhytnik.algo.brand.data.BinaryOperation;
import com.zhytnik.algo.brand.data.BinaryOperator;
import com.zhytnik.algo.brand.data.Expression;
import com.zhytnik.algo.brand.data.Variable;

public class DataHelper {

    private static int index = 0;

    public static Variable var(double value) {
        return var(("v" + index++), value);
    }

    public static Variable var(String name) {
        return var(name, 0d);
    }

    public static Variable var(String name, double value) {
        return new Variable(name, value);
    }

    public static Tree tree(Expression expression) {
        return new Tree(expression);
    }

    public static BinaryOperation add(Object left, Object right) {
        return new BinaryOperation(exp(left), exp(right), BinaryOperator.ADDITION);
    }

    public static BinaryOperation sub(Object left, Object right) {
        return new BinaryOperation(exp(left), exp(right), BinaryOperator.SUBTRACTION);
    }

    public static BinaryOperation div(Object left, Object right) {
        return new BinaryOperation(exp(left), exp(right), BinaryOperator.DIVISION);
    }

    public static BinaryOperation multiple(Object left, Object right) {
        return new BinaryOperation(exp(left), exp(right), BinaryOperator.MULTIPLICATION);
    }

    private static Expression exp(Object v) {
        if (v instanceof Expression) {
            return (Expression) v;
        }
        if (v instanceof Number) {
            return var(((Number) v).doubleValue());
        }
        return var(v.toString());
    }
}
