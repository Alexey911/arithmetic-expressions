package com.zhytnik.algo.brand.filter.ast;

import com.zhytnik.algo.brand.data.Expression;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;

import static com.zhytnik.algo.brand.filter.ast.Data.add;
import static com.zhytnik.algo.brand.filter.ast.Data.div;
import static com.zhytnik.algo.brand.filter.ast.Data.sub;
import static com.zhytnik.algo.brand.filter.ast.Data.var;
import static org.assertj.core.api.Assertions.assertThat;

class AstNormalizationTest {

    @ParameterizedTest
    @MethodSource("arguments")
    void normalizes(Expression source, String expected) {
        var actual = apply(source);

        assertThat(actual).isEqualTo(expected);
    }

    String apply(Expression source) {
        return new AstNormalization().apply(Data.tree(source)).toString();
    }

    static List<Arguments> arguments() {
        return List.of(
                Arguments.of(add("d", add("c", add("a", add("b", "e")))), "((a+b)+(c+d))+e"),
                Arguments.of(add(add(add(add("e", "b"), "a"), "c"), "d"), "((a+b)+(c+d))+e"),
                Arguments.of(add("a", add(add("d", "b"), add("c", "e"))), "((a+b)+(c+d))+e"),
                Arguments.of(div(sub("a", sub("b", "c")), var("d")), "((a+c)-b)/d"),
                Arguments.of(div(add("c", sub("a", "b")), var("d")), "((a+c)-b)/d")
        );
    }
}
