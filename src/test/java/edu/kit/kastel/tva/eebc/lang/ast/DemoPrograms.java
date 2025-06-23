package edu.kit.kastel.tva.eebc.lang.ast;

import edu.kit.kastel.tva.eebc.typesystem.ExpressionType;
import edu.kit.kastel.tva.eebc.typesystem.TestHardwareModel;
import edu.kit.kastel.tva.eebc.typesystem.State;
import edu.kit.kastel.tva.eebc.typesystem.StatementType;

import java.util.function.Function;
import java.util.function.ToIntBiFunction;
import java.util.function.ToIntFunction;
import java.util.stream.Stream;

public class DemoPrograms {
    /*
     * HELPER RECORDS
     */
    public record ExpectedExpressionType(
            ToIntFunction<State> v,
            Function<State, State> s,
            ToIntBiFunction<State, TestHardwareModel> e) {
        public ExpressionType asExpressionType(TestHardwareModel hardwareModel) {
            return new ExprType(v, s, s -> e.applyAsInt(s, hardwareModel));
        }
    }

    public record ExpectedStatementType(
            Function<State, State> s,
            ToIntBiFunction<State, TestHardwareModel> e) {
        public StatementType asStatementType(TestHardwareModel hardwareModel) {
            return new StmtType(s, s -> e.applyAsInt(s, hardwareModel));
        }
    }

    public record ExprType(
            ToIntFunction<State> v,
            Function<State, State> s,
            ToIntFunction<State> e) implements ExpressionType {
        @Override
        public int v(State before) {
            return v.applyAsInt(before);
        }

        @Override
        public State s(State before) {
            return s.apply(before);
        }

        @Override
        public int e(State before) {
            return e.applyAsInt(before);
        }

        @Override
        public TypingState typingState(State before) {
            return TypingState.COMPLETE;
        }
    }

    public record StmtType(
            Function<State, State> s,
            ToIntFunction<State> e) implements StatementType {
        @Override
        public State s(State before) {
            return s.apply(before);
        }

        @Override
        public int e(State before) {
            return e.applyAsInt(before);
        }

        @Override
        public TypingState typingState(State before) {
            return TypingState.COMPLETE;
        }
    }

    public record ExpressionCase(
            String name,
            Expression expression,
            ExpectedExpressionType type) {
    }

    public record StatementCase(
            String name,
            Statement statement,
            ExpectedStatementType type) {
    }

    public static Stream<ExpressionCase> expressions() {
        return Stream.of(
                new ExpressionCase("SIMPLE_SUM", SIMPLE_SUM, new ExpectedExpressionType(SIMPLE_SUM_V, SIMPLE_SUM_S, SIMPLE_SUM_E)),
                new ExpressionCase("READ_I", READ_I, new ExpectedExpressionType(READ_I_V, READ_I_S, READ_I_E)),
                new ExpressionCase("STORED_SUM", STORED_SUM, new ExpectedExpressionType(STORED_SUM_V, STORED_SUM_S, STORED_SUM_E)),
                new ExpressionCase("STORED_SUM2", STORED_SUM2, new ExpectedExpressionType(STORED_SUM2_V, STORED_SUM2_S, STORED_SUM2_E)),
                new ExpressionCase("IF_THEN_ELSE", IF_THEN_ELSE, new ExpectedExpressionType(IF_THEN_ELSE_V, IF_THEN_ELSE_S, IF_THEN_ELSE_E)),
                new ExpressionCase("IF_THEN_ELSE2", IF_THEN_ELSE2, new ExpectedExpressionType(IF_THEN_ELSE2_V, IF_THEN_ELSE2_S, IF_THEN_ELSE2_E))
        );
    }

    public static Stream<StatementCase> statements() {
        return Stream.of(
                new StatementCase("STORED_I", STORED_I, new ExpectedStatementType(STORED_I_S, STORED_I_E)),
                new StatementCase("STORED_IJ", STORED_IJ, new ExpectedStatementType(STORED_IJ_S, STORED_IJ_E)),
                new StatementCase("IF_THEN_ELSE3", IF_THEN_ELSE3, new ExpectedStatementType(IF_THEN_ELSE3_S, IF_THEN_ELSE3_E)),
                new StatementCase("INCREMENT", INCREMENT, new ExpectedStatementType(INCREMENT_S, INCREMENT_E))
        );
    }

    //
    // FROM HERE: PROGRAMS AND EXPECTED VALUES
    //

