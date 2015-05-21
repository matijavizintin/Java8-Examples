package com.test.flatmap;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Matija Vižintin
 * Date: 16. 05. 2015
 * Time: 09.56
 */
public class One {
    private String name;
    private Set<Many> manies = new HashSet<>();

    public One(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Many> getManies() {
        return manies;
    }

    public void setManies(Set<Many> manies) {
        this.manies = manies;
    }
}
