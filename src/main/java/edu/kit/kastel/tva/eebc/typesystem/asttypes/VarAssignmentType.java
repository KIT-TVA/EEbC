package edu.kit.kastel.tva.eebc.typesystem.asttypes;

import edu.kit.kastel.tva.eebc.lang.ast.VarAssignment;
import edu.kit.kastel.tva.eebc.typesystem.ExpressionType;
import edu.kit.kastel.tva.eebc.typesystem.HardwareModel;
import edu.kit.kastel.tva.eebc.typesystem.State;
import edu.kit.kastel.tva.eebc.typesystem.StatementType;
import edu.kit.kastel.tva.eebc.typesystem.TypingVisitor;

public class VarAssignmentType implements StatementType {
    private final String name;
    private final ExpressionType value;

    private final HardwareModel hardwareModel;

    public VarAssignmentType(VarAssignment varAssign, TypingVisitor visitor, HardwareModel hardwareModel) {
        this.name = varAssign.getVarName();
        this.value = varAssign.getValue() == null ? new DummyType() : varAssign.getValue().accept(visitor);

        this.hardwareModel = hardwareModel;
    }

    @Override
    public State s(State before) {
        return TypingVisitor.assign(name, value::v).apply(value.s(before));
    }

    @Override
    public int e(State before) {
        return value.e(before) +
                hardwareModel.timeDependentEnergyConsumption(
                        value.s(before).componentState(),
                        hardwareModel.timeVarAssignment()
                );
    }

    @Override
    public TypingState typingState(State before) {
        return value.typingState(before);
    }
}
