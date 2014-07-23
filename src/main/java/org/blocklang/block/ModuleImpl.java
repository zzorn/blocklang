package org.blocklang.block;

import org.blocklang.block.parameter.*;
import org.blocklang.compiler.CalculationListener;
import org.blocklang.compiler.ModuleCalculator;
import org.flowutils.Symbol;
import org.flowutils.classbuilder.SourceLocation;
import org.flowutils.collections.props.ReadableProps;
import org.flowutils.collections.props.WritableProps;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.flowutils.Check.*;
import static org.flowutils.Check.notNull;

/**
 *
 */
public class ModuleImpl extends BlockBase implements Module {

    private final List<Block> blocks = new ArrayList<Block>();

    private ModuleCalculator moduleCalculator;

    private PropertiesDelegate<Input> inputPropertiesDelegate;
    private PropertiesDelegate<Output> outputPropertiesDelegate;

    private final Map<Symbol, ModuleInput> moduleInputs = new LinkedHashMap<Symbol, ModuleInput>();
    private final Map<Symbol, ModuleOutput> moduleOutputs = new LinkedHashMap<Symbol, ModuleOutput>();


    @Override public <T extends Block> T addBlock(T block) {
        blocks.add(block);
        return block;
    }

    @Override public void removeBlock(Block block) {
        if (blocks.contains(block)) {
            blocks.remove(block);
            block.disconnect();
        }
    }

    @Override public ModuleInput addModuleInput(Class type, String name, Object defaultValue, String description) {
        notNull(type, "type");
        strictIdentifier(name, "name");
        final Symbol id = Symbol.get(name);
        if (hasModuleParamNamed(id)) throw new IllegalArgumentException("The module already contains a parameter with the name '"+name+"'");

        // Create module input
        final ModuleInput moduleInput = new ModuleInput(id, type, defaultValue, description);

        // Store it
        moduleInputs.put(id, moduleInput);
        addInput(moduleInput);

        return moduleInput;
    }

    @Override public ModuleOutput addModuleOutput(Class type, String name, Object defaultValue, String description) {
        notNull(type, "type");
        strictIdentifier(name, "name");
        final Symbol id = Symbol.get(name);
        if (hasModuleParamNamed(id)) throw new IllegalArgumentException("The module already contains a parameter with the name '"+name+"'");

        // Create module output
        final ModuleOutput moduleOutput = new ModuleOutput(id, type, defaultValue, description);

        // Store it
        moduleOutputs.put(id, moduleOutput);
        addOutput(moduleOutput);

        return moduleOutput;
    }

    private boolean hasModuleParamNamed(Symbol id) {
        return moduleOutputs.containsKey(id) ||
               moduleInputs.containsKey(id);
    }

    @Override public ModuleInput makeModuleInput(Input input, String name, String description) {
        if (!hasBlockWithInput(input)) throw new IllegalArgumentException("The specified input was not found in any block in this module");

        // Create module input with the same type as the input and the specified name
        final ModuleInput moduleInput = addModuleInput(input.getType(), name, input.getDefaultValue(), description);

        // Set the module input as source for the input
        input.set(moduleInput.getInternalOutput());

        return moduleInput;
    }

    @Override public ModuleOutput makeModuleOutput(Output output, String name, String description) {
        if (!hasBlockWithOutput(output)) throw new IllegalArgumentException("The specified output was not found in any block in this module");

        // Create module output with the same type as the output and the specified name
        final ModuleOutput moduleOutput = addModuleOutput(output.getType(), name, output.getDefaultValue(), description);

        // Set the output as the source for the module output
        moduleOutput.getInternalInput().set(output);

        return moduleOutput;
    }

    @Override public void removeModuleInput(ModuleInput moduleInput) {
        if (moduleInputs.containsValue(moduleInput)) {
            moduleInputs.remove(moduleInput.getName());

            // Disconnect it from any source it was using
            moduleInput.removeSource();

            // Disconnect any inputs using it
            moduleInput.getInternalOutput().disconnectUsers();
        }
    }

    @Override public void removeModuleOutput(ModuleOutput moduleOutput) {
        if (moduleOutputs.containsValue(moduleOutput)) {
            moduleOutputs.remove(moduleOutput.getName());

            // Disconnect it from any source it was using
            moduleOutput.getInternalInput().removeSource();

            // Disconnect any inputs using it
            moduleOutput.disconnectUsers();
        }
    }

    @Override public Map<Symbol, ModuleInput> getModuleInputs() {
        return moduleInputs;
    }

    @Override public Map<Symbol, ModuleOutput> getModuleOutputs() {
        return moduleOutputs;
    }

    @Override public void update() {
        update(null, null);
    }

