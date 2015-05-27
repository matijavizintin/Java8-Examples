package com.test.flatmap;

/**
 * Created by Matija Vižintin
 * Date: 16. 05. 2015
 * Time: 09.56
 */
public class Many {
    private String name;
    private One one;

    public Many(String name, One one) {
        this.name = name;
        this.one = one;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public One getOne() {
        return one;
    }

    public void setOne(One one) {
        this.one = one;
    }
}
