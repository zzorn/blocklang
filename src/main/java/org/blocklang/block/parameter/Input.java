package org.blocklang.block.parameter;

import org.blocklang.block.Block;
import org.blocklang.block.Module;
import org.blocklang.compiler.ModuleCalculator;
import org.flowutils.Symbol;

/**
 * Input parameter of a node.
 */
public class Input extends ParamBase {

    private Output source = null;

    /**
     * @param host the block the parameter is located in.
     * @param name   name for the parameter, should be unique within the block the parameter is in.
     * @param type         type of the parameter.
     * @param defaultValue initial and default value for the parameter.
     * @param description  human readable description of the parameter.
     */
    public Input(Block host, Symbol name, Class type, Object defaultValue, String description) {
        super(host, name, type, defaultValue, description);
    }

    @Override public String getSubtypeName() {
        return "input";
    }


    @Override public final void set(Object value) {
        if (super.get() != value) {
            super.set(value);

            // Clear current source if we have any
            if (source != null) {
                removeSource();
            }

            // Store value in the created ModuleCalculator object if we have one
            final Module module = getHost().getModule();
            if (module != null) {
                final ModuleCalculator moduleCalculator = module.getModuleCalculator();
                if (moduleCalculator != null) {
                    // TODO: Store the code identifier of the parameter as created with blockBuilder with the parameter
                    // TODO: Add method for setting (and getting) the value of a given parameter in the module calculator.
                    /*
                    moduleCalculator.setParameter(codeId, value);
                     */
                }
            }

        }
    }

    /**
     * @param source source parameter that is used when retrieving the value for a parameter, or null to set to none.
     */
    public final void set(Output source) {
        if (this.source != source) {
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

            // Notify the block and module, so that the ModuleCalculator can be recompiled.
            getHost().onStructureChanged();
        }
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
