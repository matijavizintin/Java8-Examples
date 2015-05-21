package com.test.functional;

import com.test.beans.Person;

/**
 * Created by Matija Vižintin
 * Date: 21. 05. 2015
 * Time: 21.16
 */
public interface ExtendedPersonFactory<P extends Person> {
    P create();
}
