package edu.kit.kastel.tva.eebc.typesystem.asttypes;

import edu.kit.kastel.tva.eebc.lang.ast.Var;
import edu.kit.kastel.tva.eebc.typesystem.ExpressionType;
import edu.kit.kastel.tva.eebc.typesystem.HardwareModel;
import edu.kit.kastel.tva.eebc.typesystem.State;

public class VarType implements ExpressionType {
    private final String name;

    private final HardwareModel hardwareModel;

    public VarType(Var var, HardwareModel hardwareModel) {
        this.name = var.getName();
        this.hardwareModel = hardwareModel;
    }

    @Override
    public int v(State before) {
        return before.programState().getVariable(name);
    }

    @Override
    public State s(State before) {
        return before;
    }

    @Override
    public int e(State before) {
        return hardwareModel.timeDependentEnergyConsumption(before.componentState(), hardwareModel.timeVar());
    }

    @Override
    public TypingState typingState(State before) {
        return TypingState.COMPLETE;
    }
}
