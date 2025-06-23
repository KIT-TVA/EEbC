package edu.kit.kastel.tva.eebc.typesystem.asttypes;

import edu.kit.kastel.tva.eebc.lang.ast.StatementConcat;
import edu.kit.kastel.tva.eebc.typesystem.State;
import edu.kit.kastel.tva.eebc.typesystem.StatementType;
import edu.kit.kastel.tva.eebc.typesystem.TypingVisitor;

public class StatementConcatType implements StatementType {
    private final StatementType left;
    private final StatementType right;

    public StatementConcatType(StatementConcat statementConcat, TypingVisitor visitor) {
        if (statementConcat.getLeft() == null) {
            this.left = new DummyType();
        } else {
            this.left = statementConcat.getLeft().accept(visitor);
        }

        if (statementConcat.getRight() == null) {
            this.right = new DummyType();
        } else {
            this.right = statementConcat.getRight().accept(visitor);
        }
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
