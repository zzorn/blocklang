package org.blocklang.block;


import org.blocklang.block.parameter.Input;
import org.blocklang.block.parameter.Internal;
import org.blocklang.block.parameter.Output;
import org.blocklang.block.parameter.Param;
import org.flowutils.Symbol;
import org.flowutils.classbuilder.ClassBuilder;
import org.flowutils.collections.props.ReadableProps;
import org.flowutils.collections.props.WritableProps;

import java.util.Map;

/**
 * A block that does some calculation,
 * has input properties that can be connected to outputs of other blocks,
 * and output properties that the calculated results are visible on.
 */
// TODO: Implement composite/container/module block
// TODO: Way to hold internal state -- keep it in the BlockCalculation, so that there is no need to get&set all fields between each simulation cycle (instead only when they need to be serialized / deserialized)
public interface Block {

    /**
     * @return read only map with the input properties of this block.
     */
    Map<Symbol, Input> getInputs();

    /**
     * @return read only map with the output properties available from this block.
     */
    Map<Symbol, Output> getOutputs();

    /**
     * @return output to use by default when connecting blocks together, if no other output is selected.
     *         The first output added to a block becomes the default output.
     */
    Output getDefaultOutput();

    /**
     * @return read only map with information about the internal properties in this block.
     */
    Map<Symbol, Internal> getInternalParams();

    /**
     * Retrieve the complete internal state of this block and any contained blocks, and store it in the specified WritableProps.
     * Can be used for serializing the state of a block or composite block.
     */
    // NOTE: Breaks if any internal property added or removed
    void getInternalState(WritableProps internalState);

    /**
     * Set the complete internal state of this block and any contained blocks, from the specified ReadableProps.
     * Can be used for serializing the state of a block or composite block.
     */
    void setInternalState(ReadableProps internalState);

    /**
     * Resets the internal state parameters to their default values.
     */
    void resetInternalState();

    /**
     * Calculates the outputs of the block, using the blocks inputs.
     *
     * Compiles a program for this node if not already compiled, then executes it
     */
    void calculateOutputs();

    /**
     * Calculates the outputs of the block, using the blocks inputs and the specified surrounding context.
     *
     * Compiles a program for this node if not already compiled, then executes it
     *
     * @param externalContext context with current context variables (e.g. time, coordinates, etc).
     */
    void calculateOutputs(ReadableProps externalContext);

    /**
     * Generates code that does the calculations of this block, can be embedded in a program using this block, or compiled stand alone by this block.
     *
     * @param blockBuilder add generated source to this builder.
     */
    void generateCode(BlockBuilder blockBuilder);

    /**
     * @param listener a listener that is notified about changes to the block.
     */
    void addListener(BlockListener listener);

    /**
     * @param listener listener to remove.
     */
    void removeListener(BlockListener listener);

    /**
     * @return the input, output or internal parameter with the specified name, or null if none found.
     */
    Param getParameter(Symbol name);

}
