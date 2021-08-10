package com.zhytnik.algo.brand.filter.ast;

import com.zhytnik.algo.brand.data.Expression;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;

import static com.zhytnik.algo.brand.filter.ast.Data.div;
import static com.zhytnik.algo.brand.filter.ast.Data.multiple;
import static org.assertj.core.api.Assertions.assertThat;

class DivisionSimplificationTest {

    @ParameterizedTest
    @MethodSource("arguments")
    void replaces(Expression source, String expected) {
        var actual = apply(source);

        assertThat(actual).isEqualTo(expected);
    }

    String apply(Expression source) {
        return new DivisionSimplification().apply(Data.tree(source)).toString();
    }

    static List<Arguments> arguments() {
        return List.of(
                Arguments.of(div("a", "b"), "a/b"),
                Arguments.of(div(div("a", "b"), "c"), "a/(b*c)"),
                Arguments.of(div("a", div("b", "c")), "(a*c)/b"),
                Arguments.of(multiple("a", div("b", "c")), "(a*b)/c"),
                Arguments.of(multiple(div("a", "b"), "c"), "(a*c)/b"),
                Arguments.of(multiple(div("a", "b"), div("c", "d")), "(a*c)/(b*d)"),
                Arguments.of(div("a", div("b", div("c", "d"))), "(a*c)/(b*d)"),
                Arguments.of(div(div(div("a", "b"), "c"), "d)"), "a/((b*c)*d))")
        );
    }
}
