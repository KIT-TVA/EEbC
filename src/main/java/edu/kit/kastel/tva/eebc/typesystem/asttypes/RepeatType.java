package edu.kit.kastel.tva.eebc.typesystem.asttypes;

import edu.kit.kastel.tva.eebc.lang.ast.Repeat;
import edu.kit.kastel.tva.eebc.typesystem.ExpressionType;
import edu.kit.kastel.tva.eebc.typesystem.State;
import edu.kit.kastel.tva.eebc.typesystem.StatementType;
import edu.kit.kastel.tva.eebc.typesystem.TypingVisitor;

public class RepeatType implements StatementType{
    private final ExpressionType count;
    private final StatementType body;

    public RepeatType(Repeat repeat, TypingVisitor visitor) {
        this.count = repeat.getCount().accept(visitor);
        this.body = repeat.getBody().accept(visitor);
    }

    @Override
    public State s(State before) {
        return TypingVisitor.repeat(count::v, count::s, body::s).apply(before);
    }

    @Override
    public int e(State before) {
        return count.e(before) + TypingVisitor.repeat(count::v, count::s, body::e, body::s).applyAsInt(before);
    }

    @Override
    public TypingState typingState(State before) {
        return count.typingState(before).equals(TypingState.COMPLETE) ? TypingState.COMPLETE : TypingState.INCOMPLETE;
    }
}
