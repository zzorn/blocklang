package org.blocklang.nodes;


import java.util.List;

/**
 *
 */
public interface Node {

    List<Input> getInputs();

    List<Output> getOutputs();

    Output getDefaultOutput();

    /**
     * Calculates the outputs of the node, using the nodes inputs and the specified surrounding context.
     *
     * @param context context with current context variables (e.g. time, coordinates, etc).
     */
    void calculateOutputs(Context context);

    void addListener(NodeListener listener);

    void removeListener(NodeListener listener);

}
