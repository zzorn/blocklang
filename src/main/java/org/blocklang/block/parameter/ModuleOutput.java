package org.blocklang.block.parameter;

import org.flowutils.Symbol;

/**
 * Output from a module.  Offers an Input to itself which is visible to module internal blocks.
 */
public final class ModuleOutput extends Output {

    private final Input input;

    public ModuleOutput(Symbol name, Class type) {
        this(name, type, null);
    }

    public ModuleOutput(Symbol name, Class type, Object defaultValue) {
        this(name, type, defaultValue, null);
    }

    public ModuleOutput(Symbol name, Class type, Object defaultValue, String description) {
        super(name, type, defaultValue, description);

        input = new Input(name, type, defaultValue, description);
    }

    /**
     * @return the module internal input for this output.
     */
    public Input getInternalInput() {
        return input;
    }

    @Override public String getSubtypeName() {
        return "moduleOutput";
    }

    @Override public void disconnect() {
        super.disconnect();

        input.disconnect();
    }
}
