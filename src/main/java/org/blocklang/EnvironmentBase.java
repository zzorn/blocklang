package org.blocklang;

import java.io.File;
import java.util.Collection;

/**
 *
 */
public abstract class EnvironmentBase implements Environment {

    private final LanguageParser parser;
    private final String packageBase;

    protected EnvironmentBase() {
        this("org.blocklang.usergenerated");
    }

    protected EnvironmentBase(final String packageBase) {
        this(new BlockLangParser(), packageBase);
    }

    protected EnvironmentBase(final LanguageParser parser, final String packageBase) {
        this.parser = parser;
        this.packageBase = packageBase;
    }

    @Override
    public void addType(final Type type) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void addType(final Class type, final String identifier, final String description) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void addFunction(final Function function) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void addFunctions(final Class staticUtilityClass) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void addFunction(final String source) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void addModule(final Module module) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void addModule(final String source) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void addModule(final File file) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void addModules(final File directory) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Collection<Type> getTypes() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Collection<Function> getFunctions() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Collection<Module> getModules() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Environment getParent() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Environment createChild() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
