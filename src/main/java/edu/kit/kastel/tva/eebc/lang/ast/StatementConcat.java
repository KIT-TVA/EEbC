package edu.kit.kastel.tva.eebc.lang.ast;

import java.util.Arrays;

public class StatementConcat extends Statement {
    private Statement left;
    private Statement right;

    public StatementConcat(Statement left, Statement right) {
        this.left = left;
        this.right = right;
    }

    public StatementConcat(Statement left, Statement right, Statement... more) {
        this.left = left;

        if (more.length == 0) {
            this.right = right;
        } else if (more.length == 1) {
            this.right = new StatementConcat(right, more[0]);
        } else {
            this.right = new StatementConcat(right, more[0], Arrays.copyOfRange(more, 1, more.length));
        }
    }

    public Statement getLeft() {
        return left;
    }

    public Statement getRight() {
        return right;
    }

    @Override
    public <S, E extends S> S accept(StatementVisitor<S, E> visitor) {
        return visitor.visitStatementConcat(this);
    }
}
