package com.zhytnik.algo.brand.filter.ast;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;

import static com.zhytnik.algo.brand.data.BinaryOperator.ADDITION;
import static com.zhytnik.algo.brand.filter.ast.Data.var;
import static com.zhytnik.algo.brand.filter.ast.Node.operation;
import static org.assertj.core.api.Assertions.assertThat;

class DefaultNodeComparatorTest {

    @ParameterizedTest
    @MethodSource("arguments")
    void compares(Node x, Node y, int expected) {
        assertThat(DefaultNodeComparator.INSTANCE.compare(x, y)).isEqualTo(expected);
    }

    static List<Arguments> arguments() {
        return List.of(
                Arguments.of(variable("x"), variable("y"), -1),
                Arguments.of(variable("x"), variable("x"), 0),
                Arguments.of(variable("y"), variable("x"), 1),

                Arguments.of(variable("one"), variable("x"), -1),
                Arguments.of(variable("x"), variable("one"), 1),

                Arguments.of(variable("z"), operation(variable("x"), variable("y"), ADDITION), 1),
                Arguments.of(operation(variable("x"), variable("y"), ADDITION), variable("z"), -1)
        );
    }

    static Node variable(String name) {
        return Node.variable(var(name));
    }
}
