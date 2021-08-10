package com.zhytnik.algo.brand.filter.ast;

import com.zhytnik.algo.brand.data.BinaryOperator;
import com.zhytnik.algo.brand.settings.FeatureSettings;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.UnaryOperator;

import static com.zhytnik.algo.brand.data.BinaryOperator.SUBTRACTION;

/**
 * Groups expressions to the groups of the maximum possible size and sorts them according to their sizes and variables' names.
 */
public final class GroupAndSort implements UnaryOperator<Tree> {

    private final Comparator<Node> comparator;

    public GroupAndSort() {
        this(FeatureSettings.defaultSettings());
    }

    public GroupAndSort(FeatureSettings settings) {
        this(settings.nodeComparator());
    }

    public GroupAndSort(Comparator<Node> comparator) {
        this.comparator = Objects.requireNonNull(comparator);
    }

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
        var operands = new ArrayList<Node>();

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
        operands.sort(comparator);

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
