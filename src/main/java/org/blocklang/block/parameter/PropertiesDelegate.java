package org.blocklang.block.parameter;

import org.blocklang.block.parameter.Param;
import org.flowutils.Symbol;
import org.flowutils.collections.props.PropsBase;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Implements the Properties interface and delegates to the specified Parameter map.
 * @param <P>
 */
public final class PropertiesDelegate<P extends Param> extends PropsBase {
    private final Map<Symbol, P> params;

    public PropertiesDelegate(Map<Symbol, P> params) {
        this.params = params;
    }

    @Override public <T> T get(Symbol id) {
        return (T) (params.get(id).get());
    }

    @Override public boolean has(Symbol id) {
        return params.containsKey(id);
    }

    @Override public Map<Symbol, Object> getAll(Map<Symbol, Object> out) {
        if (out == null) out = new LinkedHashMap<Symbol, Object>();

        for (Map.Entry<Symbol, P> entry : params.entrySet()) {
            out.put(entry.getKey(), entry.getValue().get());
        }

        return out;
    }

    @Override public Set<Symbol> getIdentifiers(Set<Symbol> identifiersOut) {
        return params.keySet();
    }

    @Override public Object set(Symbol id, Object value) {
        final P param = params.get(id);
        final Object oldValue = param.get();
        param.set(value);
        return oldValue;
    }

    @Override public <T> T remove(Symbol id) {
        throw new UnsupportedOperationException("Can not remove any parameter!");
    }

    @Override public void removeAll() {
        throw new UnsupportedOperationException("Can not remove any parameter!");
    }
}
