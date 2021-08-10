package com.zhytnik.algo.brand.filter.ast;

import com.zhytnik.algo.brand.data.Expression;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;

import static com.zhytnik.algo.brand.filter.ast.DataHelper.add;
import static com.zhytnik.algo.brand.filter.ast.DataHelper.div;
import static com.zhytnik.algo.brand.filter.ast.DataHelper.multiple;
import static com.zhytnik.algo.brand.filter.ast.DataHelper.sub;
import static org.assertj.core.api.Assertions.assertThat;

class BinaryGroupingTest {

    @ParameterizedTest
    @MethodSource("arguments")
    void groups(Expression source, String expected) {
        var actual = apply(source);

        assertThat(actual).isEqualTo(expected);
    }

    String apply(Expression source) {
        return new BinaryGrouping().apply(DataHelper.tree(source)).toString();
    }

    static List<Arguments> arguments() {
        return List.of(
                /* LEFT - LEFT */

                Arguments.of(add(add("a", add("b", "c")), "d"), "(a+d)+(b+c)"),
                Arguments.of(sub(sub("a", sub("b", "c")), "d"), "(a-d)-(b-c)"),
                Arguments.of(add(sub("a", add("b", "c")), "d"), "(a+d)-(b+c)"),
                Arguments.of(sub(add("a", add("b", "c")), "d"), "(a-d)+(b+c)"),
                Arguments.of(div(div("a", add("b", "c")), "d"), "(a/d)/(b+c)"),
                Arguments.of(div(multiple("a", add("b", "c")), "d"), "(a/d)*(b+c)"),
                Arguments.of(multiple(div("a", add("b", "c")), "d"), "(a*d)/(b+c)"),
                Arguments.of(multiple(multiple("a", multiple("b", "c")), "d"), "(a*d)*(b*c)"),

                /* LEFT - RIGHT */

                Arguments.of(add(sub(add("a", "b"), "c"), "d"), "(a+b)+(d-c)"),
                Arguments.of(sub(add(add("a", "b"), "c"), "d"), "(a+b)+(c-d)"),
                Arguments.of(div(multiple(add("a", "b"), "c"), "d"), "(a+b)*(c/d)"),
                Arguments.of(multiple(div(add("a", "b"), "c"), "d"), "(a+b)*(d/c)"),
                Arguments.of(add(add(add(add("a", "b"), "c"), "d"), "e"), "((a+b)+(c+d))+e"),
                Arguments.of(sub(sub(sub(sub("a", "b"), "c"), "d"), "e"), "((a-b)-(c+d))-e"),
                Arguments.of(div(div(div(div("a", "b"), "c"), "d"), "e"), "((a/b)/(c*d))/e"),
                Arguments.of(multiple(multiple(multiple(multiple("a", "b"), "c"), "d"), "e"), "((a*b)*(c*d))*e"),

                /* RIGHT - LEFT */

                Arguments.of(add("a", sub("b", add("c", "d"))), "(a+b)-(c+d)"),
                Arguments.of(sub("a", add("b", add("c", "d"))), "(a-b)-(c+d)"),
                Arguments.of(multiple("a", div("b", add("c", "d"))), "(a*b)/(c+d)"),
                Arguments.of(div("a", multiple("b", add("c", "d"))), "(a/b)/(c+d)"),
                Arguments.of(div("a", div("b", div("c", div("d", "e")))), "a/((b/c)*(d/e))"),
                Arguments.of(add("a", add("b", add("c", add("d", "e")))), "a+((b+c)+(d+e))"),
                Arguments.of(sub("a", sub("b", sub("c", sub("d", "e")))), "a-((b-c)+(d-e))"),
                Arguments.of(multiple("a", multiple("b", multiple("c", multiple("d", "e")))), "a*((b*c)*(d*e))"),

                /* RIGHT - RIGHT */

                Arguments.of(add("a", add(add("b", "c"), "d")), "(a+d)+(b+c)"),
                Arguments.of(add("a", sub(add("b", "c"), "d")), "(a-d)+(b+c)"),
                Arguments.of(sub("a", add(add("b", "c"), "d")), "(a-d)-(b+c)"),
                Arguments.of(sub("a", sub(add("b", "c"), "d")), "(a+d)-(b+c)"),
                Arguments.of(div("a", div(add("b", "c"), "d")), "(a*d)/(b+c)"),
                Arguments.of(multiple("a", div(add("b", "c"), "d")), "(a/d)*(b+c)"),
                Arguments.of(div("a", multiple(add("b", "c"), "d")), "(a/d)/(b+c)"),
                Arguments.of(multiple("a", multiple(add("b", "c"), "d")), "(a*d)*(b+c)")
        );
    }
}
