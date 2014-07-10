package org.blocklang.block;

import org.blocklang.block.parameter.Param;
import org.blocklang.compiler.BlockCalculator;
import org.flowutils.classbuilder.SourceLocation;

/**
 * Used to build the source for blocks.
 *
 *
 */
public interface BlockBuilder {

    /**
     * This method is called by the BlockBase code, no need to call this in specific Block implementations.
     *
     * @param currentBlock the block that is currently being generated, or null if non-block specific code is being generated.
     *                     Used for resolving shorthand references ("$paramName") to block parameters in added code.
     */
    void setCurrentBlock(Block currentBlock);

    /**
     * @return a new BlockCalculator instance based on the added source.
     *         The internal state of the BlockCalculator is not yet initialized, that should be done by the calling code.
     */
    BlockCalculator createCalculator();

    /**
     * Adds the specified line to the source at the specified location.
     * Indentation and terminating newline will be added to the line.
     *
     * @param line the source line to add.
     *             Parameters can be referenced with $parameterName, that is, a dollar sign followed by the name of a
     *             parameter of the currently generated block.
     */
    void addSourceLine(SourceLocation location, String line);

    /**
     * Adds a source line that imports the specified class.
     * Ignores imports of primitives, and imports the component type of array types.
     */
    void addImport(Class type);

    /**
     * @return unique id in the code for the specified parameter (may differ for different BlockBuilders).
     */
    String getParamId(Param param);

    /**
     * Adds a field for the specified parameter.  Also stores the default value of the parameter, and uses it
     * when initializing a new instance.
     * @param parameter parameter to add.
     * @return the unique id of the field with the parameter value.
     */
    String addParamField(Param parameter);

    /**
     * Creates a new unique field at the start of the class.
     *
     * @param type type of the variable.  Automatically imported if not already imported.
     * @param name descriptive name of the variable (must be a valid identifier and not contain underscores).
     *             A postfix making the generated variable name unique will be automatically added.
     * @param makeFinal if true the variable will be marked final.
     * @param initializationExpression The right hand side of the initialization expression.  A semicolon and newline is added to the end automatically.
     * @return the unique id of the temporary variable.
     */
    String addField(Class type, String name, boolean makeFinal, String initializationExpression);

    /**
     * Creates a new unique variable at the start of the calculation method.
     *
     * @param type type of the variable.  Automatically imported if not already imported.
     * @param name descriptive name of the variable (must be a valid identifier and not contain underscores).
     *             A postfix making the generated variable name unique will be automatically added.
     * @param makeFinal if true the variable will be marked final.
     * @param initializationExpression The right hand side of the initialization expression.  A semicolon and newline is added to the end automatically.
     * @return the unique id of the temporary variable.
     */
    String addVar(Class type, String name, boolean makeFinal, String initializationExpression);

    /**
     * Creates a new unique variable at the specified location.
     *
     * @param location location to place the assignment at (not possible to place in all locations).
     * @param type type of the variable.  Automatically imported if not already imported.
     * @param name descriptive name of the variable (must be a valid identifier and not contain underscores).
     *             A postfix making the generated variable name unique will be automatically added.
     * @param makeFinal if true the variable will be marked final.
     * @param initializationExpression The right hand side of the initialization expression.  A semicolon and newline is added to the end automatically.
     * @return the unique id of the temporary variable.
     */
    String addVar(SourceLocation location, Class type, String name, boolean makeFinal, String initializationExpression);

    /**
     * @return a new unique id for a variable with the specified descriptive name (must be a valid identifier and not contain underscores).
     */
    String createVariableId(String name);

    /**
     * Creates an assignment from a parameter to a parameter in the calculation code.
     *
     * @param paramToAssignTo the parameter that the value is assigned to.
     * @param paramToAssignFrom parameter used to get the value to assign.
     */
    void assign(Param paramToAssignTo, Param paramToAssignFrom);

    /**
     * Creates an assignment statement from an expression to a parameter in the calculation code.
     *
     * @param paramToAssignTo the parameter that the value is assigned to.
     * @param expression expression used to calculate the value.  A semicolon and newline are automatically added at the end.
     */
    void assign(Param paramToAssignTo, String expression);

    /**
     * Creates an assignment statement from an expression to a variable id in the calculation code.
     *
     * @param variableIdToAssignTo the unique id of a variable to assign the expression to (should be generated with addVar to avoid any accidental duplicates generated).
     * @param expression expression used to calculate the value.  A semicolon and newline are automatically added at the end.
     */
    void assign(String variableIdToAssignTo, String expression);

    /**
     * Creates an assignment from a parameter to a parameter.
     *
     * @param location location to place the assignment statement in.
     * @param paramToAssignTo the parameter that the value is assigned to.
     * @param paramToAssignFrom parameter used to get the value to assign.
     */
    void assign(SourceLocation location, Param paramToAssignTo, Param paramToAssignFrom);

    /**
     * Creates an assignment statement from an expression to a parameter.
     *
     * @param location location to place the assignment statement in.
     * @param paramToAssignTo the parameter that the value is assigned to.
     * @param expression expression used to calculate the value.  A semicolon and newline are automatically added at the end.
     */
    void assign(SourceLocation location, Param paramToAssignTo, String expression);

    /**
     * Creates an assignment statement from an expression to a variable id.
     *
     * @param location location to place the assignment statement in.
     * @param variableIdToAssignTo the unique id of a variable to assign the expression to (should be generated with addVar to avoid any accidental duplicates generated).
     * @param expression expression used to calculate the value.  A semicolon and newline are automatically added at the end.
     */
    void assign(SourceLocation location, String variableIdToAssignTo, String expression);

}
