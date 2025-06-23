package edu.kit.kastel.tva.eebc.typesystem;

import edu.kit.kastel.tva.eebc.lang.ast.BinOp;
import edu.kit.kastel.tva.eebc.lang.ast.CallCompFun;
import edu.kit.kastel.tva.eebc.lang.ast.Const;
import edu.kit.kastel.tva.eebc.lang.ast.ExpressionConcat;
import edu.kit.kastel.tva.eebc.lang.ast.IfThenElse;
import edu.kit.kastel.tva.eebc.lang.ast.Input;
import edu.kit.kastel.tva.eebc.lang.ast.Repeat;
import edu.kit.kastel.tva.eebc.lang.ast.Skip;
import edu.kit.kastel.tva.eebc.lang.ast.StatementConcat;
import edu.kit.kastel.tva.eebc.lang.ast.StatementVisitor;
import edu.kit.kastel.tva.eebc.lang.ast.Var;
import edu.kit.kastel.tva.eebc.lang.ast.VarAssignment;
import edu.kit.kastel.tva.eebc.typesystem.asttypes.BinOpType;
import edu.kit.kastel.tva.eebc.typesystem.asttypes.ConstType;
import edu.kit.kastel.tva.eebc.typesystem.asttypes.ExpressionConcatType;
import edu.kit.kastel.tva.eebc.typesystem.asttypes.IfThenElseType;
import edu.kit.kastel.tva.eebc.typesystem.asttypes.RepeatType;
import edu.kit.kastel.tva.eebc.typesystem.asttypes.SkipType;
import edu.kit.kastel.tva.eebc.typesystem.asttypes.StatementConcatType;
import edu.kit.kastel.tva.eebc.typesystem.asttypes.VarAssignmentType;
import edu.kit.kastel.tva.eebc.typesystem.asttypes.VarType;

import java.util.function.Function;
import java.util.function.ToIntFunction;

public class TypingVisitor implements StatementVisitor<StatementType, ExpressionType> {
    private final HardwareModel hardwareModel;

    public static Function<State, State> assign(String varName,
                                                ToIntFunction<State> v) {
        return s -> {
            State out = s.copy();
            out.programState().setVariable(varName, v.applyAsInt(s));
            return out;
        };
    }

    public static <T> Function<State, T> ifThenElse(ToIntFunction<State> condition,
                                                    Function<State, T> then,
                                                    Function<State, T> else_) {
        return s -> condition.applyAsInt(s) != 0 ? then.apply(s) : else_.apply(s);
    }

    public static ToIntFunction<State> ifThenElse(ToIntFunction<State> condition,
                                                    ToIntFunction<State> then,
                                                    ToIntFunction<State> else_) {
        return s -> condition.applyAsInt(s) != 0 ? then.applyAsInt(s) : else_.applyAsInt(s);
    }

    public static Function<State, State> repeat(ToIntFunction<State> count,
                                                Function<State, State> start,
                                                Function<State, State> body) {
        return s -> repeatInner(count.applyAsInt(s), body, start.apply(s));
    }

    public static ToIntFunction<State> repeat(ToIntFunction<State> count,
                                              Function<State, State> start,
                                              ToIntFunction<State> cost,
                                              Function<State, State> body) {
        return s -> repeatInner(count.applyAsInt(s), cost, body, start.apply(s));
    }

    public static Function<State, State> higherOrderSubstitution(ToIntFunction<State> arg,
                                                                 Function<State, State> before) {
        return s -> {
            ProgramState programState = new ProgramState();
            programState.setVariable("arg", arg.applyAsInt(s));
            return new State(programState, before.apply(s).componentState());
        };
    }

    public static Function<State, State> split(Function<State, State> left,
                                               Function<State, State> right) {
        return s -> {
            State leftState = left.apply(s);
            State rightState = right.apply(s);

            return new State(leftState.programState(), rightState.componentState());
        };
    }

    private static State repeatInner(int count,
                                    Function<State, State> body,
                                    State begin) {
        if (count <= 0) {
            return begin;
        } else {
            return repeatInner(count - 1, body, body.apply(begin));
        }
    }

    private static int repeatInner(int count,
                                   ToIntFunction<State> cost,
                                   Function<State, State> body,
                                   State begin) {
        if (count <= 0) {
            return 0;
        } else {
            return repeatInner(count - 1, cost, body, body.apply(begin)) + cost.applyAsInt(begin);
        }
    }

    public TypingVisitor(HardwareModel hardwareModel) {
        this.hardwareModel = hardwareModel;
    }

    @Override
    public IfThenElseType visitIfThenElse(IfThenElse ifThenElse) {
        return new IfThenElseType(ifThenElse, this, hardwareModel);
    }

    @Override
    public RepeatType visitRepeat(Repeat repeat) {
        return new RepeatType(repeat, this);
    }

    @Override
    public SkipType visitSkip(Skip skip) {
        return new SkipType();
    }

    @Override
    public StatementConcatType visitStatementConcat(StatementConcat statementConcat) {
        return new StatementConcatType(statementConcat, this);
    }

    @Override
    public VarAssignmentType visitVarAssignment(VarAssignment varAssign) {
        return new VarAssignmentType(varAssign, this, hardwareModel);
    }

    @Override
    public BinOpType visitBinOp(BinOp binOp) {
        return new BinOpType(binOp, this, hardwareModel);
    }

    @Override
    public ConstType visitConst(Const const_) {
        return new ConstType(const_, hardwareModel);
    }

    @Override
    public ExpressionConcatType visitExpressionConcat(ExpressionConcat expressionConcat) {
        return new ExpressionConcatType(expressionConcat, this);
    }

    @Override
    public ExpressionType visitInput(Input input) {
        return null;
    } // TODO

    @Override
    public VarType visitVar(Var var) {
        return new VarType(var, hardwareModel);
    }

    @Override
    public ExpressionType visitCallCompFun(CallCompFun callCompFun) {
        return null;
    } // TODO
}
