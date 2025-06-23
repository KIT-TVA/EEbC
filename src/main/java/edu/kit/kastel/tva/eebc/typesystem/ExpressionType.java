package edu.kit.kastel.tva.eebc.typesystem;

public interface ExpressionType extends StatementType {
    int v(State before);
}
