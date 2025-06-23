package edu.kit.kastel.tva.eebc.typesystem;


public interface StatementType {
    State s(State before);
    int e(State before);
    TypingState typingState(State before);

    enum TypingState {
        COMPLETE {
            @Override
            public TypingState combine(TypingState other) {
                return switch (other) {
                    case COMPLETE -> COMPLETE;
                    case ESTIMATED -> ESTIMATED;
                    case INCOMPLETE -> INCOMPLETE;
                };
            }
        },
        ESTIMATED {
            @Override
            public TypingState combine(TypingState other) {
                return switch (other) {
                    case COMPLETE, ESTIMATED -> ESTIMATED;
                    case INCOMPLETE -> INCOMPLETE;
                };
            }
        },
        INCOMPLETE {
            @Override
            public TypingState combine(TypingState other) {
                return INCOMPLETE;
            }
        };

        public abstract TypingState combine(TypingState other);
    }
}
