package org.blocklang.nodes;


import org.blocklang.compiler.SourceBuilder;
import org.flowutils.Symbol;
import org.flowutils.collections.props.ReadableProps;

import java.util.List;
import java.util.Map;

/**
 *
 */
// TODO: Rename to block?
public interface Node {

    Map<Symbol, Input> getInputs();

    Map<Symbol, Output> getOutputs();

    Output getDefaultOutput();

    /**
     * Calculates the outputs of the node, using the nodes inputs and the specified surrounding context.
     *
     * @param context context with current context variables (e.g. time, coordinates, etc).
     */
    // Compiles a program for this node if not already compiled, then executes it
    void calculateOutputs(ReadableProps context);

    /**
     * Generates code that does the calculations of this node, can be embedded in a program using this node, or compiled stand alone by this node.
     */
    void generateCode(SourceBuilder sourceBuilder);

    void addListener(NodeListener listener);

    void removeListener(NodeListener listener);

}
