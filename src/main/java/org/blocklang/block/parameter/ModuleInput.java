package org.blocklang.block.parameter;

import org.blocklang.block.Block;
import org.flowutils.Symbol;

/**
 * Input to a module.  Offers an Output from itself which is visible to module internal blocks.
 */
public final class ModuleInput extends Input {

    private final Output output;

    /**
     * @param host the block the parameter is located in.
     * @param name   name for the parameter, should be unique within the block the parameter is in.
     * @param type         type of the parameter.
     * @param defaultValue initial and default value for the parameter.
     * @param description  human readable description of the parameter.
     */
    public ModuleInput(Block host, Symbol name, Class type, Object defaultValue, String description) {
        super(host, name, type, defaultValue, description);

        output = new Output(host, name, type, defaultValue, description);
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
