package edu.kit.kastel.tva.eebc.typesystem.asttypes;

import edu.kit.kastel.tva.eebc.lang.ast.BinOp;
import edu.kit.kastel.tva.eebc.typesystem.ExpressionType;
import edu.kit.kastel.tva.eebc.typesystem.HardwareModel;
import edu.kit.kastel.tva.eebc.typesystem.State;
import edu.kit.kastel.tva.eebc.typesystem.TypingVisitor;

public class BinOpType implements ExpressionType {
    private final ExpressionType left;
    private final ExpressionType right;
    private final BinOp.Op op;

    private final HardwareModel hardwareModel;

    public BinOpType(BinOp binOp, TypingVisitor visitor, HardwareModel hardwareModel) {
        this.left = binOp.getLeft() == null ? new DummyType() : binOp.getLeft().accept(visitor);
        this.right = binOp.getRight() == null ? new DummyType() : binOp.getRight().accept(visitor);
        this.op = binOp.getOp();

        this.hardwareModel = hardwareModel;
    }

    @Override
    public int v(State before) {
        return op.applyAsInt(left.v(before), right.v(left.s(before)));
    }

    @Override
    public State s(State before) {
        return right.s(left.s(before));
    }

    @Override
    public int e(State before) {
        return left.e(before) +
                right.e(left.s(before)) +
                hardwareModel.timeDependentEnergyConsumption(
                        right.s(left.s(before)).componentState(),
                        hardwareModel.timeBinOp(op)
                );
    }

    @Override
    public TypingState typingState(State before) {
        return left.typingState(before).combine(right.typingState(left.s(before)));
    }
}
