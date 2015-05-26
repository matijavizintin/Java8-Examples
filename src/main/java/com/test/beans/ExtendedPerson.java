package com.test.beans;

/**
 * Created by Matija Vižintin
 * Date: 21. 05. 2015
 * Time: 21.10
 */
public class ExtendedPerson extends Person implements Comparable {
    public ExtendedPerson() {
    }

    public ExtendedPerson(String name, Integer age) {
        super(name, age);
    }

    @Override public int compareTo(Object o) {
        return getName().compareTo(((Person)o).getName());
    }
}
