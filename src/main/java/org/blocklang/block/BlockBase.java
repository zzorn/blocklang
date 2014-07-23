package org.blocklang.block;

import org.blocklang.block.parameter.Input;
import org.blocklang.block.parameter.Internal;
import org.blocklang.block.parameter.Output;
import org.blocklang.block.parameter.Param;
import org.blocklang.compiler.ModuleCalculator;
import org.blocklang.compiler.CalculationListener;
import org.flowutils.Check;
import org.flowutils.Symbol;
import org.flowutils.classbuilder.SourceLocation;
import org.flowutils.collections.props.PropsBase;
import org.flowutils.collections.props.ReadableProps;
import org.flowutils.collections.props.WritableProps;

import java.util.*;

/**
 * Common functionality for blocks.
 */
public abstract class BlockBase implements Block {

    // We do not initialize these collections here, as derived blocks may initialize parameters
    // in their field declarations, and those might be called before the initialization of these collections could be done.
    // So instead they are lazily initialized.
    private Map<Symbol, Input> inputs;
    private Map<Symbol, Output> outputs;
    private Map<Symbol, Internal> internals;

    private Output defaultOutput;
    private final Set<BlockListener> listeners = new LinkedHashSet<BlockListener>(3);


    /**
     */
    protected BlockBase() {
        // Make sure the parameter collections are initialized
        getMutableInputs();
        getMutableInternals();
        getMutableOutputs();
    }


    @Override public final void generateCode(BlockBuilder blockBuilder) {

        // Set current block for the builder, used to resolve parameter references
        blockBuilder.setCurrentBlock(this);

        // Add parameter fields
        for (Input input : inputs.values()) {
            blockBuilder.addParamField(input);
        }
        for (Output output : outputs.values()) {
            blockBuilder.addParamField(output);
        }
        for (Internal internal : internals.values()) {
            blockBuilder.addParamField(internal);
        }

        // Create block-specific code
        doGenerateCode(blockBuilder);

        // Clear current block
        blockBuilder.setCurrentBlock(null);

    }

    @Override public final void disconnect() {
        for (Input input : inputs.values()) {
            input.disconnect();
        }

        for (Output output : outputs.values()) {
            output.disconnect();
        }
    }

    /**
     * Generate block specific code.
     * @param blockBuilder builder to add generated code lines to.
     */
    protected abstract void doGenerateCode(BlockBuilder blockBuilder);



    @Override public final Map<Symbol, Input> getInputs() {
        return Collections.unmodifiableMap(getMutableInputs());
    }

