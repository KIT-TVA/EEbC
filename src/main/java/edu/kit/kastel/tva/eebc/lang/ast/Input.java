package edu.kit.kastel.tva.eebc.lang.ast;

public class Input extends Expression {
    private String name;

    public Input(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public <S, E extends S> E accept(StatementVisitor<S, E> visitor) {
        return visitor.visitInput(this);
    }
}
