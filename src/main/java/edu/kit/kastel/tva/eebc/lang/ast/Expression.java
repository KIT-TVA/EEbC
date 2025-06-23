package edu.kit.kastel.tva.eebc.lang.ast;

public abstract class Expression extends Statement {
    @Override
    public abstract <S, E extends S> E accept(StatementVisitor<S, E> visitor);
}
