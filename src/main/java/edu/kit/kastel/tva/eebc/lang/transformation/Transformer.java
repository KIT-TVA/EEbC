package edu.kit.kastel.tva.eebc.lang.transformation;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import de.tu_bs.cs.isf.cbc.cbcmodel.AbstractStatement;
import de.tu_bs.cs.isf.cbc.cbcmodel.CbCFormula;
import de.tu_bs.cs.isf.cbc.cbcmodel.CompositionStatement;
import de.tu_bs.cs.isf.cbc.cbcmodel.Condition;
import de.tu_bs.cs.isf.cbc.cbcmodel.SelectionStatement;
import de.tu_bs.cs.isf.cbc.cbcmodel.SmallRepetitionStatement;
import de.tu_bs.cs.isf.cbc.cbcmodel.Variant;
import edu.kit.kastel.tva.eebc.lang.ast.BinOp;
import edu.kit.kastel.tva.eebc.lang.ast.Const;
import edu.kit.kastel.tva.eebc.lang.ast.Expression;
import edu.kit.kastel.tva.eebc.lang.ast.IfThenElse;
import edu.kit.kastel.tva.eebc.lang.ast.Repeat;
import edu.kit.kastel.tva.eebc.lang.ast.Skip;
import edu.kit.kastel.tva.eebc.lang.ast.Statement;
import edu.kit.kastel.tva.eebc.lang.ast.StatementConcat;
import edu.kit.kastel.tva.eebc.lang.ast.Var;
import edu.kit.kastel.tva.eebc.lang.ast.VarAssignment;
import java.util.List;

public class Transformer {
    private static final JavaParser JAVA_PARSER = new JavaParser();

    public static Statement transform(CbCFormula formula) {
        return transform(formula.getStatement());
    }

    private static Statement transform(AbstractStatement abstractStatement) {
        return switch (abstractStatement) {
            case CompositionStatement compositionStatement -> transform(compositionStatement);
            case SelectionStatement selectionStatement -> transform(selectionStatement);
            case SmallRepetitionStatement repetitionStatement -> transform(repetitionStatement);
            default -> {
                AbstractStatement refinement = abstractStatement.getRefinement();
                yield refinement == null ? parse(abstractStatement.getName()) : transform(refinement);
            }
        };
    }

    private static StatementConcat transform(CompositionStatement compositionStatement) {
        Statement first = transform(compositionStatement.getFirstStatement());
        Statement second = transform(compositionStatement.getSecondStatement());
        return new StatementConcat(first, second);
    }

    private static IfThenElse transform(SelectionStatement selectionStatement) {
        List<Condition> guards = selectionStatement.getGuards();
        List<AbstractStatement> commands = selectionStatement.getCommands();

        if (guards.isEmpty()) {
            throw new IllegalArgumentException("SelectionStatement must have at least one guard.");
        }

        IfThenElse result = new IfThenElse(transform(guards.getFirst()), transform(commands.getFirst()), null);

        for (int i = 1; i < guards.size(); i++) {
            IfThenElse newBranch = new IfThenElse(transform(guards.get(i)), transform(commands.get(i)), null);
            result.setElseBranch(newBranch);
            result = newBranch;
        }

        result.setElseBranch(new Skip());
        return result;
    }

    private static Repeat transform(SmallRepetitionStatement repetitionStatement) {
        return new Repeat(
                transform(repetitionStatement.getVariant()),
                new IfThenElse(
                        transform(repetitionStatement.getGuard()),
                        transform(repetitionStatement.getLoopStatement()),
                        new Skip()
                )
        );
    }

    private static Expression transform(Condition condition) {
        return transformFromJava(JAVA_PARSER.parseExpression(condition.getName()).getResult().orElseThrow(
                () -> new IllegalArgumentException("Could not parse condition: " + condition.getName())
        ));
    }

    private static Expression transform(Variant variant) {
        return transformFromJava(JAVA_PARSER.parseExpression(variant.getName()).getResult().orElseThrow(
                () -> new IllegalArgumentException("Could not parse variant: " + variant.getName())
        ));
    }

    private static Statement parse(String statement) {
        return transformFromJava(JAVA_PARSER.parseBlock("{" + statement + "}").getResult().orElseThrow(
                () -> new IllegalArgumentException("Could not parse statement: " + statement
        )));
    }

    private static Expression transformFromJava(com.github.javaparser.ast.expr.Expression expr) {
        return switch (expr) {
            case BinaryExpr e -> new BinOp(
                    transformFromJava(e.getLeft()),
                    transformFromJava(e.getRight()),
                    transformFromJava(e.getOperator())
            );
            case NameExpr e -> new Var(e.getNameAsString());
            case IntegerLiteralExpr e -> new Const(e.asNumber().intValue());
            default -> throw new IllegalArgumentException("Expression type not implemented: " + expr.getClass().getSimpleName());
        };
    }

    private static Statement transformFromJava(BlockStmt block) {
        return block.getStatements().stream()
                .map(Transformer::transformFromJava)
                .reduce(StatementConcat::new)
                .orElse(new Skip());
    }

    private static Statement transformFromJava(com.github.javaparser.ast.stmt.Statement stmt) {
        if (stmt.isExpressionStmt() && stmt.asExpressionStmt().getExpression().isAssignExpr()) {
            // fix difference in classification of statement and expression
            AssignExpr assignExpr = stmt.asExpressionStmt().getExpression().asAssignExpr();
            return new VarAssignment(
                    assignExpr.getTarget().toString(),
                    transformFromJava(assignExpr.getValue())
            );
        }

        throw new IllegalArgumentException("Statement type not implemented: " + stmt.getClass().getSimpleName());
    }

    private static BinOp.Op transformFromJava(BinaryExpr.Operator op) {
        return switch (op) {
            case OR, BINARY_OR -> BinOp.Op.OR;
            case AND, BINARY_AND -> BinOp.Op.AND;
            case XOR -> BinOp.Op.XOR;
            case EQUALS -> BinOp.Op.EQ;
            case NOT_EQUALS -> BinOp.Op.NE;
            case LESS -> BinOp.Op.LT;
            case GREATER -> BinOp.Op.GT;
            case LESS_EQUALS -> BinOp.Op.LE;
            case GREATER_EQUALS -> BinOp.Op.GE;
            case LEFT_SHIFT -> BinOp.Op.SHL;
            case SIGNED_RIGHT_SHIFT -> BinOp.Op.SHR;
            case UNSIGNED_RIGHT_SHIFT -> BinOp.Op.USHR;
            case PLUS -> BinOp.Op.ADD;
            case MINUS -> BinOp.Op.MIN;
            case MULTIPLY -> BinOp.Op.MUL;
            case DIVIDE -> BinOp.Op.DIV;
            case REMAINDER -> BinOp.Op.MOD;
        };
    }
}
