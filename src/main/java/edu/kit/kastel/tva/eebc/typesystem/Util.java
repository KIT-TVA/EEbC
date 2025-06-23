package edu.kit.kastel.tva.eebc.typesystem;

import java.util.function.Function;
import java.util.function.ToIntFunction;

public final class Util {
    private Util() {

    }

    public static <T> ToIntFunction<T> add(ToIntFunction<T> f1, ToIntFunction<T> f2) {
        return t -> f1.applyAsInt(t) + f2.applyAsInt(t);
    }
}
