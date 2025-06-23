package edu.kit.kastel.tva.eebc.lang.ast;

public class Skip extends Statement {
    @Override
    public <S, E extends S> S accept(StatementVisitor<S, E> visitor) {
        return visitor.visitSkip(this);
    }
}
