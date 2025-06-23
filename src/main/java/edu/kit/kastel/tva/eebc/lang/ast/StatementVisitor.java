package edu.kit.kastel.tva.eebc.lang.ast;

public interface StatementVisitor<S, E extends S> extends ExpressionVisitor<E> {
    S visitIfThenElse(IfThenElse ifThenElse);
    S visitRepeat(Repeat repeat);
    S visitSkip(Skip skip);
    S visitStatementConcat(StatementConcat statementConcat);
    S visitVarAssignment(VarAssignment varAssign);
}