    /*
     * HELPERS FOR READABILITY
     */
    private static final String I = "i";
    private static final String J = "j";
    private static final String K = "k";
    private static final Const C0 = new Const(0);
    private static final Const C1 = new Const(1);
    private static final Const C2 = new Const(2);
    private static final Const C7 = new Const(7);

    /*
     * 1 + 2
     */
    private static final Expression SIMPLE_SUM = new BinOp(C1, C2, BinOp.Op.ADD);
    private static final ToIntFunction<State> SIMPLE_SUM_V = _ -> 3;
    private static final Function<State, State> SIMPLE_SUM_S = Function.identity();
    private static final ToIntBiFunction<State, TestHardwareModel> SIMPLE_SUM_E = (s, h) ->
            2 * h.timeDependentEnergyConsumption(s.componentState(), h.timeConst()) + h.timeDependentEnergyConsumption(s.componentState(), h.timeBinOp(BinOp.Op.ADD));

    /*
     * i = 1
     */
    private static final Statement STORED_I = new VarAssignment(I, C1);
    private static final Function<State, State> STORED_I_S = s -> {
        State out = s.copy();
        out.programState().setVariable(I, 1);
        return out;
    };
    private static final ToIntBiFunction<State, TestHardwareModel> STORED_I_E = (s, h) ->
            h.timeDependentEnergyConsumption(s.componentState(), h.timeVarAssignment()) + h.timeDependentEnergyConsumption(s.componentState(), h.timeConst());

    /*
     * i
     */
    private static final Expression READ_I = new Var(I);
    private static final ToIntFunction<State> READ_I_V = s -> s.programState().getVariable(I);
    private static final Function<State, State> READ_I_S = Function.identity();
    private static final ToIntBiFunction<State, TestHardwareModel> READ_I_E = (s, h) ->
            h.timeDependentEnergyConsumption(s.componentState(), h.timeVar());

    /*
     * i = 1; j = 2
     */
    private static final Statement STORED_IJ = new StatementConcat(
            new VarAssignment(I, C1),
            new VarAssignment(J, C2)
    );
    private static final Function<State, State> STORED_IJ_S = s -> {
        State out = s.copy();
        out.programState().setVariable(I, 1);
        out.programState().setVariable(J, 2);
        return out;
    };
    private static final ToIntBiFunction<State, TestHardwareModel> STORED_IJ_E = (s, h) ->
            2 * h.timeDependentEnergyConsumption(s.componentState(), h.timeVarAssignment()) + 2 * h.timeDependentEnergyConsumption(s.componentState(), h.timeConst());

    /*
     * i = 1; j = 2; i + j
     */
    private static final Expression STORED_SUM = new ExpressionConcat(
            new StatementConcat(
                    new VarAssignment(I, C1),
                    new VarAssignment(J, C2)
            ),
            new BinOp(new Var(I), new Var(J), BinOp.Op.ADD)
    );
    private static final ToIntFunction<State> STORED_SUM_V = _ -> 3;
    private static final Function<State, State> STORED_SUM_S = s -> {
        State out = s.copy();
        out.programState().setVariable(I, 1);
        out.programState().setVariable(J, 2);
        return out;
    };
    private static final ToIntBiFunction<State, TestHardwareModel> STORED_SUM_E = (s, h) ->
            2 * h.timeDependentEnergyConsumption(s.componentState(), h.timeVarAssignment()) +
                    2 * h.timeDependentEnergyConsumption(s.componentState(), h.timeConst()) +
                    2 * h.timeDependentEnergyConsumption(s.componentState(), h.timeVar()) +
                    h.timeDependentEnergyConsumption(s.componentState(), h.timeBinOp(BinOp.Op.ADD));

    /*
     * i = 1; j = 2; k = i + j; j = 7; k
     */
    private static final Expression STORED_SUM2 = new ExpressionConcat(
            new StatementConcat(
                    new VarAssignment(I, C1),
                    new VarAssignment(J, C2),
                    new VarAssignment(K, new BinOp(new Var(I), new Var(J), BinOp.Op.ADD)),
                    new VarAssignment(J, C7)
            ),
            new Var(K)
    );
    private static final ToIntFunction<State> STORED_SUM2_V = _ -> 3;
    private static final Function<State, State> STORED_SUM2_S = s -> {
        State out = s.copy();
        out.programState().setVariable(I, 1);
        out.programState().setVariable(J, 7);
        out.programState().setVariable(K, 3);
        return out;
    };
    private static final ToIntBiFunction<State, TestHardwareModel> STORED_SUM2_E = (s, h) ->
            4 * h.timeDependentEnergyConsumption(s.componentState(), h.timeVarAssignment()) +
                    3 * h.timeDependentEnergyConsumption(s.componentState(), h.timeConst()) +
                    3 * h.timeDependentEnergyConsumption(s.componentState(), h.timeVar()) +
                    h.timeDependentEnergyConsumption(s.componentState(), h.timeBinOp(BinOp.Op.ADD));

