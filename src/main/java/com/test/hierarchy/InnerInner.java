package com.test.hierarchy;

/**
 * Created by Matija Vižintin
 * Date: 16. 05. 2015
 * Time: 12.28
 */
public class InnerInner {
    private InnerInnerInner innerInnerInner;

    public InnerInner(InnerInnerInner innerInnerInner) {
        this.innerInnerInner = innerInnerInner;
    }

    public InnerInnerInner getInnerInnerInner() {
        return innerInnerInner;
    }

    public void setInnerInnerInner(InnerInnerInner innerInnerInner) {
        this.innerInnerInner = innerInnerInner;
    }
}