    @Override public Map<Symbol, Internal> getInternalParams() {
        return Collections.unmodifiableMap(internals);
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

    @Override public Param getParameter(Symbol name) {
        Param param = inputs.get(name);
        if (param == null) param = outputs.get(name);
        if (param == null) param = internals.get(name);

        return param;
    }

    /**
     * Calls all registered block listeners and notifies them that this block has changed.
     */
    protected final void notifyListenersBlockChanged() {
        for (BlockListener listener : listeners) {
            listener.onBlockChanged(this);
        }
    }

    /**
     * Creates and registers a new numerical input parameter with initial and default value zero.
     *
     * @param name user readable name of the input.
     * @return the created and registered input parameter.
     */
    protected final Input input(String name) {
        return input(name, 0.0);
    }

    /**
     * Creates and registers a new numerical input parameter with a default value.
     *
     * @param name user readable name of the input.
     * @param defaultValue default and initial value for the input.
     * @return the created and registered input parameter.
     */
    protected final Input input(String name, double defaultValue) {
        return input(name, defaultValue, null);
    }

    /**
     * Creates and registers a new numerical input parameter.
     *
     * @param name user readable name of the input.
     * @param defaultValue default and initial value for the input.
     * @param description user readable description of the input.
     * @return the created and registered input parameter.
     */
    protected final Input input(String name, double defaultValue, String description) {
        return addInput(new Input(Symbol.get(name), Double.class, defaultValue, description));
    }

    /**
     * Creates and registers a new input parameter.
     *
     * @param name user readable name of the input.
     * @return the created and registered input parameter.
     */
    protected final Input input(String name, Class type) {
        return input(name, type, null);
    }

    /**
     * Creates and registers a new input parameter.
     *
     * @param name user readable name of the input.
     * @param defaultValue default and initial value for the input.
     * @return the created and registered input parameter.
     */
    protected final Input input(String name, Class type, Object defaultValue) {
        return input(name, type, defaultValue, null);
    }

    /**
     * Creates and registers a new input parameter.
     *
     * @param name user readable name of the input.
     * @param defaultValue default and initial value for the input.
     * @param description user readable description of the input.
     * @return the created and registered input parameter.
     */
    protected final Input input(String name, Class type, Object defaultValue, String description) {
        return addInput(new Input(Symbol.get(name), type, defaultValue, description));
    }

    /**
     * Creates and registers a new numerical internal state parameter with initial and default value zero.
     *
     * @param name user readable name of the internal state parameter.
     * @return the created and registered internal state parameter.
     */
    protected final Internal internal(String name) {
        return internal(name, 0.0);
    }

    /**
     * Creates and registers a new numerical internal state parameter.
     *
     * @param name user readable name of the internal state parameter.
     * @param defaultValue default and initial value for the internal state parameter.
     * @return the created and registered internal state parameter.
     */
    protected final Internal internal(String name, double defaultValue) {
        return internal(name, defaultValue, null);
    }

    /**
     * Creates and registers a new numerical internal state parameter.
     *
     * @param name user readable name of the internal state parameter.
     * @param defaultValue default and initial value for the internal state parameter.
     * @param description user readable description of the internal state parameter.
     * @return the created and registered internal state parameter.
     */
    protected final Internal internal(String name, double defaultValue, String description) {
        return addInternal(new Internal(Symbol.get(name), Double.class, defaultValue, description));
    }

    /**
     * Creates and registers a new internal state parameter.
     *
     * @param name user readable name of the internal state parameter.
     * @return the created and registered internal state parameter.
     */
    protected final Internal internal(String name, Class type) {
        return internal(name, type, null);
    }

    /**
     * Creates and registers a new internal state parameter.
     *
     * @param name user readable name of the internal state parameter.
     * @param defaultValue default and initial value for the internal state parameter.
     * @return the created and registered internal state parameter.
     */
    protected final Internal internal(String name, Class type, Object defaultValue) {
        return internal(name, type, defaultValue, null);
    }

    /**
     * Creates and registers a new internal state parameter.
     *
     * @param name user readable name of the internal state parameter.
     * @param defaultValue default and initial value for the internal state parameter.
     * @param description user readable description of the internal state parameter.
     * @return the created and registered internal state parameter.
     */
    protected final Internal internal(String name, Class type, Object defaultValue, String description) {
        return addInternal(new Internal(Symbol.get(name), type, defaultValue, description));
    }

    /**
     * Creates and registers a new numerical output parameter.
     *
     * @param name user readable name of the output.
     * @return the created and registered output parameter.
     */
    protected final Output output(String name) {
        return output(name, (String) null);
    }

    /**
     * Creates and registers a new numerical output parameter.
     *
     * @param name user readable name of the output.
     * @param description user readable description of the output.
     * @return the created and registered output parameter.
     */
    protected final Output output(String name, String description) {
        return addOutput(new Output(Symbol.get(name), Double.class, 0.0, description));
    }

    /**
     * Creates and registers a new output parameter.
     *
     * @param name user readable name of the output.
     * @return the created and registered output parameter.
     */
    protected final Output output(String name, Class type) {
        return output(name, type, null);
    }

    /**
     * Creates and registers a new output parameter.
     *
     * @param name user readable name of the output.
     * @param description user readable description of the output.
     * @return the created and registered output parameter.
     */
    protected final Output output(String name, Class type, String description) {
        return addOutput(new Output(Symbol.get(name), type, null, description));
    }

    // TODO: Throw exception if the same identifier already exists as input, internal, or external.

    protected final Input addInput(Input input) {
        addParam(input, getMutableInputs(), "input");
        return input;
    }

    protected final Internal addInternal(Internal internal) {
        addParam(internal, getMutableInternals(), "internal");
        return internal;
    }

    protected final Output addOutput(Output output) {
        addParam(output, getMutableOutputs(), "output");

        // Initialize defaultOutput if this is the first output
        if (defaultOutput == null) defaultOutput = output;

        return output;
    }

    private <T extends Param> void addParam(T param, Map<Symbol, T> params, String paramCategoryName) {
        final Symbol name = param.getName();

        if (params.containsKey(name)) throw new IllegalArgumentException("An "+paramCategoryName+" named " + name + " has already been added to this block!");

        params.put(name, param);
    }


    protected Map<Symbol, Input> getMutableInputs() {
        if (inputs == null) inputs = new LinkedHashMap<Symbol, Input>();
        return inputs;
    }

    protected Map<Symbol, Internal> getMutableInternals() {
        if (internals == null) internals = new LinkedHashMap<Symbol, Internal>();
        return internals;
    }

    protected Map<Symbol, Output> getMutableOutputs() {
        if (outputs == null) outputs = new LinkedHashMap<Symbol, Output>();
        return outputs;
    }


}
