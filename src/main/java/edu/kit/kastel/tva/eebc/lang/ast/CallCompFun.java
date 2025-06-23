package edu.kit.kastel.tva.eebc.lang.ast;

public class CallCompFun extends Expression {
    private final String name;
    private final Expression arg;

    public CallCompFun(String name, Expression arg) {
        this.name = name;
        this.arg = arg;
    }

    public String getName() {
        return name;
    }

    public Expression getArg() {
        return arg;
    }

    @Override
    public <S, E extends S> E accept(StatementVisitor<S, E> visitor) {
        return visitor.visitCallCompFun(this);
    }
}
