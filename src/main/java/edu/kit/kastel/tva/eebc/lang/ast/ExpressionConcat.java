package edu.kit.kastel.tva.eebc.lang.ast;

import java.util.Arrays;

public class ExpressionConcat extends Expression {
    private Statement left;
    private Expression right;

    public ExpressionConcat(Statement left, Expression right) {
        this.left = left;
        this.right = right;
    }

    public ExpressionConcat(Statement left, Expression right, Expression... more) {
        this.left = left;

        if (more.length == 0) {
            this.right = right;
        } else if (more.length == 1) {
            this.right = new ExpressionConcat(right, more[0]);
        } else {
            this.right = new ExpressionConcat(right, more[0], Arrays.copyOfRange(more, 1, more.length));
        }
    }

    public Statement getLeft() {
        return left;
    }

    public Expression getRight() {
        return right;
    }

    @Override
    public <S, E extends S> E accept(StatementVisitor<S, E> visitor) {
        return visitor.visitExpressionConcat(this);
    }
}
