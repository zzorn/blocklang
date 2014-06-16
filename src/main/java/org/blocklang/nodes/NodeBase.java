package org.blocklang.nodes;

import org.flowutils.Check;
import org.flowutils.Symbol;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Common functionality for nodes.
 */
public abstract class NodeBase implements Node {

    private List<Input> inputs;
    private List<Output> outputs;
    private final Set<NodeListener> listeners = new LinkedHashSet<NodeListener>(3);

    @Override public final List<Input> getInputs() {
        if (inputs == null) inputs = new ArrayList<Input>();

        return inputs;
    }

    @Override public final List<Output> getOutputs() {
        if (outputs == null) outputs = new ArrayList<Output>();

        return outputs;
    }

    @Override public final Output getDefaultOutput() {
        if (outputs == null || outputs.isEmpty()) return null;
        else return outputs.get(0);
    }

    @Override public final void addListener(NodeListener listener) {
        Check.notNull(listener, "listener");

        listeners.add(listener);
    }

    @Override public final void removeListener(NodeListener listener) {
        listeners.remove(listener);
    }

    /**
     * Creates and registers a new numerical input parameter.
     *
     * @param name user readable name of the input.
     * @return the created and registered input parameter.
     */
    protected final Input input(String name) {
        return input(name, NumType.INSTANCE);
    }

    /**
     * Creates and registers a new input parameter.
     *
     * @param name user readable name of the input.
     * @return the created and registered input parameter.
     */
    protected final Input input(String name, ParamType type) {
        return input(name, type, null);
    }

    /**
     * Creates and registers a new input parameter.
     *
     * @param name user readable name of the input.
     * @param defaultValue default and initial value for the input.
     * @return the created and registered input parameter.
     */
    protected final Input input(String name, ParamType type, Object defaultValue) {
        return input(name, type, null, defaultValue);
    }

    /**
     * Creates and registers a new numerical input parameter with a default value.
     *
     * @param name user readable name of the input.
     * @param defaultValue default and initial value for the input.
     * @return the created and registered input parameter.
     */
    protected final Input input(String name, double defaultValue) {
        return input(name, null, defaultValue);
    }

    /**
     * Creates and registers a new input parameter.
     *
     * @param name user readable name of the input.
     * @param description user readable description of the input.
     * @param defaultValue default and initial value for the input.
     * @return the created and registered input parameter.
     */
    protected final Input input(String name, ParamType type, String description, Object defaultValue) {

        final Input input = new Input(Symbol.get(name), type, description, defaultValue);

        getInputs().add(input);

        return input;
    }

    /**
     * Creates and registers a new numerical input parameter.
     *
     * @param name user readable name of the input.
     * @param description user readable description of the input.
     * @param defaultValue default and initial value for the input.
     * @return the created and registered input parameter.
     */
    protected final Input input(String name, String description, double defaultValue) {

        final Input input = new Input(Symbol.get(name), NumType.INSTANCE, description, defaultValue);

        getInputs().add(input);

        return input;
    }

    /**
     * Creates and registers a new numerical output parameter.
     *
     * @param name user readable name of the output.
     * @return the created and registered output parameter.
     */
    protected final Output output(String name) {
        return output(name, NumType.INSTANCE, null);
    }

    /**
     * Creates and registers a new output parameter.
     *
     * @param name user readable name of the output.
     * @return the created and registered output parameter.
     */
    protected final Output output(String name, ParamType type) {
        return output(name, type, null);
    }

    /**
     * Creates and registers a new numerical output parameter.
     *
     * @param name user readable name of the output.
     * @param description user readable description of the output.
     * @return the created and registered output parameter.
     */
    protected final Output output(String name, String description) {

        final Output output = new Output(Symbol.get(name), NumType.INSTANCE, description);

        getOutputs().add(output);

        return output;
    }

    /**
     * Creates and registers a new output parameter.
     *
     * @param name user readable name of the output.
     * @param description user readable description of the output.
     * @return the created and registered output parameter.
     */
    protected final Output output(String name, ParamType type, String description) {

        final Output output = new Output(Symbol.get(name), type, description);

        getOutputs().add(output);

        return output;
    }


    /**
     * Calls all registered node listeners and notifies them that this node has changed.
     */
    protected final void notifyListenersNodeChanged() {
        for (NodeListener listener : listeners) {
            listener.onNodeChanged(this);
        }
    }

}
