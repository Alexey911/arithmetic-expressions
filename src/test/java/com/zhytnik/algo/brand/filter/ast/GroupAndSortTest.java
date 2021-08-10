package com.zhytnik.algo.brand.filter.ast;

import com.zhytnik.algo.brand.data.Expression;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;

import static com.zhytnik.algo.brand.filter.ast.DataHelper.add;
import static com.zhytnik.algo.brand.filter.ast.DataHelper.multiple;
import static org.assertj.core.api.Assertions.assertThat;

class GroupAndSortTest {

    @ParameterizedTest
    @MethodSource("arguments")
    void sorts(Expression source, String expected) {
        var actual = apply(source);

        assertThat(actual).isEqualTo(expected);
    }

    String apply(Expression source) {
        return new GroupAndSort().apply(DataHelper.tree(source)).toString();
    }

    static List<Arguments> arguments() {
        return List.of(
                Arguments.of(add("y", "x"), "x+y"),
                Arguments.of(multiple("a", add("y", "x")), "(x+y)*a"),
                Arguments.of(multiple("a", add(add("d", "c"), "b")), "((b+c)+d)*a"),
                Arguments.of(multiple("a", add(multiple("d", "c"), "b")), "((c*d)+b)*a"),
                Arguments.of(multiple("a", multiple(multiple("d", "c"), "b")), "(a*b)*(c*d)")

        );
    }
}
