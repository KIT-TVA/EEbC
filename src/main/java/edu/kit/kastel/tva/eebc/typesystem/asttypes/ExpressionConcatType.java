package edu.kit.kastel.tva.eebc.typesystem.asttypes;

import edu.kit.kastel.tva.eebc.lang.ast.ExpressionConcat;
import edu.kit.kastel.tva.eebc.typesystem.ExpressionType;
import edu.kit.kastel.tva.eebc.typesystem.State;
import edu.kit.kastel.tva.eebc.typesystem.StatementType;
import edu.kit.kastel.tva.eebc.typesystem.TypingVisitor;

public class ExpressionConcatType implements ExpressionType {
    private final StatementType left;
    private final ExpressionType right;

    public ExpressionConcatType(ExpressionConcat expressionConcat, TypingVisitor visitor) {
        this.left = expressionConcat.getLeft().accept(visitor);
        this.right = expressionConcat.getRight().accept(visitor);
    }

    @Override
    public int v(State before) {
        return right.v(left.s(before));
    }

    @Override
    public State s(State before) {
        return right.s(left.s(before));
    }

    @Override
    public int e(State before) {
        return left.e(before) + right.e(left.s(before));
    }

    @Override
    public TypingState typingState(State before) {
        return left.typingState(before).combine(right.typingState(left.s(before)));
    }
}
