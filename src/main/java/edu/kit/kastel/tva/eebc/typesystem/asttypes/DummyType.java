package edu.kit.kastel.tva.eebc.typesystem.asttypes;

import edu.kit.kastel.tva.eebc.typesystem.ExpressionType;
import edu.kit.kastel.tva.eebc.typesystem.State;

public class DummyType implements ExpressionType {
    @Override
    public int v(State before) {
        return 0;
    }

    @Override
    public State s(State before) {
        return before;
    }

    @Override
    public int e(State before) {
        return 0;
    }

    @Override
    public TypingState typingState(State before) {
        return TypingState.INCOMPLETE;
    }
}
