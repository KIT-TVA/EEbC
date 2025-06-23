package edu.kit.kastel.tva.eebc.lang.ast;

public interface ExpressionVisitor<E> {
    E visitBinOp(BinOp binOp);
    E visitConst(Const const_);
    E visitExpressionConcat(ExpressionConcat expressionConcat);
    E visitInput(Input input);
    E visitVar(Var var);
    E visitCallCompFun(CallCompFun callCompFun);
}
