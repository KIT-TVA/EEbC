package edu.kit.kastel.tva.eebc.typesystem.asttypes;

import edu.kit.kastel.tva.eebc.lang.ast.IfThenElse;
import edu.kit.kastel.tva.eebc.typesystem.ExpressionType;
import edu.kit.kastel.tva.eebc.typesystem.HardwareModel;
import edu.kit.kastel.tva.eebc.typesystem.State;
import edu.kit.kastel.tva.eebc.typesystem.StatementType;
import edu.kit.kastel.tva.eebc.typesystem.TypingVisitor;

public class IfThenElseType implements StatementType {
    private final ExpressionType condition;
    private final StatementType thenBranch;
    private final StatementType elseBranch;

    private final HardwareModel hardwareModel;

    public IfThenElseType(IfThenElse ifThenElse, TypingVisitor typingVisitor, HardwareModel hardwareModel) {
        this.condition = ifThenElse.getCondition().accept(typingVisitor);
        this.thenBranch = ifThenElse.getThenBranch().accept(typingVisitor);
        this.elseBranch = ifThenElse.getElseBranch().accept(typingVisitor);

        this.hardwareModel = hardwareModel;
    }

    @Override
    public State s(State before) {
        return TypingVisitor.ifThenElse(condition::v, thenBranch::s, elseBranch::s).apply(condition.s(before));
    }

    @Override
    public int e(State before) {
        return condition.e(before) +
                TypingVisitor.ifThenElse(condition::v, thenBranch::e, elseBranch::e).applyAsInt(condition.s(before)) +
                hardwareModel.timeDependentEnergyConsumption(
                        condition.s(before).componentState(),
                        hardwareModel.timeIf()
                );
    }

    @Override
    public TypingState typingState(State before) {
        if (condition.typingState(before).equals(TypingState.COMPLETE)) {
            int val = condition.v(before);

            if (val > 0) {
                return thenBranch.typingState(condition.s(before));
            } else {
                return elseBranch.typingState(condition.s(before));
            }
        } else {
            return TypingState.INCOMPLETE; // TODO
        }
    }
}
