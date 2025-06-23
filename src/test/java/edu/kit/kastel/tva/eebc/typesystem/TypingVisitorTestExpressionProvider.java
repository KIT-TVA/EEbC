package edu.kit.kastel.tva.eebc.typesystem;

import edu.kit.kastel.tva.eebc.lang.ast.BinOp;
import edu.kit.kastel.tva.eebc.lang.ast.DemoPrograms;
import edu.kit.kastel.tva.eebc.lang.ast.Expression;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.function.Consumer;
import java.util.stream.Stream;

public class TypingVisitorTestExpressionProvider implements ArgumentsProvider {
    private static final TestHardwareModel HARDWARE_MODEL = new TestHardwareModel();

    static {
        HARDWARE_MODEL.setTimeConst(1);
        HARDWARE_MODEL.setTimeVarAssignment(10);
        HARDWARE_MODEL.setTimeVar(100);
        HARDWARE_MODEL.setTimeIf(1000);
        HARDWARE_MODEL.putTimeBinOp(BinOp.Op.ADD, 10000);
    }

    @Override
    public Stream<Arguments> provideArguments(ExtensionContext context) {
        return DemoPrograms.expressions()
                .mapMulti(TypingVisitorTestExpressionProvider::generateStates)
                .map(TypingVisitorTestExpressionProvider::toArguments);
    }

    private record TestCase(
            String name,
            State before,
            TestHardwareModel hardwareModel,
            Expression expression,
            ExpressionType expectedType) {
    }

    private static void generateStates(DemoPrograms.ExpressionCase expression,
                                       Consumer<TestCase> consumer) {
        consumer.accept(new TestCase(expression.name(), State.empty(), HARDWARE_MODEL,
                expression.expression(), expression.type().asExpressionType(HARDWARE_MODEL)));
        consumer.accept(new TestCase(expression.name(), State.random(), HARDWARE_MODEL,
                expression.expression(), expression.type().asExpressionType(HARDWARE_MODEL)));
        consumer.accept(new TestCase(expression.name(), State.random(), HARDWARE_MODEL,
                expression.expression(), expression.type().asExpressionType(HARDWARE_MODEL)));
        consumer.accept(new TestCase(expression.name(), State.random(), HARDWARE_MODEL,
                expression.expression(), expression.type().asExpressionType(HARDWARE_MODEL)));
        consumer.accept(new TestCase(expression.name(), State.random(), HARDWARE_MODEL,
                expression.expression(), expression.type().asExpressionType(HARDWARE_MODEL)));
    }

    private static Arguments toArguments(TestCase testCase) {
        return Arguments.argumentSet(
                testCase.name,
                testCase.before,
                testCase.hardwareModel,
                testCase.expression(),
                testCase.expectedType().v(testCase.before),
                testCase.expectedType().s(testCase.before),
                testCase.expectedType().e(testCase.before)
        );
    }
}
