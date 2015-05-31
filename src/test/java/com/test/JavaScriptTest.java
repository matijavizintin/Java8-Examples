package com.test;

import com.test.timed.LoggingTimedTest;
import org.junit.Assert;
import org.junit.Test;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * Created by Matija Vižintin
 * Date: 14. 05. 2015
 * Time: 20.32
 */
public class JavaScriptTest extends LoggingTimedTest {

    /**
     * This is a simple JS engine test. It executes a simple javascript function.
     *
     * @throws ScriptException
     */
    @Test
    public void scriptEngine() throws ScriptException {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");

        // evaluate javascript code
        Double value = (Double)engine.eval( "function f() { return 1; }; f() + 1;" );
        Assert.assertEquals(2., value.doubleValue(), Math.pow(10, -9));
    }
}
