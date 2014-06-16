package org.blocklang;

import org.blocklang.parser.Compilation;

import java.io.File;
import java.util.Collection;

/**
 *
 */
public interface Environment {

    /**
     * Add a type that can be used from the compiled code.
     * @param type
     */
    void addType(Type type);

    /**
     * Add a class that can be used from the compiled code.
     * @param type
     * @param identifier
     * @param description
     */
    void addType(Class type, String identifier, String description);

    /**
     * Add a static function that is available to the compiled code.
     * @param function
     */
    void addFunction(Function function);

    /**
     * Add all public static functions from the specified class, so that they
     * are available to the compiled code.
     *
     * Descriptions and metadata for the functions can be specified with annotations.
     *
     * @param staticUtilityClass class with static functions that should be available to
     *                           compiled code.
     */
    void addFunctions(Class staticUtilityClass);

    void addFunction(String source);

    /**
     * Add a module to be compiled.
     * @param module
     */
    void addModule(Module module);

    /**
     * Parses the given source to a module, and adds it.
     * @param source source code.
     */
    void addModule(String source);

    /**
     * Loads source code from the given file, parses it to a module, and adds it.
     * @param file file with one or more modules.
     */
    void addModule(File file);

    void addModules(File directory);

    /**
     * @return types available to modules.
     */
    Collection<Type> getTypes();

    /**
     * @return static functions available to modules.
     */
    Collection<Function> getFunctions();

    /**
     * @return modules available in the environment.
     */
    Collection<Module> getModules();

    /**
     * Compiles all modules in the environment.
     * @return compilation that can be used to access the compiled classes.
     */
    Compilation compile();

    /**
     * @return the parent environment of this environment, or null if none.
     */
    Environment getParent();

    /**
     * @return a new environment which contains all of the members of this environment,
     *         but where new members can be added without affecting the parent environment.
     */
    Environment createChild();
}
