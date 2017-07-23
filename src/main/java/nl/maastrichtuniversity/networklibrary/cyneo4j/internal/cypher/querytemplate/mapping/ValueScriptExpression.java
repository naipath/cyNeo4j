package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.querytemplate.mapping;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class ValueScriptExpression<T,V> implements ValueExpression<T,V> {
    private final String script;
    private final String varName;

    public ValueScriptExpression(String script, String varName) {
        this.script = script;
        this.varName = varName;
    }

    @Override
    public V eval(T val) {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        engine.put(varName, val);
        try {
            V result = (V)engine.eval(script);
            return result;
        } catch (ScriptException e) {
            return null;
        } catch (ClassCastException e) {
            return null;
        }
    }
}
