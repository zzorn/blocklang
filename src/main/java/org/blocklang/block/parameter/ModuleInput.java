package org.blocklang.block.parameter;

import org.flowutils.Symbol;

/**
 * Input to a module.  Offers an Output from itself which is visible to module internal blocks.
 */
public final class ModuleInput extends Input {

    private final Output output;

    public ModuleInput(Symbol name, Class type) {
        this(name, type, null);
    }

    public ModuleInput(Symbol name, Class type, Object defaultValue) {
        this(name, type, defaultValue, null);
    }

    public ModuleInput(Symbol name, Class type, Object defaultValue, String description) {
        super(name, type, defaultValue, description);

        output = new Output(name, type, defaultValue, description);
    }

    /**
     * @return output of this parameter for module internal use.
     */
    public Output getInternalOutput() {
        return output;
    }

    @Override public String getSubtypeName() {
        return "moduleInput";
    }

    @Override public void disconnect() {
        super.disconnect();

        output.disconnect();
    }
}
