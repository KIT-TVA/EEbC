package edu.kit.kastel.tva.eebc.typesystem;

import java.util.function.ToIntFunction;

public record ConstValueFunction (
    int value
) implements ToIntFunction<State> {
    @Override
    public int applyAsInt(State value) {
        return this.value;
    }
}
