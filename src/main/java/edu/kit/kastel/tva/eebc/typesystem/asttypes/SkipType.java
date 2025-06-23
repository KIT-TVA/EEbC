package edu.kit.kastel.tva.eebc.typesystem.asttypes;

import edu.kit.kastel.tva.eebc.typesystem.State;
import edu.kit.kastel.tva.eebc.typesystem.StatementType;

public class SkipType implements StatementType {
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
        return TypingState.COMPLETE;
    }
}
