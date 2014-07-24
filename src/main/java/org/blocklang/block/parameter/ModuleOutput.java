package org.blocklang.block.parameter;

import org.blocklang.block.Block;
import org.flowutils.Symbol;

/**
 * Output from a module.  Offers an Input to itself which is visible to module internal blocks.
 */
public final class ModuleOutput extends Output {

    private final Input input;

    /**
     * @param host the block the parameter is located in.
     * @param name   name for the parameter, should be unique within the block the parameter is in.
     * @param type         type of the parameter.
     * @param defaultValue initial and default value for the parameter.
     * @param description  human readable description of the parameter.
     */
    public ModuleOutput(Block host, Symbol name, Class type, Object defaultValue, String description) {
        super(host, name, type, defaultValue, description);

        input = new Input(host, name, type, defaultValue, description);
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
