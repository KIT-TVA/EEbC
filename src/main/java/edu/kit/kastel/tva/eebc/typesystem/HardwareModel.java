package edu.kit.kastel.tva.eebc.typesystem;

import edu.kit.kastel.tva.eebc.lang.ast.BinOp;

/**
 * Interface representing the hardware model of the system.
 */
public interface HardwareModel {
    /**
     * Returns the energy consumption of the system if it stays in its current state for the given time.
     *
     * @param state the current state of the system
     * @param time the time the system stays in its current state
     * @return the energy consumption of the system for the given time
     */
    int timeDependentEnergyConsumption(ComponentState state, int time);

    /**
     * Returns the time it takes to evaluate a constant.
     *
     * @return the time it takes to evaluate a constant
     */
    int timeConst();

    /**
     * Returns the time it takes to evaluate a binary operation.
     *
     * @param op the binary operation
     * @return the time it takes to evaluate the binary operation
     */
    int timeBinOp(BinOp.Op op);

    /**
     * Returns the time it takes to evaluate an if statement.
     *
     * @return the time it takes to evaluate an if statement
     */
    int timeIf();

    /**
     * Returns the time it takes to assign a variable.
     *
     * @return the time it takes to assign a variable
     */
    int timeVarAssignment();

    /**
     * Returns the time it takes to evaluate a variable.
     *
     * @return the time it takes to evaluate a variable
     */
    int timeVar();

    /**
     * Returns the time it takes to call a component function.
     *
     * @param name the name of the component function
     * @return the time it takes to call the component function
     */
    int timeCallCompFun(String name);

    /**
     * Returns if the energy consumption of the component function with the given name and argument is deterministic.
     *
     * <p>
     *     This means that the energy consumption does not depend on the state of the system or any other
     *     factors, and will always return the same value for the same input.
     * </p>
     *
     * @param funName the name of the component function
     * @return true if the energy consumption is deterministic, false otherwise
     */
    boolean isDeterministic(String funName);
}