    /*
     * if 1 then i = 1 else i = 2; i
     */
    private static final Expression IF_THEN_ELSE = new ExpressionConcat(
            new IfThenElse(C1, new VarAssignment(I, C1), new VarAssignment(I, C2)),
            new Var(I)
    );
    private static final ToIntFunction<State> IF_THEN_ELSE_V = _ -> 1;
    private static final Function<State, State> IF_THEN_ELSE_S = s -> {
        State out = s.copy();
        out.programState().setVariable(I, 1);
        return out;
    };
    private static final ToIntBiFunction<State, TestHardwareModel> IF_THEN_ELSE_E = (s, h) ->
            h.timeDependentEnergyConsumption(s.componentState(), h.timeIf()) +
                    h.timeDependentEnergyConsumption(s.componentState(), h.timeVarAssignment()) +
                    h.timeDependentEnergyConsumption(s.componentState(), h.timeVar()) +
                    2 * h.timeDependentEnergyConsumption(s.componentState(), h.timeConst());

    /*
     * if 0 then i = 1 else i = 2; i
     */
    private static final Expression IF_THEN_ELSE2 = new ExpressionConcat(
            new IfThenElse(C0, new VarAssignment(I, C1), new VarAssignment(I, C2)),
            new Var(I)
    );
    private static final ToIntFunction<State> IF_THEN_ELSE2_V = _ -> 2;
    private static final Function<State, State> IF_THEN_ELSE2_S = s -> {
        State out = s.copy();
        out.programState().setVariable(I, 2);
        return out;
    };
    private static final ToIntBiFunction<State, TestHardwareModel> IF_THEN_ELSE2_E = (s, h) ->
            h.timeDependentEnergyConsumption(s.componentState(), h.timeIf()) +
                    h.timeDependentEnergyConsumption(s.componentState(), h.timeVarAssignment()) +
                    h.timeDependentEnergyConsumption(s.componentState(), h.timeVar()) +
                    2 * h.timeDependentEnergyConsumption(s.componentState(), h.timeConst());

    /*
     * i = 1; j = 2; k = i + j; if k then i = 2 else i = 7
     */
    private static final Statement IF_THEN_ELSE3 = new StatementConcat(
            new VarAssignment(I, C1),
            new VarAssignment(J, C2),
            new VarAssignment(K, new BinOp(new Var(I), new Var(J), BinOp.Op.ADD)),
            new IfThenElse(new Var(K), new VarAssignment(I, C2), new VarAssignment(I, C7))
    );
    private static final Function<State, State> IF_THEN_ELSE3_S = s -> {
        State out = s.copy();
        out.programState().setVariable(I, 2);
        out.programState().setVariable(J, 2);
        out.programState().setVariable(K, 3);
        return out;
    };
    private static final ToIntBiFunction<State, TestHardwareModel> IF_THEN_ELSE3_E = (s, h) ->
            4 * h.timeDependentEnergyConsumption(s.componentState(), h.timeVarAssignment()) +
                    3 * h.timeDependentEnergyConsumption(s.componentState(), h.timeConst()) +
                    3 * h.timeDependentEnergyConsumption(s.componentState(), h.timeVar()) +
                    h.timeDependentEnergyConsumption(s.componentState(), h.timeBinOp(BinOp.Op.ADD)) +
                    h.timeDependentEnergyConsumption(s.componentState(), h.timeIf());

    /*
     * i = 0; repeat 7 i = i + 1
     */
    private static final Statement INCREMENT = new StatementConcat(
            new VarAssignment(I, C0),
            new Repeat(C7, new VarAssignment(I, new BinOp(new Var(I), C1, BinOp.Op.ADD)))
    );
    private static final Function<State, State> INCREMENT_S = s -> {
        State out = s.copy();
        out.programState().setVariable(I, 7);
        return out;
    };
    private static final ToIntBiFunction<State, TestHardwareModel> INCREMENT_E = (s, h) ->
            8 * h.timeDependentEnergyConsumption(s.componentState(), h.timeVarAssignment()) +
                    9 * h.timeDependentEnergyConsumption(s.componentState(), h.timeConst()) +
                    7 * h.timeDependentEnergyConsumption(s.componentState(), h.timeVar()) +
                    7 * h.timeDependentEnergyConsumption(s.componentState(), h.timeBinOp(BinOp.Op.ADD));
}
