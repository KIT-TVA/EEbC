package edu.kit.kastel.tva.eebc.typesystem.asttypes;

import edu.kit.kastel.tva.eebc.lang.ast.CallCompFun;
import edu.kit.kastel.tva.eebc.typesystem.ExpressionType;
import edu.kit.kastel.tva.eebc.typesystem.HardwareModel;
import edu.kit.kastel.tva.eebc.typesystem.State;
import edu.kit.kastel.tva.eebc.typesystem.TypingVisitor;

public class CallCompFunType implements ExpressionType {
    private final String name;
    private final ExpressionType arg;

    private final HardwareModel hardwareModel;

    public CallCompFunType(CallCompFun callCompFun, TypingVisitor visitor, HardwareModel hardwareModel) {
        this.arg = callCompFun.getArg().accept(visitor);
        this.name = callCompFun.getName();

        this.hardwareModel = hardwareModel;
    }

    @Override
    public int v(State before) {
        State substituted = TypingVisitor.higherOrderSubstitution(arg::v, arg::s).apply(before);
        return substituted.componentState().fun(name, arg.v(substituted));
    }

    @Override
    public State s(State before) {
        return TypingVisitor.split(arg::s, TypingVisitor.higherOrderSubstitution(arg::v, arg::s).andThen(s ->
                new State(s.programState(), s.componentState().succ(name, arg.v(s))
        ))).apply(before);
    }

    @Override
    public int e(State before) {
        State substituted = TypingVisitor.higherOrderSubstitution(arg::v, arg::s).apply(before);
        return arg.e(before) +
                hardwareModel.timeDependentEnergyConsumption(
                        substituted.componentState(),
                        hardwareModel.timeCallCompFun(name)
                ) +
                substituted.componentState().ec(name, arg.v(substituted));
    }

    @Override
    public TypingState typingState(State before) {
        return null; // TODO
    }
}
