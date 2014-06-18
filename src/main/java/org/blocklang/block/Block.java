package org.blocklang.block;


import org.blocklang.block.parameter.Input;
import org.blocklang.block.parameter.Output;
import org.blocklang.compiler.SourceBuilder;
import org.flowutils.Symbol;
import org.flowutils.collections.props.ReadableProps;

import java.util.Map;

/**
 * A block that does some calculation,
 * has input properties that can be connected to outputs of other blocks,
 * and output properties that the calculated results are visible on.
 */
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
     * Calculates the outputs of the block, using the blocks inputs and the specified surrounding context.
     *
     * @param context context with current context variables (e.g. time, coordinates, etc).
     */
    // Compiles a program for this node if not already compiled, then executes it
    void calculateOutputs(ReadableProps context);

    /**
     * Generates code that does the calculations of this block, can be embedded in a program using this block, or compiled stand alone by this block.
     */
    void generateCode(SourceBuilder sourceBuilder);

    /**
     * @param listener a listener that is notified about changes to the block.
     */
    void addListener(BlockListener listener);

    /**
     * @param listener listener to remove.
     */
    void removeListener(BlockListener listener);

}
