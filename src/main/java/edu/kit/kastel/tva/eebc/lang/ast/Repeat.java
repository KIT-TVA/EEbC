package edu.kit.kastel.tva.eebc.lang.ast;

public class Repeat extends Statement {
    private Expression count;
    private Statement body;

    public Repeat(Expression count, Statement body) {
        this.count = count;
        this.body = body;
    }

    public Expression getCount() {
        return count;
    }

    public Statement getBody() {
        return body;
    }

    @Override
    public <S, E extends S> S accept(StatementVisitor<S, E> visitor) {
        return visitor.visitRepeat(this);
    }
}
