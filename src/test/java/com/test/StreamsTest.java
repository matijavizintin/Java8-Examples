package com.test;

import com.google.common.base.Joiner;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Matija Vižintin
 * Date: 05. 05. 2015
 * Time: 14.43
 */
public class StreamsTest {

    @Test public void test1() {
        List<Person> input = generatePersons(100);
        Map<Integer, List<Person>> map = input.stream().collect(Collectors.groupingBy(Person::getAge));
        print(map);
    }

    @Test public void test2() {
        List<Person> input = generatePersons(100);
        List<Person> list = input.stream().filter(person -> person.age > 10).sorted((o1, o2) -> o1.getAge().compareTo(o2.getAge())).collect(
                Collectors.toList());
        print(list);
    }

    @Test public void test3() {
        List<Person> input = generatePersons(100);
        Map<Integer, List<Person>> map = input.parallelStream().collect(Collectors.groupingByConcurrent(t -> t.age));
        print(map);
    }

    @Test public void test4() {
        List<Person> input = generatePersons(100);
        List<Integer> output = Lists.newArrayList();
        input.forEach(
                person -> {
                    output.add(person.getAge());
                    System.out.println(person);
                });
        print(output);
    }

    @Test public void test5() {
        List<Person> input = generatePersons(100);
        System.out.println("Old ages");
        print(input);
        input.forEach(person -> person.setAge((int)(Math.random() * 100)));
        System.out.println("New ages");
        print(input);
    }

    @Test public void test6() throws Exception {
        List<Person> input = generatePersons(1000 * 1000);

        long start = System.currentTimeMillis();
        List<String> output = input.parallelStream().map(person -> person.getName()).collect(Collectors.toList());
        long stop = System.currentTimeMillis();
        System.out.println("exec time parallel = " + (stop - start));
        print(output);

        start = System.currentTimeMillis();
        output = input.stream().map(person -> person.getName()).collect(Collectors.toList());
        stop = System.currentTimeMillis();
        System.out.println("exec time single = " + (stop - start));

        print(output);
    }

    @Test public void test7() {
        List<Person> input = generatePersons(1000);
        List<Person> students = input.stream().parallel()         // set concurrent
                .filter(p -> p.getAge() > 18)  // filtering will be performed concurrently
                .sequential()       // set single threaded
                .map(Person::getAge).map(Person::new).collect(Collectors.toCollection(ArrayList::new));
    }

    @Test public void test8() {
        // count all male years
        List<Person> input = generatePersons(1000000);
        Stopwatch sw = Stopwatch.createStarted();
        int summed = input.stream().filter(person -> person.getGender() == Gender.MALE).mapToInt(Person::getAge).sum();
        System.out.println((sw.stop().elapsed(TimeUnit.MILLISECONDS)));
        System.out.println("summed = " + summed);

        // count total years in parallel
        sw = Stopwatch.createStarted();
        int totalY = input.stream().parallel().map(Person::getAge).reduce(0, Integer::sum);
        System.out.println((sw.stop().elapsed(TimeUnit.MILLISECONDS)));
        System.out.println("totalY = " + totalY);
    }

    @Test
    public void test9() throws Exception {
        // crate file
        File f = new File("temp.txt");
        f.createNewFile();
        BufferedWriter bw = Files.newWriter(f, Charset.defaultCharset());
        List<Person> persons = generatePersons(1000);

        persons.forEach(
                person -> {
                    try {
                        bw.write(person.getName());
                        bw.newLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        bw.flush();

        // read file
        try (Stream<String> lines = java.nio.file.Files.lines(f.toPath(), Charset.defaultCharset())) {
            lines.onClose(() -> System.out.println("File ended.")).forEach(System.out::println);
        }
    }

    public List<Person> generatePersons(long limit) {
        List<Person> list = new ArrayList<>();
        for (int i = 0; i < limit; i++) {
            Person person = new Person();
            person.setName(Math.random() + "");
            person.setGender(Math.random() > 0.5 ? Gender.MALE : Gender.FEMALE);
            person.setAge((int)(Math.random() * 100));
            list.add(person);
        }
        return list;
    }

    private void print(Map map) {
        String s = Joiner.on("\n").withKeyValueSeparator("-->").join(map);
        System.out.println("\n" + s);
    }

    private void print(List list) {
        String s = Joiner.on(", ").join(list);
        //System.out.println("\n" + s);
    }

    private void println(List list) {
        System.out.println();
        list.forEach(System.out::println);
    }
}
