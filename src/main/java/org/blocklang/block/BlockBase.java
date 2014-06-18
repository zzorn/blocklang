package org.blocklang.block;

import org.blocklang.block.parameter.Input;
import org.blocklang.block.parameter.NumType;
import org.blocklang.block.parameter.Output;
import org.blocklang.block.parameter.ParamType;
import org.blocklang.compiler.BlockCalculation;
import org.flowutils.Check;
import org.flowutils.Symbol;
import org.flowutils.collections.props.Props;
import org.flowutils.collections.props.PropsBase;
import org.flowutils.collections.props.ReadableProps;

import java.util.*;

/**
 * Common functionality for blocks.
 */
public abstract class BlockBase implements Block {

    private Map<Symbol, Input> inputs;
    private Map<Symbol, Output> outputs;
    private Output defaultOutput;
    private final Set<BlockListener> listeners = new LinkedHashSet<BlockListener>(3);

    private BlockCalculation blockCalculation;

    private final Props parameterDelegate = new PropsBase() {
        @Override public <T> T get(Symbol id) {
            return inputs.get(id).get();
        }

        @Override public boolean has(Symbol id) {
            return inputs.containsKey(id);
        }

        @Override public Map<Symbol, Object> getAll(Map<Symbol, Object> out) {
            if (out == null) out = new LinkedHashMap<Symbol, Object>();

            for (Map.Entry<Symbol, Input> entry : inputs.entrySet()) {
                out.put(entry.getKey(), entry.getValue().get());
            }

            return out;
        }

        @Override public Object set(Symbol id, Object value) {
            final Output output = outputs.get(id);
            final Object oldValue = output.get();
            output.set(value);
            return oldValue;
        }

        @Override public <T> T remove(Symbol id) {
            throw new IllegalStateException("Can not remove any parameter!");
        }

        @Override public void removeAll() {
            throw new IllegalStateException("Can not remove any parameter!");
        }
    };

    @Override public final Map<Symbol, Input> getInputs() {
        return Collections.unmodifiableMap(getMutableInputs());
    }

    @Override public final Map<Symbol, Output> getOutputs() {
        return Collections.unmodifiableMap(getMutableOutputs());
    }

    @Override public final Output getDefaultOutput() {
        if (outputs == null || outputs.isEmpty()) return null;
        else return defaultOutput;
    }

    @Override public final void addListener(BlockListener listener) {
        Check.notNull(listener, "listener");

        listeners.add(listener);
    }

    @Override public final void removeListener(BlockListener listener) {
        listeners.remove(listener);
    }

    @Override public final void calculateOutputs(ReadableProps context) {
        if (blockCalculation == null) {
            blockCalculation = compileCode();
        }

        blockCalculation.calculate(parameterDelegate, parameterDelegate, null);
    }

    /*
    @Override public void generateCode(SourceBuilder sourceBuilder) {

        // TODO: Implement
    }
    */

    protected final BlockCalculation compileCode() {

        // Generate code

        // Compile

        // TODO: Implement
        return null;
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
        return addInput(new Input(Symbol.get(name), type, description, defaultValue));
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
        return addInput(new Input(Symbol.get(name), NumType.INSTANCE, description, defaultValue));
    }

    private Input addInput(Input input) {
        getMutableInputs().put(input.getIdentifier(), input);
        return input;
    }

    private Map<Symbol, Input> getMutableInputs() {
        if (inputs == null) inputs = new LinkedHashMap<Symbol, Input>();
        return inputs;
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
        return addOutput(new Output(Symbol.get(name), NumType.INSTANCE, description));
    }

    /**
     * Creates and registers a new output parameter.
     *
     * @param name user readable name of the output.
     * @param description user readable description of the output.
     * @return the created and registered output parameter.
     */
    protected final Output output(String name, ParamType type, String description) {
        return addOutput(new Output(Symbol.get(name), type, description));
    }


    private Output addOutput(Output output) {
        getMutableOutputs().put(output.getIdentifier(), output);

        // Initialize defaultOutput if this is the first output
        if (defaultOutput == null) defaultOutput = output;

        return output;
    }

    private Map<Symbol, Output> getMutableOutputs() {
        if (outputs == null) outputs = new LinkedHashMap<Symbol, Output>();
        return outputs;
    }

    /**
     * Calls all registered block listeners and notifies them that this block has changed.
     */
    protected final void notifyListenersBlockChanged() {
        for (BlockListener listener : listeners) {
            listener.onBlockChanged(this);
        }
    }


}
