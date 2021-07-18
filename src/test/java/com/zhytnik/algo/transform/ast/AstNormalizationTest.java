package com.zhytnik.algo.transform.ast;

import org.junit.jupiter.api.Test;

import java.util.function.UnaryOperator;

import static com.zhytnik.algo.transform.ast.Data.add;
import static com.zhytnik.algo.transform.ast.Data.multiple;
import static com.zhytnik.algo.transform.ast.Data.sub;
import static com.zhytnik.algo.transform.ast.Data.tree;
import static com.zhytnik.algo.transform.ast.Data.var;
import static org.assertj.core.api.Assertions.assertThat;

class AstNormalizationTest {

    UnaryOperator<Tree> normalization = new AstNormalization();

    @Test
    void normalizes() {
        var result = normalization.apply(
                tree(
                        sub(
                                add(
                                        multiple(var("m"), var("l")),
                                        var("d")
                                ),
                                var("k")
                        )
                )
        );

        var expected = tree(
                add(
                        multiple(var("m"), var("l")),
                        sub(var("d"), var("k"))
                )
        );

        assertThat(result.getRoot().toExpression()).isEqualTo(expected.getRoot().toExpression());
    }

    @Test
    void normalizesMultiple() {
        var result = normalization.apply(
                tree(
                        sub(
                                add(
                                        multiple(var("o"), var("l")),
                                        var("k")
                                ),
                                var("z")
                        )
                )
        );

        var expected = tree(
                add(
                        multiple(var("o"), var("l")),
                        sub(var("k"), var("z"))
                )
        );

        assertThat(result.getRoot().toExpression()).isEqualTo(expected.getRoot().toExpression());
    }
}
