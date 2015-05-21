package com.test.hierarchy;

/**
 * Created by Matija Vižintin
 * Date: 16. 05. 2015
 * Time: 12.28
 */
public class Inner {
    private InnerInner innerInner;

    public Inner(InnerInner innerInner) {
        this.innerInner = innerInner;
    }

    public InnerInner getInnerInner() {
        return innerInner;
    }

    public void setInnerInner(InnerInner innerInner) {
        this.innerInner = innerInner;
    }
}
