package org.blocklang.compiler;

import java.util.*;

import static org.blocklang.compiler.SourceLocation.*;
import static org.flowutils.Check.notNull;

/**
 * Used to build up source for a class to be compiled.
 */
// TODO: Include compilation method as well?
public final class SourceBuilder {

    private final Set<Class> allowedClasses = new HashSet<Class>();
    private final Set<Class> importedClasses = new LinkedHashSet<Class>();
    private final Map<SourceLocation, StringBuilder> sourcePortions = new HashMap<SourceLocation, StringBuilder>();

    private String source;

    /**
     * @param allowedClasses classes that can be imported and used in the generated source.
     */
    public SourceBuilder(Class ... allowedClasses) {
        this(Arrays.asList(allowedClasses));
    }

    /**
     * @param allowedClasses classes that can be imported and used in the generated source.
     */
    public SourceBuilder(Collection<Class> allowedClasses) {
        // Register allowed classes
        for (Class allowedClass : allowedClasses) {
            registerAllowedClass(allowedClass);
        }

        // Setup string builders for each location
        for (SourceLocation sourceLocation : SourceLocation.values()) {
            sourcePortions.put(sourceLocation, new StringBuilder());
        }
    }

    protected void registerAllowedClass(Class allowedClass) {
        notNull(allowedClass, "allowedClass");
        if (allowedClass.isPrimitive()) throw new IllegalArgumentException("Can not add allowed primitive class " + allowedClass.getName() + ", arrays are already allowed");
        if (allowedClass.isArray()) throw new IllegalArgumentException("Can not add allowed array class " + allowedClass.getName() + ", primitives are already allowed");

        allowedClasses.add(allowedClass);
    }


    /**
     * Imports a specified class, if it is white-listed.
     */
    public void importType(Class type) {
        notNull(type, "type");
        if (type.isPrimitive()) throw new IllegalArgumentException("Can not import primitive type '"+type.getName()+"', primitive types are automatically imported");
        if (type.isArray()) throw new IllegalArgumentException("Can not import array type '"+type.getName()+"', array types are automatically imported");

        // Ensure the type to import is white-listed
        if (!allowedClasses.contains(type)) throw new IllegalArgumentException("The type '"+type+"' is not allowed in generated programs.");

        // Add import statement, if not already added
        if (!importedClasses.contains(type)) {
            importedClasses.add(type);

            addSourceLine(IMPORTS, "import " + type.getName());
        }
    }


    /**
     * Adds the specified line to the source at the specified location.
     * Indentation and terminating semicolon will be added to the line.
     */
    public void addSourceLine(SourceLocation location, String line) {
        notNull(location, "location");
        notNull(line, "line");

        sourcePortions.get(location).
                append(location.getIndent()).
                append(line).
                append(";\n");
    }


}
