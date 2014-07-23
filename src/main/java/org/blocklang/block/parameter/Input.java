package org.blocklang.block.parameter;

import org.flowutils.Symbol;

/**
 * Input parameter of a node.
 */
public class Input extends ParamBase {

    private Output source = null;


    public Input(Symbol name, Class type) {
        super(name, type);
    }

    public Input(Symbol name, Class type, Object defaultValue) {
        super(name, type, defaultValue);
    }

    public Input(Symbol name, Class type, Object defaultValue, String description) {
        super(name, type, defaultValue, description);
    }

    @Override public String getSubtypeName() {
        return "input";
    }


    @Override public final void set(Object value) {
        super.set(value);

        // Clear current source if we have any
        if (source != null) {
            removeSource();
        }

        // TODO: Store value in the created ModuleCalculator object if we have one
    }

    /**
     * @param source source parameter that is used when retrieving the value for a parameter, or null to set to none.
     */
    public final void set(Output source) {
        // Remove self as user of previous source, if we had any
        if (this.source != null) {
            this.source.removeUser(this);
        }

        // Update source
        this.source = source;

        // Add self as user of new source, if we have one
        if (this.source != null) {
            this.source.addUser(this);
        }

        // TODO: If we are in a module, mark the Module the block is in to recompile its ModuleCalculator
    }

    @Override public final <T> T get() {
        if (source != null) return source.get();
        else return super.get();
    }

    /**
     * @return the parameter that is used to retrieve the value for this parameter, or null if none.
     */
    public final Param getSource() {
        return source;
    }

    /**
     * Removes the source from this input, reverting to the last set value.
     */
    public final void removeSource() {
        set((Output) null);
    }

    /**
     * Disconnect this input from any outputs it was connected to.
     */
    public void disconnect() {
        removeSource();
    }
}
