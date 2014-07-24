package org.blocklang.block;

import org.blocklang.block.parameter.Input;
import org.blocklang.block.parameter.ModuleInput;
import org.blocklang.block.parameter.ModuleOutput;
import org.blocklang.block.parameter.Output;
import org.blocklang.compiler.CalculationListener;
import org.blocklang.compiler.ModuleCalculator;
import org.flowutils.Symbol;
import org.flowutils.collections.props.ReadableProps;

import java.util.List;
import java.util.Map;

/**
 * Contains blocks.  Can be connected to other blocks or modules.
 * Contains compiled calculator class.
 */
public interface Module extends Block {

    /**
     * Adds a block to this module.
     * The block will be calculated and updated by this module.
     *
     * @return the added block.
     */
    <T extends Block> T addBlock(T block);

    /**
     * Removes a block from this module.
     */
    void removeBlock(Block block);

    /**
     * Adds a new input to the module.
     */
    ModuleInput addModuleInput(Class type, String name, Object defaultValue, String description);

    /**
     * Adds a new output to the module.
     */
    ModuleOutput addModuleOutput(Class type, String name, Object defaultValue, String description);

    /**
     * Make the specified input of a block contained in this module into an input to the whole module.
     *
     * @param input the block input to promote to a module input.  Must be of a block in this module that is not already used as a module input.
     * @param name name to use for the module input.  The name of the original input is not affected.
     * @param description description to use for the module input, or null.  The description of the original input is not affected.
     * @return the new module input.
     */
    ModuleInput makeModuleInput(Input input, String name, String description);

    /**
     * Make the specified output of a block contained in this module into an output to the whole module.
     *
     * @param output the block input to promote to a module input.  Must be of a block in this module that is not already used as a module output.
     * @param name name to use for the module output.  The name of the original output is not affected.
     * @param description description to use for the module output, or null.  The description of the original output is not affected.
     * @return the new module output.
     */
    ModuleOutput makeModuleOutput(Output output, String name, String description);

    /**
     * Removes the specified input from the module.  Any internal blocks relying on it will fall back to default values for their input.
     */
    void removeModuleInput(ModuleInput moduleInput);

    /**
     * Removes the specified output from the module.  Any external blocks relying on it will fall back to default values for their input.
     */
    void removeModuleOutput(ModuleOutput moduleOutput);

    /**
     * @return the parameters used to pass inputs to the module.
     */
    Map<Symbol, ModuleInput> getModuleInputs();

    /**
     * @return the parameters used to return the result of this module.
     */
    Map<Symbol, ModuleOutput> getModuleOutputs();


    /**
     * Calculates the outputs of the block in the module, using the inputs of all blocks in the module.
     *
     * Compiles a program for this module if not already compiled, then executes it
     */
    void update();

    /**
     * Calculates the outputs of the block in the module, using the inputs of all blocks in the module.
     *
     * Compiles a program for this module if not already compiled, then executes it
     *
     * @param externalContext context with current context variables (e.g. time, coordinates, etc).
     */
    void update(ReadableProps externalContext, CalculationListener calculationListener);

    /**
     * @return the module calculator for this module, if it has been created.
     */
    ModuleCalculator getModuleCalculator();


    /**
     * Should be called when parameters are connected or disconnected, or blocks added or removed, or the code produced by blocks changed.
     */
    void onStructureChanged();
}
