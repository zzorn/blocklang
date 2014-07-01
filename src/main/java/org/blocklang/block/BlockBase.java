package org.blocklang.block;

import org.blocklang.block.parameter.Input;
import org.blocklang.block.parameter.Internal;
import org.blocklang.block.parameter.Output;
import org.blocklang.block.parameter.Param;
import org.blocklang.compiler.BlockCalculation;
import org.blocklang.compiler.CalculationListener;
import org.flowutils.Check;
import org.flowutils.Symbol;
import org.flowutils.classbuilder.ClassBuilder;
import org.flowutils.classbuilder.ClassBuilderException;
import org.flowutils.classbuilder.JaninoClassBuilder;
import org.flowutils.collections.props.PropsBase;
import org.flowutils.collections.props.ReadableProps;

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

    private BlockCalculation blockCalculation;

    private PropertiesDelegate<Input> inputPropertiesDelegate;
    private PropertiesDelegate<Internal> internalPropertiesDelegate;
    private PropertiesDelegate<Output> outputPropertiesDelegate;

    /**
     */
    protected BlockBase() {
        // Make sure the parameter collections are initialized
        getMutableInputs();
        getMutableInternals();
        getMutableOutputs();
    }

    /*
    @Override public void generateCode(SourceBuilder sourceBuilder) {

        // TODO: Implement
    }
    */
    // TODO: Generate overall code skeleton (extract or get inputs, create fields for internal, create variables and return code for outputs), have a protected abstract method that derived classes need to implement which generates
    // the actual calculation code.
    // TODO: Add functions to Param or BlockBase for getting the generated code name of a given input/output.

    protected final BlockCalculation compileCode() {

        final ClassBuilder<BlockCalculation> classBuilder = new JaninoClassBuilder<BlockCalculation>(
                BlockCalculation.class,
                "calculate",
                "externalContext",
                "inputParameters",
                "internalParameters",
                "outputParameters",
                "calculationListener");

        // Generate code
        generateCode(classBuilder);

        // Compile and create instance
        try {
            return classBuilder.createInstance();
        } catch (ClassBuilderException e) {
            throw new IllegalStateException("Unexpected problem compiling a block: " + e.getMessage(), e);
        }
    }

    @Override public final void calculateOutputs(ReadableProps externalContext) {
        // Compile the calculation if not already compiled
        if (blockCalculation == null) {
            blockCalculation = compileCode();
        }

        // Initialize the delegates if not already initialized.
        if (inputPropertiesDelegate    == null) inputPropertiesDelegate    = new PropertiesDelegate<Input>(getMutableInputs());
        if (internalPropertiesDelegate == null) internalPropertiesDelegate = new PropertiesDelegate<Internal>(getMutableInternals());
        if (outputPropertiesDelegate   == null) outputPropertiesDelegate   = new PropertiesDelegate<Output>(getMutableOutputs());

        // Invoke calculation, delegating inputs, outputs, and internal state to parameters in this block.
        blockCalculation.calculate(externalContext,
                                   inputPropertiesDelegate,
                                   internalPropertiesDelegate,
                                   outputPropertiesDelegate,
                                   (CalculationListener) null);
    }

    @Override public final Map<Symbol, Input> getInputs() {
        return Collections.unmodifiableMap(getMutableInputs());
    }

    @Override public Map<Symbol, Internal> getInternalState() {
        return Collections.unmodifiableMap(internals);
    }

    @Override public final Map<Symbol, Output> getOutputs() {
        return Collections.unmodifiableMap(getMutableOutputs());
    }

    @Override public final Output getDefaultOutput() {
        if (outputs == null || outputs.isEmpty()) return null;
        else return defaultOutput;
    }

    @Override public final void resetInternalState() {
        if (internals != null) {
            for (Internal internal : internals.values()) {
                internal.resetToDefault();
            }
        }
    }

    @Override public final void addListener(BlockListener listener) {
        Check.notNull(listener, "listener");

        listeners.add(listener);
    }

    @Override public final void removeListener(BlockListener listener) {
        listeners.remove(listener);
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

    private Input addInput(Input input) {
        getMutableInputs().put(input.getIdentifier(), input);
        return input;
    }

    private Internal addInternal(Internal internal) {
        getMutableInternals().put(internal.getIdentifier(), internal);
        return internal;
    }

    private Output addOutput(Output output) {
        getMutableOutputs().put(output.getIdentifier(), output);

        // Initialize defaultOutput if this is the first output
        if (defaultOutput == null) defaultOutput = output;

        return output;
    }


    private Map<Symbol, Input> getMutableInputs() {
        if (inputs == null) inputs = new LinkedHashMap<Symbol, Input>();
        return inputs;
    }

    private Map<Symbol, Internal> getMutableInternals() {
        if (internals == null) internals = new LinkedHashMap<Symbol, Internal>();
        return internals;
    }

    private Map<Symbol, Output> getMutableOutputs() {
        if (outputs == null) outputs = new LinkedHashMap<Symbol, Output>();
        return outputs;
    }


    /**
     * Implements the Properties interface and delegates to the specified Parameter map.
     * @param <P>
     */
    private static final class PropertiesDelegate<P extends Param> extends PropsBase {
        private final Map<Symbol, P> params;

        private PropertiesDelegate(Map<Symbol, P> params) {
            this.params = params;
        }

        @Override public <T> T get(Symbol id) {
            return (T) params.get(id);
        }

        @Override public boolean has(Symbol id) {
            return params.containsKey(id);
        }

        @Override public Map<Symbol, Object> getAll(Map<Symbol, Object> out) {
            if (out == null) out = new LinkedHashMap<Symbol, Object>();

            for (Map.Entry<Symbol, P> entry : params.entrySet()) {
                out.put(entry.getKey(), entry.getValue().get());
            }

            return out;
        }

        @Override public Set<Symbol> getIdentifiers(Set<Symbol> identifiersOut) {
            return params.keySet();
        }

        @Override public Object set(Symbol id, Object value) {
            final P param = params.get(id);
            final Object oldValue = param.get();
            param.set(value);
            return oldValue;
        }

        @Override public <T> T remove(Symbol id) {
            throw new UnsupportedOperationException("Can not remove any parameter!");
        }

        @Override public void removeAll() {
            throw new UnsupportedOperationException("Can not remove any parameter!");
        }
    }



}
