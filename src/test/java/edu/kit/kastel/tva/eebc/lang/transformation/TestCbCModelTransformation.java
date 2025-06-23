package edu.kit.kastel.tva.eebc.lang.transformation;

import de.tu_bs.cs.isf.cbc.cbcmodel.CbCFormula;
import edu.kit.kastel.tva.eebc.lang.ast.Statement;
import jakarta.xml.bind.JAXBException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class TestCbCModelTransformation {
    @Test
    public void testReadExponentiation() {
        CbCFormula cbCFormula;
        try {
            cbCFormula = CbCModelReader.readModelInputStream(
                    TestCbCModelTransformation.class.getResourceAsStream("Exponentiation.cbcmodel")
            );
        } catch (JAXBException | IOException e) {
            Assertions.fail("Failed to read CbC model", e);
            return;
        }
        Assertions.assertNotNull(cbCFormula, "CbCFormula should not be null");

        Statement statement = Transformer.transform(cbCFormula);
        Assertions.assertNotNull(statement, "Transformed statement should not be null");

        // TODO Implement semantics check of transformed statement.
    }
}
