package edu.kit.kastel.tva.eebc.lang.ast;

public abstract class Statement {
    public abstract <S, E extends S> S accept(StatementVisitor<S, E> visitor);
}
