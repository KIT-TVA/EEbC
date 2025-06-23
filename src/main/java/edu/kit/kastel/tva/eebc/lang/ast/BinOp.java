package edu.kit.kastel.tva.eebc.lang.ast;

import java.util.function.IntBinaryOperator;

public class BinOp extends Expression {
    private final Expression left;
    private final Expression right;
    private final Op op;

    public BinOp(Expression left, Expression right, Op op) {
        this.left = left;
        this.right = right;
        this.op = op;
    }

    public Expression getLeft() {
        return left;
    }

    public Expression getRight() {
        return right;
    }

    public Op getOp() {
        return op;
    }

    @Override
    public <S, E extends S> E accept(StatementVisitor<S, E> visitor) {
        return visitor.visitBinOp(this);
    }

    public enum Op implements IntBinaryOperator {
        ADD(Integer::sum),
        MIN((left, right) -> left - right),
        MUL((left, right) -> left * right),
        DIV((left, right) -> left / right),
        MOD((left, right) -> left % right),
        AND((left, right) -> left & right),
        OR((left, right) -> left | right),
        XOR((left, right) -> left ^ right),
        SHL((left, right) -> left << right),
        SHR((left, right) -> left >> right),
        USHR((left, right) -> left >>> right),
        EQ((left, right) -> left == right ? 1 : 0),
        NE((left, right) -> left != right ? 1 : 0),
        LT((left, right) -> left < right ? 1 : 0),
        LE((left, right) -> left <= right ? 1 : 0),
        GT((left, right) -> left > right ? 1 : 0),
        GE((left, right) -> left >= right ? 1 : 0),
        LOG((left, right) -> (int) Math.ceil(Math.log(left) / Math.log(right)));

        private final IntBinaryOperator op;

        Op(IntBinaryOperator op) {
            this.op = op;
        }

        @Override
        public int applyAsInt(int left, int right) {
            return op.applyAsInt(left, right);
        }
    }
}
