package edu.kit.kastel.tva.eebc.typesystem;

public record State(
    ProgramState programState,
    ComponentState componentState
) {
    public State copy() {
        return new State(programState.copy(), componentState.copy());
    }

    public static State empty() {
        return new State(new ProgramState(), ComponentState.empty());
    }

    public static State random() {
        return new State(ProgramState.random(), ComponentState.empty());
    }

    @Override
    public String toString() {
        return String.format("[%s, %s]", programState, componentState);
    }
}
