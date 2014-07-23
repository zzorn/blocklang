package org.blocklang.block.parameter;

import org.flowutils.Symbol;

import java.util.ArrayList;
import java.util.List;

/**
 * Result parameter of a node.
 */
public class Output extends ParamBase {

    private final List<Input> connectedUsers = new ArrayList<Input>();

    /**
     * @param name name for the parameter, should be unique within the block the parameter is in.
     * @param type       type of the parameter.
     */
    public Output(Symbol name, Class type) {
        super(name, type);
    }

    /**
     * @param name   name for the parameter, should be unique within the block the parameter is in.
     * @param type         type of the parameter.
     * @param defaultValue initial and default value for the parameter.
     */
    public Output(Symbol name, Class type, Object defaultValue) {
        super(name, type, defaultValue);
    }

    /**
     * @param name   name for the parameter, should be unique within the block the parameter is in.
     * @param type         type of the parameter.
     * @param defaultValue initial and default value for the parameter.
     * @param description  human readable description of the parameter.
     */
    public Output(Symbol name, Class type, Object defaultValue, String description) {
        super(name, type, defaultValue, description);
    }

    @Override public String getSubtypeName() {
        return "output";
    }

    /**
     * Disconnects any inputs using this output from it.
     */
    public final void disconnectUsers() {
        for (Input connectedInput : connectedUsers) {
            connectedInput.removeSource();
        }
        connectedUsers.clear();
    }

    /**
     * @param input an user of this output to keep track of and notify if this output is removed.
     */
    public final void addUser(Input input) {
        connectedUsers.add(input);
    }

    /**
     * @param input user to remove.
     */
    public final void removeUser(Input input) {
        connectedUsers.remove(input);
    }

    /**
     * Disconnect this output from any users of it.
     */
    public void disconnect() {
        disconnectUsers();
    }
}
