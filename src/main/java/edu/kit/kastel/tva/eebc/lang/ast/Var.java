package edu.kit.kastel.tva.eebc.lang.ast;

public class Var extends Expression {
    private String name;

    public Var(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public <S, E extends S> E accept(StatementVisitor<S, E> visitor) {
        return visitor.visitVar(this);
    }
}