    @Override public void update(ReadableProps externalContext, CalculationListener calculationListener) {
        // Compile the calculation if not already compiled
        if (moduleCalculator == null) {
            moduleCalculator = compileModuleCalculator();
        }
        if (moduleCalculator == null) throw new IllegalStateException("ModuleCalculator should have been created");

        // Initialize the delegates if not already initialized.
        if (inputPropertiesDelegate    == null) inputPropertiesDelegate  = new PropertiesDelegate<Input>(getMutableInputs());
        if (outputPropertiesDelegate   == null) outputPropertiesDelegate = new PropertiesDelegate<Output>(getMutableOutputs());

        // Invoke calculation, delegating inputs, outputs, and internal state to parameters in this block.
        moduleCalculator.update(externalContext,
                                inputPropertiesDelegate,
                                outputPropertiesDelegate,
                                calculationListener);

    }


    protected final ModuleCalculator compileModuleCalculator() {

        // Generate code
        final BlockBuilder blockBuilder = new BlockBuilderImpl();
        generateCalculatorCode(blockBuilder);

        // Compile and create instance
        return blockBuilder.createCalculator();
    }

    /**
     * Generate code that reads input parameters, initializes internal parameters, generates any block-specific code,
     * and then updates the output parameters (and internal parameters).
     */
    public void generateCalculatorCode(BlockBuilder blockBuilder) {

        // Set current block for the builder, used to resolve parameter references
        blockBuilder.setCurrentBlock(this);

        // Add internal input/ouput parameters
        for (ModuleInput moduleInput : moduleInputs.values()) {
            blockBuilder.addParamField(moduleInput.getInternalOutput());
        }
        for (ModuleOutput moduleOutput : moduleOutputs.values()) {
            blockBuilder.addParamField(moduleOutput.getInternalInput());
        }

        // Add initialization of module input values
        for (Input input : getMutableInputs().values()) {
            final String inputName = input.getName().toString();
            final String inputField = input.getId(blockBuilder);
            blockBuilder.addSourceLine(SourceLocation.BEFORE_CALCULATION,
                                       "if (inputParameters.has(\"" + inputName +"\")) { " +
                                       inputField + " = (" + input.getType().getCanonicalName() +") (inputParameters.get(\"" + inputName +"\").get()); " +
                                       "}");
        }

        // Assign values of module inputs to internal outputs
        for (ModuleInput moduleInput : moduleInputs.values()) {
            blockBuilder.assign(moduleInput.getInternalOutput(), moduleInput);
        }

        // Clear current block
        blockBuilder.setCurrentBlock(null);

        // Create block-specific code
        generateCode(blockBuilder);

        // Restore current block
        blockBuilder.setCurrentBlock(this);


        // Assign values of internal inputs to module outputs
        for (ModuleOutput moduleOutput : moduleOutputs.values()) {
            blockBuilder.assign(moduleOutput, moduleOutput.getInternalInput());
        }

        // Add return of output values
        for (Output output : getMutableOutputs().values()) {
            final String outputName = output.getName().toString();
            final String outputField = output.getId(blockBuilder);
            blockBuilder.addSourceLine(SourceLocation.AFTER_CALCULATION, "outputParameters.get(\""+ outputName +"\").set("+ outputField +");");
        }

        // Clear current block
        blockBuilder.setCurrentBlock(null);
    }


    @Override protected void doGenerateCode(BlockBuilder blockBuilder) {
        // Generate code for the blocks in the module
        for (Block block : blocks) {
            block.generateCode(blockBuilder);
        }
    }

    @Override public Param getParameter(Symbol name) {
        Param parameter = super.getParameter(name);

        if (parameter == null) {
            // Look for input parameter for the module with the specified name
            for (ModuleInput moduleInput : moduleInputs.values()) {
                final Output internalOutput = moduleInput.getInternalOutput();
                if (internalOutput.getName() == name) return internalOutput;
            }

            // Look for output parameter for the module with the specified name
            for (ModuleOutput moduleOutput : moduleOutputs.values()) {
                final Input internalInput = moduleOutput.getInternalInput();
                if (internalInput.getName() == name) return internalInput;
            }
        }

        return parameter;
    }

    /*
    @Override public final void resetInternalState() {
        // TODO: If composite block, recursively call resetInternalState.  Also just reset internal state.

        final Map<Symbol, Internal> internals = getMutableInternals();

        if (internals != null) {
            for (Internal internal : internals.values()) {
                internal.resetToDefault();
            }
        }
    }

    @Override public void getInternalState(WritableProps internalState) {
        // TODO: Implement

    }

    @Override public void setInternalState(ReadableProps internalState) {
        // TODO: Implement

    }
    */

    private boolean hasBlockWithInput(Input input) {
        for (Block block : blocks) {
            if (block.getInputs().containsValue(input)) return true;
        }

        return false;
    }

    private boolean hasBlockWithOutput(Output output) {
        for (Block block : blocks) {
            if (block.getOutputs().containsValue(output)) return true;
        }

        return false;
    }



}
