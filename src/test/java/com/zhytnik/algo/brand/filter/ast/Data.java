package com.zhytnik.algo.brand.filter.ast;

import com.zhytnik.algo.brand.data.BinaryOperation;
import com.zhytnik.algo.brand.data.BinaryOperator;
import com.zhytnik.algo.brand.data.Expression;
import com.zhytnik.algo.brand.data.Variable;

public class Data {

    public static Variable exp(String name) {
        return new Variable(name, 0d);
    }

    public static Tree tree(Expression expression) {
        return Tree.toTree(expression);
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
        return v instanceof Expression ? (Expression) v : exp(v.toString());
    }
}
