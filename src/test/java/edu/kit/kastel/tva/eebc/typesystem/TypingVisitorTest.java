package edu.kit.kastel.tva.eebc.typesystem;

import edu.kit.kastel.tva.eebc.lang.ast.Expression;
import edu.kit.kastel.tva.eebc.lang.ast.Statement;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

public class TypingVisitorTest {
    @ParameterizedTest
    @ArgumentsSource(TypingVisitorTestExpressionProvider.class)
    public void testTypingVisitorExpression(State before, TestHardwareModel hardwareModel, Expression toType,
                                            int expValue, State expAfter, int expEnergy) {
        ExpressionType type = toType.accept(new TypingVisitor(hardwareModel));

        int value = type.v(before);
        Assertions.assertEquals(expValue, value,
                "expected value to be " + expValue + " but was " + value);

        testGeneralized(before, type, expAfter, expEnergy);
    }

    @ParameterizedTest
    @ArgumentsSource(TypingVisitorTestStatementProvider.class)
    public void testTypingVisitorStatement(State before, TestHardwareModel hardwareModel, Statement toType,
                                           State expAfter, int expEnergy) {
        StatementType type = toType.accept(new TypingVisitor(hardwareModel));
        testGeneralized(before, type, expAfter, expEnergy);
    }

    private void testGeneralized(State before, StatementType type,
                                 State expAfter, int expEnergy) {
        State afterByS = type.s(before);
        Assertions.assertEquals(expAfter, afterByS,
                "expected state after applying s to be " + expAfter + " but was " + afterByS);

        int energy = type.e(before);
        Assertions.assertEquals(expEnergy, energy,
                "expected energy to be " + expEnergy + " but was " + energy);

        Assertions.assertEquals(StatementType.TypingState.COMPLETE, type.typingState(before),
                "expected typing state to be COMPLETE but was " + type.typingState(before));
    }
}
