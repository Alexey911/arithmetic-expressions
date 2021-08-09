package com.zhytnik.algo.brand.filter.ast;

import com.zhytnik.algo.brand.data.BinaryOperator;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

import static com.zhytnik.algo.brand.data.BinaryOperator.SUBTRACTION;

final class CommutativeSorting implements UnaryOperator<Tree> {

    @Override
    public Tree apply(Tree tree) {
        traverse(tree.root, SUBTRACTION);
        return tree;
    }

    private List<Node> traverse(Node node, BinaryOperator root) {
        if (node.isVariable()) {
            return root.isCommutative() ? toCommutativeOperandList(node) : new ArrayList<>();
        }

        var op = node.operator;

        var lefts = traverse(node.left, op);
        var rights = traverse(node.right, op);

        if (op.isCommutative()) {
            var operands = toCommutativeOperandList(node, lefts, rights);

            if (op == root) {
                return operands;
            } else {
                var tmp = sortAndGroup(operands, op);

                node.left = tmp.left;
                node.right = tmp.right;
            }
            return toCommutativeOperandList(node);
        }
        return new ArrayList<>();
    }

    private List<Node> toCommutativeOperandList(Node variable) {
        List<Node> operands = new ArrayList<>();

        operands.add(variable);
        return operands;
    }

    private List<Node> toCommutativeOperandList(Node operation, List<Node> leftsAsTarget, List<Node> rights) {
        if (leftsAsTarget.isEmpty()) {
            leftsAsTarget.add(operation.left);
        }

        if (rights.isEmpty()) {
            leftsAsTarget.add(operation.right);
        } else {
            leftsAsTarget.addAll(rights);
        }
        return leftsAsTarget;
    }

    private Node sortAndGroup(List<Node> operands, BinaryOperator operator) {
        operands.sort(NodeComparator.INSTANCE);

        return group(operands, operator, 0, operands.size());
    }

    private Node group(List<Node> source, BinaryOperator operator, int offset, int count) {
        if (count == 1) {
            return source.get(offset);
        } else {
            int right = ((count & 1) == 1) ? 1 : count / 2;
            int left = count - right;

            return Node.operation(
                    group(source, operator, offset, left),
                    group(source, operator, offset + left, right),
                    operator
            );
        }
    }
}
