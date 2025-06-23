package edu.kit.kastel.tva.eebc.typesystem;

import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

public class ProgramState {
    private final HashMap<String, Integer> variables;

    public static ProgramState random() {
        ProgramState random = new ProgramState();
        Random generator = new Random();
        for (char c = 'a'; c <= 'z'; c++) {
            random.setVariable(String.valueOf(c), generator.nextInt(-128, 128));
        }
        return random;
    }

    public ProgramState() {
        this.variables = new HashMap<>();
    }

    public int getVariable(String name) {
        if (!variables.containsKey(name)) {
            return 0;
        }

        return variables.get(name);
    }

    public void setVariable(String name, int value) {
        variables.put(name, value);
    }

    public ProgramState copy() {
        ProgramState copy = new ProgramState();
        copy.variables.putAll(variables);
        return copy;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ProgramState that = (ProgramState) o;
        return Objects.equals(variables, that.variables);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(variables);
    }

    @Override
    public String toString() {
        return variables.toString();
    }
}
