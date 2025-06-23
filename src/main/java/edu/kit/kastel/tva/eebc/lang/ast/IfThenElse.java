package edu.kit.kastel.tva.eebc.lang.ast;

public class IfThenElse extends Statement {
    private Expression condition;
    private Statement thenBranch;
    private Statement elseBranch;

    public IfThenElse(Expression condition, Statement thenBranch, Statement elseBranch) {
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }

    public Expression getCondition() {
        return condition;
    }

    public Statement getThenBranch() {
        return thenBranch;
    }

    public Statement getElseBranch() {
        return elseBranch;
    }

    public void setElseBranch(Statement elseBranch) {
        this.elseBranch = elseBranch;
    }

    @Override
    public <S, E extends S> S accept(StatementVisitor<S, E> visitor) {
        return visitor.visitIfThenElse(this);
    }
}
