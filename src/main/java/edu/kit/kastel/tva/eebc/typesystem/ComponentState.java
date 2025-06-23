package edu.kit.kastel.tva.eebc.typesystem;

/**
 * Represents a state of a hardware component in a system.
 */
public interface ComponentState {
    /**
     * Creates an empty component state.
     *
     * <p>
     *     This method returns a default implementation of the ComponentState interface
     *     that can be used for test purposes when hardware components are not used.
     * </p>
     *
     * @return an instance of ComponentState that represents an empty state
     */
    static ComponentState empty() {
        return new ComponentState() {
            @Override
            public ComponentState copy() {
                return this;
            }

            @Override
            public int fun(String name, int arg) {
                return 0; // Default implementation returns 0 for any function call
            }

            @Override
            public ComponentState succ(String name, int arg) {
                return this; // Default implementation returns the same state
            }

            @Override
            public int ec(String name, int arg) {
                return 0; // Default implementation returns 0 energy consumption
            }
        };
    }

    /**
     * Creates a copy of the current component state.
     *
     * @return a new instance of ComponentState with the same state as the current one
     */
    ComponentState copy();

    /**
     * Returns the result of the component function with the given name and argument.
     *
     * @param name the name of the component function
     * @param arg the argument to the component function
     * @return the result of the component function
     */
    int fun(String name, int arg);

    /**
     * Returns the successor component state after applying the component function with the given name and argument.
     *
     * @param name the name of the component function
     * @param arg the argument to the component function
     * @return the successor component state
     */
    ComponentState succ(String name, int arg);

    /**
     * Returns the energy consumption of the component function with the given name and argument.
     *
     * @param name the name of the component function
     * @param arg the argument to the component function
     * @return the energy consumption of the component function
     */
    int ec(String name, int arg);
}
