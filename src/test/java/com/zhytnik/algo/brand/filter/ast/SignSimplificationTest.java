package com.zhytnik.algo.brand.filter.ast;

import com.zhytnik.algo.brand.data.Expression;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;

import static com.zhytnik.algo.brand.filter.ast.DataHelper.div;
import static com.zhytnik.algo.brand.filter.ast.DataHelper.multiple;
import static com.zhytnik.algo.brand.filter.ast.DataHelper.sub;
import static com.zhytnik.algo.brand.filter.ast.DataHelper.tree;
import static org.assertj.core.api.Assertions.assertThat;

class SignSimplificationTest {

    @ParameterizedTest
    @MethodSource("arguments")
    void simplifies(Expression source, String expected) {
        var actual = apply(source);

        assertThat(actual).isEqualTo(expected);
    }

    String apply(Expression source) {
        return new SignSimplification().apply(tree(source)).toString();
    }

    static List<Arguments> arguments() {
        return List.of(
                Arguments.of(div(sub("a", "d"), sub("c", "b")), "(a-d)/(c-b)"),
                Arguments.of(div(sub("d", "a"), sub("b", "c")), "(a-d)/(c-b)"),
                Arguments.of(div(sub("c", "b"), sub("a", "d")), "(c-b)/(a-d)"),
                Arguments.of(div(sub("b", "c"), sub("d", "a")), "(c-b)/(a-d)"),
                Arguments.of(sub("k", multiple(sub("d", "a"), sub("b", "c"))), "k-((a-d)*(c-b))"),
                Arguments.of(div(multiple(sub("a", "b"), "k"), sub("n", "c")), "((b-a)*k)/(c-n)"),
                Arguments.of(div(multiple(sub("b", "a"), "k"), sub("c", "n")), "((b-a)*k)/(c-n)"),
                Arguments.of(div(multiple(sub("a", "b"), sub("d", "c")), sub("k", "e")), "((a-b)*(c-d))/(e-k)")
        );
    }
}