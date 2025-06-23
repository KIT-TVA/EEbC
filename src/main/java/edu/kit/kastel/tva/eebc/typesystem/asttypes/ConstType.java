package edu.kit.kastel.tva.eebc.typesystem.asttypes;

import edu.kit.kastel.tva.eebc.lang.ast.Const;
import edu.kit.kastel.tva.eebc.typesystem.ExpressionType;
import edu.kit.kastel.tva.eebc.typesystem.HardwareModel;
import edu.kit.kastel.tva.eebc.typesystem.State;

public class ConstType implements ExpressionType {
    private final int value;

    private final HardwareModel hardwareModel;

    public ConstType(Const const_, HardwareModel hardwareModel) {
        this.value = const_.getValue();
        this.hardwareModel = hardwareModel;
    }

    @Override
    public int v(State before) {
        return value;
    }

    @Override
    public State s(State before) {
        return before;
    }

    @Override
    public int e(State before) {
        return hardwareModel.timeDependentEnergyConsumption(before.componentState(), hardwareModel.timeConst());
    }

    @Override
    public TypingState typingState(State before) {
        return TypingState.COMPLETE;
    }
}
