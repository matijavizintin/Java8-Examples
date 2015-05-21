package com.test;

import netscape.javascript.JSException;
import org.junit.Test;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * Created by Matija Vižintin
 * Date: 14. 05. 2015
 * Time: 20.32
 */
public class JavaScriptTest {

    @Test
    public void test1() throws ScriptException {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName( "JavaScript" );

        System.out.println( engine.getClass().getName() );
        System.out.println( "Result:" + engine.eval( "function f() { return 1; }; f() + 1;" ) );
    }
}
