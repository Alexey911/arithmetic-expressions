package com.zhytnik.algo.brand.filter.ast;

import com.zhytnik.algo.brand.data.Expression;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;

import static com.zhytnik.algo.brand.filter.ast.Data.add;
import static com.zhytnik.algo.brand.filter.ast.Data.sub;
import static org.assertj.core.api.Assertions.assertThat;

class SubtractionSeparationTest {

    @ParameterizedTest
    @MethodSource("arguments")
    void separates(Expression source, String expected) {
        var actual = apply(source);

        assertThat(actual).isEqualTo(expected);
    }

    String apply(Expression source) {
        return new SubtractionSeparation().apply(Data.tree(source)).toString();
    }

    static List<Arguments> arguments() {
        return List.of(
                Arguments.of(sub("a", sub("b", "c")), "(a+c)-b"),
                Arguments.of(sub(sub("a", "b"), sub("c", "d")), "(a+d)-(b+c)"),
                Arguments.of(sub("a", sub("b", sub("c", sub("d", "e")))), "((c+e)+a)-(b+d)"),
                Arguments.of(sub("a", add(sub("b", "c"), sub("d", "e"))), "((c+e)+a)-(b+d)"),
                Arguments.of(sub("a", add(sub("b", "c"), add("d", "e"))), "(a+c)-((d+e)+b)"),
                Arguments.of(sub("a", add(add("b", "c"), sub("d", "e"))), "(a+e)-((b+c)+d)"),
                Arguments.of(add(add(sub("a", "b"), add("c", "d")), add("e", "k")), "(((c+d)+a)+(e+k))-b"),
                Arguments.of(sub(add(sub("a", "b"), add("c", "d")), add("e", "k")), "((c+d)+a)-((e+k)+b)")
        );
    }
}
