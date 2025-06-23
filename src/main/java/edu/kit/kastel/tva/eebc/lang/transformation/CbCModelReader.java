package edu.kit.kastel.tva.eebc.lang.transformation;

import de.tu_bs.cs.isf.cbc.cbcmodel.CbCFormula;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.file.Files;

public class CbCModelReader {
    private static final JAXBContext CONTEXT;

    static {
        try {
            CONTEXT = JAXBContext.newInstance(CbCFormula.class);
        } catch (JAXBException e) {
            throw new IllegalStateException("JAXB could not load context for class created automatically from XSD.", e);
        }
    }

    public static CbCFormula readModelInputStream(InputStream stream) throws JAXBException, IOException {
        return readModelString(new String(stream.readAllBytes()));
    }

    public static CbCFormula readModelFile(File file) throws JAXBException, IOException {
        return readModelString(Files.readString(file.toPath()));
    }

    public static CbCFormula readModelString(String string) throws JAXBException {
        Unmarshaller unmarshaller = CONTEXT.createUnmarshaller();
        String formulaString = extractFormulaFromXmi(string);
        Source source = new StreamSource(new StringReader(formulaString));
        return unmarshaller.unmarshal(source, CbCFormula.class).getValue();
    }

    private static String extractFormulaFromXmi(String xmi) {
        int start = xmi.indexOf("<cbcmodel:CbCFormula");
        int end = xmi.lastIndexOf("</cbcmodel:CbCFormula>") + "</cbcmodel:CbCFormula>".length();

        if (start == -1 || end == -1) {
            throw new IllegalArgumentException("Invalid XMI format: CbCFormula not found.");
        }

        String substr = xmi.substring(start, end);
        substr = "<?xml version=\"1.0\" encoding=\"ASCII\"?>\n" + substr;
        substr = substr.replaceFirst("<cbcmodel:CbCFormula", "<cbcmodel:CbCFormula xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:cbcmodel=\"http://cbc.isf.cs.tu-bs.de/cbcmodel\"");
        return substr;
    }
}
