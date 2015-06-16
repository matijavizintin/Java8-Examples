package com.test.lambda;

import com.test.beans.ExtendedPerson;
import com.test.beans.Person;
import com.test.functional.Converter;
import com.test.functional.ExtendedPersonFactory;
import com.test.functional.PersonFactory;
import com.test.timed.LoggingTimedTest;
import org.junit.Test;

/**
 * Created by Matija Vižintin
 * Date: 21. 05. 2015
 * Time: 20.42
 */
public class MethodReferencesTest extends LoggingTimedTest {

    /**
     * Test shows how code can be simplified from pre-Java8 interface implementation to single-row method reference.
     */
    @Test
    public void simplification() {
        // old school
        Converter<String, Integer> converter = new Converter<String, Integer>() {
            @Override public Integer convert(String from) {
                return Integer.valueOf(from);
            }
        };

        // lambda
        Converter<String, Integer> converter1 = from -> Integer.valueOf(from);

        // method reference
        Converter<String, Integer> converter2 = Integer::valueOf;

        // run converter
        String input = "123";
        Integer convertedValue = converter2.convert(input);
        System.out.printf("Input: %s, Converted: %d\n", input, convertedValue);
    }

    /**
     * Pass reference to instance's method to a functional interface implementation.
     */
    @Test
    public void methodReferencing() {
        // instantiate a converter
        Converter<String, String> instanceOfFirstConverter = String::toUpperCase;

        // instantiate a second converter and pass first converter method reference
        Converter<String, String> secondConverter = instanceOfFirstConverter::convert;

        // run converter
        String input = "abc";
        String output = secondConverter.convert(input);
        System.out.printf("Input: %s, Output: %s\n", input, output);
    }

    /**
     * Using method reference to create new objects - as constructor reference.
     *
     * First we implement create method with a constructor. Then we pass crate method 2 parameters to match a constructor.
     */
    @Test
    public void constructorReference() {
        // constructor reference example with a constructor with 2 parameters
        PersonFactory<Person> factory = Person::new;
        Person p = factory.create("Sheldon", 30);
        System.out.println(p);

        // with lambda to make it clear
        PersonFactory<Person> factory1 = (firstName, age) -> new Person(firstName, age);
        Person p1 = factory1.create("Sheldon2", 31);
        System.out.println(p1);

        // extended person example
        ExtendedPersonFactory<Person> factory2 = ExtendedPerson::new;
        Person p2 = factory2.create();
        System.out.println(p2);
    }
}
