package edu.kit.kastel.tva.eebc.lang.ast;

public class VarAssignment extends Statement {
    private String varName;
    private Expression value;

    public VarAssignment(String varName, Expression value) {
        this.varName = varName;
        this.value = value;
    }

    public String getVarName() {
        return varName;
    }

    public Expression getValue() {
        return value;
    }

    @Override
    public <S, E extends S> S accept(StatementVisitor<S, E> visitor) {
        return visitor.visitVarAssignment(this);
    }
}
