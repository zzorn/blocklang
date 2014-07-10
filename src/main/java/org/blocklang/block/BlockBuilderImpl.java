package org.blocklang.block;

import org.blocklang.block.parameter.Param;
import org.blocklang.compiler.BlockCalculator;
import org.flowutils.Check;
import org.flowutils.Symbol;
import org.flowutils.classbuilder.ClassBuilder;
import org.flowutils.classbuilder.ClassBuilderException;
import org.flowutils.classbuilder.JaninoClassBuilder;
import org.flowutils.classbuilder.SourceLocation;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.flowutils.classbuilder.SourceLocation.FIELDS;
import static org.flowutils.classbuilder.SourceLocation.METHODS;

/**
 *
 */
// TODO: Add addInput / addOutput / addInternal methods, store the default value in some map or similar, pass that to the instance when initialized.
public final class BlockBuilderImpl implements BlockBuilder {

    private static final int PARAMETER_NAME_TRUNCATE_LENGTH = 20;
    private final ClassBuilder<BlockCalculator> classBuilder;
    private final Map<Param, String> uniqueParamIds = new LinkedHashMap<Param, String>();
    private Block currentBlock;

    private final Pattern namePattern = Pattern.compile("\\$(\\w+)");

    private final Map<String, Object> parameterDefaultValues = new ConcurrentHashMap<String, Object>();
    private final StringBuilder initializeDefaultsMethod = new StringBuilder();

    public BlockBuilderImpl() {
        classBuilder = new JaninoClassBuilder<BlockCalculator>(BlockCalculator.class,
                                                                "calculate",
                                                                "externalContext",
                                                                "inputParameters",
                                                                "outputParameters",
                                                                "calculationListener");

        // Start initialization method
        initializeDefaultsMethod.append("public void initializeDefaults(Map parameterDefaults) {\n");
        addImport(Map.class);
    }

    @Override public void setCurrentBlock(Block currentBlock) {
        this.currentBlock = currentBlock;
    }

    @Override public BlockCalculator createCalculator() {

        // Finalize initialization method
        initializeDefaultsMethod.append("  }");
        classBuilder.addSourceLine(METHODS, initializeDefaultsMethod.toString());

        try {
            // Compile a new instance
            final BlockCalculator blockCalculator = classBuilder.createInstance();


            // TODO: Remove: DEBUG: show code:
            System.out.println(classBuilder.createSource());


            // Initialize it
            blockCalculator.initializeDefaults(parameterDefaultValues);

            return blockCalculator;
        } catch (ClassBuilderException e) {
            throw new IllegalStateException("Problem when creating a BlockCalculator: " + e.getMessage(), e);
        }
    }

    @Override public void addSourceLine(SourceLocation location, String line) {
        classBuilder.addSourceLine(location, expandParameterReferences(line));
    }

    /**
     * Replaces any references to parameter names in the code (of style "$paramName") with the real identifier of the parameter.
     */
    private String expandParameterReferences(String sourceString) {

        StringBuffer buffer = new StringBuffer();

        // Replace parameter refs with parameters
        Matcher matcher = namePattern.matcher(sourceString);
        while (matcher.find()) {
            // Get matched text
            final String match = matcher.group(1);

            // Check that we have some current block specified
            if (currentBlock == null) throw new IllegalStateException("No current block specified, can not expand '"+match+"' in source string \n" + sourceString);

            // Get actual unique parameter id
            final Param parameter = currentBlock.getParameter(Symbol.get(match));
            if (parameter == null) throw new IllegalStateException("Can not find a parameter named '"+match+"' in source string \n" + sourceString);

            // Replace the parameter reference with the actual unique parameter id
            matcher.appendReplacement(buffer, parameter.getId(this));
        }
        matcher.appendTail(buffer);

        // Return updated source string
        return buffer.toString();

/*
        if (currentBlock != null) {
            sourceString.
        }
        else {
            return sourceString;
        }
        */
    }

    @Override public void addImport(Class type) {
        classBuilder.addImport(type);
    }

