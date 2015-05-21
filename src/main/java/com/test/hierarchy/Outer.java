package com.test.hierarchy;

/**
 * Created by Matija Vižintin
 * Date: 16. 05. 2015
 * Time: 12.28
 */
public class Outer {
    private Inner inner;

    public Outer(Inner inner) {
        this.inner = inner;
    }

    public Inner getInner() {
        return inner;
    }

    public void setInner(Inner inner) {
        this.inner = inner;
    }
}
