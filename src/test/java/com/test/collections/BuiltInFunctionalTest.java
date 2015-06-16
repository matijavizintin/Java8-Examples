package com.test.collections;

import com.test.beans.ExtendedPerson;
import com.test.beans.Person;
import com.test.generators.DataGenerator;
import com.test.timed.LoggingTimedTest;
import org.junit.Assert;
import org.junit.Test;

import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Created by Matija Vižintin
 * Date: 22. 05. 2015
 * Time: 15.26
 */
public class BuiltInFunctionalTest extends LoggingTimedTest {

    /**
     * Test demonstrates usage of predicates. More or less the same as guava.
     */
    @Test
    public void predicates() {
        Predicate<String> isEmptyPredicate = String::isEmpty;

        // test predicate
        boolean test1 = isEmptyPredicate.test("abc");
        boolean test2 = isEmptyPredicate.negate().test("abc");
        boolean test3 = isEmptyPredicate.or(isEmptyPredicate.negate()).test("abc");

        // assert
        Assert.assertFalse(test1);
        Assert.assertTrue(test2);
        Assert.assertTrue(test3);

        // predicate in practice
        Predicate<Integer> integerPredicate = integer -> integer > 5;
        long count = DataGenerator.integers(10).stream().filter(integerPredicate).count();
        Assert.assertEquals(4, count);
    }

    /**
     * Test demonstrates usage of functions. More or less the same as guava.
     */
    @Test
    public void functions() {
        // string to integer function
        Function<String, Integer> stringToIntegerFunction = Integer::parseInt;

        // concatenate functions
        Function<String, Double> stringToIntegerToDoubleFunction = stringToIntegerFunction.andThen(Integer::doubleValue);

        // apply
        double d = stringToIntegerToDoubleFunction.apply("10");
        Assert.assertEquals(10D, d, Math.pow(10, -9));          // assert

        // compose - identity
        Function<Integer, Integer> identity = stringToIntegerFunction.compose(Object::toString);
        int input = 10;
        int i = identity.apply(input);
        Assert.assertEquals(Function.identity().apply(input), i);

        // function in practice
        Function<Integer, String> toStringFunction = Object::toString;
        DataGenerator.integers(10).stream().map(toStringFunction).forEach(s -> System.out.printf("%s ", s));
        System.out.println();
    }

    /**
     * Test demonstrates usage of suppliers and consumers.
     */
    @Test
    public void suppliersAndConsumers() {
        // suppliers
        Supplier<Person> supplier = Person::new;
        Person p = supplier.get();

        // assert empty person
        Assert.assertNull(p.getName());
        Assert.assertNull(p.getAge());

        // consumer
        Consumer<Person> consumer = person -> System.out.print(person.getName());
        consumer = consumer.andThen(person -> System.out.printf(" is %d old.\n", person.getAge()));
        consumer.accept(new Person("Sheldon", 30));

        // supplier in practice
        Person p1 = DataGenerator.integers(10).stream().collect(
                supplier, (person, integer) -> person.setAge(Optional.ofNullable(person.getAge()).orElse(0) + integer),
                (person1, person2) -> System.out.println(
                        "Since we are not in parallel this is never gonna happen. If you see this print, there is something wrong with the test"));
        System.out.println(p1);

        // consumer in practice
        Consumer<Integer> integerConsumer = System.out::print;
        DataGenerator.integers(10).stream().forEach(integerConsumer);
        System.out.println();
    }

    /**
     * Test demonstrates usage of comparators.
     */
    @Test
    public void comparators() {
        Comparator<Person> personAgeComparator = (o1, o2) -> {
            int r =  o1.getAge().compareTo(o2.getAge());
            System.out.printf("Comparing person by age. Result: %d\n", r);
            return r;
        };
        Comparator<Person> personNameComparator = (o1, o2) -> {
            int r = o1.getName().compareTo(o2.getName());
            System.out.printf("Comparing person by name. Result: %d\n", r);
            return r;
        };
        Comparator<ExtendedPerson> extendedPersonComparator = ExtendedPerson::compareTo;

        // compare
        ExtendedPerson p1 = new ExtendedPerson("Sheldon", 30);
        ExtendedPerson p2 = new ExtendedPerson("Leonard", 30);
        int comparison1 = personAgeComparator.compare(p1, p2);
        int comparison2 = extendedPersonComparator.compare(p1, p2);

        // assert
        Assert.assertEquals(0, comparison1);
        Assert.assertTrue(comparison2 > 0);

        // chaining comparators
        System.out.println("\nTesting comparators chaining.");

        System.out.println("2 comparison should happen.");
        int ageNameComparison = personAgeComparator.thenComparing(personNameComparator).compare(p1, p2);

        System.out.println("1 comparison should happen.");
        int nameAgeComparison = personNameComparator.thenComparing(personAgeComparator).compare(p1, p2);

        // assert
        Assert.assertTrue(ageNameComparison > 0);
        Assert.assertTrue(nameAgeComparison > 0);
    }

    /**
     * Optional class test. Not actual functional interface but an utility.
     */
    @Test
    public void optional() {
        // case with value
        Optional<String> optional = Optional.of("abc");
        boolean present = optional.isPresent();
        String value = optional.get();
        optional.ifPresent(System.out::println);

        // assert
        Assert.assertTrue(present);
        Assert.assertNotNull(value);

        // case without value
        Optional<String> optionalNull = Optional.ofNullable(null);
        present = optionalNull.isPresent();
        System.out.println(optionalNull.orElse("Else"));     // else will be printed

        // assert
        Assert.assertFalse(present);
        Assert.assertNotNull(optionalNull.orElse("Else"));
    }

    /**
     * Testing optional with null input. It's expected to throw a NullPointerException.
     */
    @Test(expected = NullPointerException.class)
    public void optionalOfNull() {
        Optional.of(null);
    }

    /**
     * Testing optional without else value. It's expected to throw a NoSuchElementException.
     */
    @Test(expected = NoSuchElementException.class)
    public void optionalWithoutElse() {
        Optional.ofNullable(null).get();
    }
}