    @Override public String getParamId(Param param) {
        if (!uniqueParamIds.containsKey(param)) {
            // Ensure that the parameter id can be safely used in generated code, and truncate it if it is too long.
            String paramIdentifier = param.getName().getString();
            if (paramIdentifier.length() > PARAMETER_NAME_TRUNCATE_LENGTH) {
                paramIdentifier = paramIdentifier.substring(0, PARAMETER_NAME_TRUNCATE_LENGTH);
            }
            Check.strictIdentifier(paramIdentifier, "(truncated) parameter identifier");

            // Create new unique id, remember it, and return it
            final String uniqueId = classBuilder.createUniqueIdentifier(param.getSubtypeName(), paramIdentifier);
            uniqueParamIds.put(param, uniqueId);
            return uniqueId;
        } else {
            // Retrieve previously generated unique id
            return uniqueParamIds.get(param);
        }
    }

    @Override public String addParamField(Param parameter) {
        // Get unique identifier for the variable
        final String id = getParamId(parameter);

        // Import type if needed
        final Class type = parameter.getType();
        addImport(type);

        // Add field declaration
        addSourceLine(FIELDS, "private " + parameter.getPrimitiveTypeIfPossible().getCanonicalName() + " " + id + ";");

        // Add field initialization
        initializeDefaultsMethod.append("    if (parameterDefaults.containsKey(\""+id+"\")) { " + id + " = ("+type.getCanonicalName()+") parameterDefaults.get(\""+id+"\"); }\n");

        // Store default value for initialization
        parameterDefaultValues.put(id, parameter.getDefaultValue());

        // Return unique identifier of the parameter field
        return id;
    }

    @Override public String addField(Class type,
                                     String name,
                                     boolean makeFinal,
                                     String initializationExpression) {
        return addVar(FIELDS, type, name, makeFinal, initializationExpression);
    }

    @Override public String addVar(Class type,
                                   String name,
                                   boolean makeFinal,
                                   String initializationExpression) {
        return addVar(SourceLocation.BEFORE_CALCULATION, type, name, makeFinal, initializationExpression);
    }

    @Override public String addVar(SourceLocation location,
                                   Class type,
                                   String name,
                                   boolean makeFinal,
                                   String initializationExpression) {

        // Create unique identifier for the variable
        final String id = createVariableId(name);

        // Check that we can add a variable at the location
        if (!location.isValidVariableLocation()) throw new IllegalArgumentException("Can not create a new variable '"+name+"' at the source location " + location);

        // Import type if needed
        addImport(type);

        // Construct the source line
        String line = (makeFinal ? "final " : "") + type.getCanonicalName() + " " + id + (initializationExpression == null ? "" : " = " + initializationExpression) + ";";

        // Fields need to be prefixed with visibility modifier
        if (location == FIELDS) {
            line = "private " + line;
        }

        // Add to source
        addSourceLine(location, line);

        // Return unique identifier of the variable
        return id;
    }


    @Override public String createVariableId(String name) {
        // Ensure that the name can be safely used in generated code, and truncate it if it is too long.
        if (name.length() > PARAMETER_NAME_TRUNCATE_LENGTH) {
            name = name.substring(0, PARAMETER_NAME_TRUNCATE_LENGTH);
        }
        Check.strictIdentifier(name, "(truncated) variable name");

        // Create unique identifier for the variable
        return classBuilder.createUniqueIdentifier("var", name);
    }

    @Override public void assign(Param paramToAssignTo, Param paramToAssignFrom) {
        assign(SourceLocation.AT_CALCULATION, paramToAssignTo, paramToAssignFrom);
    }

    @Override public void assign(Param paramToAssignTo, String expression) {
        assign(SourceLocation.AT_CALCULATION, paramToAssignTo, expression);
    }

    @Override public void assign(String variableIdToAssignTo, String expression) {
        assign(SourceLocation.AT_CALCULATION, variableIdToAssignTo, expression);
    }

    @Override public void assign(SourceLocation location, Param paramToAssignTo, Param paramToAssignFrom) {
        assign(location, getParamId(paramToAssignTo), getParamId(paramToAssignFrom));
    }

    @Override public void assign(SourceLocation location, Param paramToAssignTo, String expression) {
        assign(location, getParamId(paramToAssignTo), expression);
    }

    @Override public void assign(SourceLocation location, String variableIdToAssignTo, String expression) {
        if (!location.isValidAssignmentLocation()) throw new IllegalArgumentException("Can not add any assignment statement at the location " + location);

        addSourceLine(location, variableIdToAssignTo + " = " + expression + ";");
    }

}
