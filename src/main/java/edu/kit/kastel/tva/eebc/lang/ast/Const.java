package edu.kit.kastel.tva.eebc.lang.ast;

public class Const extends Expression {
    private int value;

    public Const(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public <S, E extends S> E accept(StatementVisitor<S, E> visitor) {
        return visitor.visitConst(this);
    }
}
