package com.test.functional;

import com.test.beans.Person;

/**
 * Created by Matija Vižintin
 * Date: 21. 05. 2015
 * Time: 21.06
 */
public interface PersonFactory<P extends Person> {
    P create(String firstName, Integer age);
}
