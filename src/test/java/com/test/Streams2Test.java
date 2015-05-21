package com.test;

import com.google.common.collect.Lists;
import com.test.flatmap.Many;
import com.test.flatmap.One;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Created by Matija Vižintin
 * Date: 15. 05. 2015
 * Time: 14.38
 */
public class Streams2Test {

    @Test
    public void test1() {
        List<Integer> list = generateRandomList(10000);
        list.stream().findFirst().ifPresent(System.out::println);

        Stream.of(1,2,3,4,5,6).findAny().ifPresent(System.out::println);
    }

    @Test
    public void test2() {
        IntStream.range(1, 1000).forEach(System.out::print);
        System.out.println();

        Arrays.stream(generateRandomArray(1000)).map(x -> x * 2 + 1).average().ifPresent(System.out::print);
        System.out.println();
    }

    @Test
    public void test3() {
        Arrays.stream(generateRandomArrayOfStrings(1000)).mapToInt(Integer::parseInt).max().ifPresent(System.out::print);
        System.out.println();
    }

    @Test
    public void test4() {
        // laziness
        System.out.println("Starting lazy example.");
        List<Integer> list = generateRandomList(20);
        list.stream().filter(integer -> {System.out.println("Filter: " + integer); return true;});
        System.out.println("Nothing is printed. No terminal operation.");

        list.stream().filter(integer -> {System.out.println("Filter: " + integer); return true;}).forEach(x -> System.out.println("For each: " + x));
        System.out.println("Result is printed.");
    }

    @Test
    public void test5() {
        // vertical processing
        String[] array = {"a1", "b1", "c1", "d1"};
        boolean b = Stream.of(array).map(s -> {System.out.println("input: " + s); return s.toUpperCase();}).anyMatch(s1 -> s1.startsWith("A"));
        System.out.println("b = " + b);
    }

    @Test
    public void test6() {
        Stream.of("d2", "a2", "b1", "b3", "c").sorted(
                (s1, s2) -> {
                    System.out.printf("sort: %s; %s\n", s1, s2);
                    return s1.compareTo(s2);
                }).filter(
                s -> {
                    System.out.println("filter: " + s);
                    return s.startsWith("a");
                }).map(
                s -> {
                    System.out.println("map: " + s);
                    return s.toUpperCase();
                }).forEach(s -> System.out.println("forEach: " + s));

        // optimizacija
        Stream.of("d2", "a2", "b1", "b3", "c").filter(
                s -> {
                    System.out.println("filter: " + s);
                    return s.startsWith("a");
                }).sorted(
                (s1, s2) -> {
                    System.out.printf("sort: %s; %s\n", s1, s2);
                    return s1.compareTo(s2);
                }).map(
                s -> {
                    System.out.println("map: " + s);
                    return s.toUpperCase();
                }).forEach(s -> System.out.println("forEach: " + s));
    }

    @Test
    public void test7() {
        // stream supplier
        Supplier<Stream<String>> supplier = () -> Stream.of(generateRandomArrayOfStrings(100)).filter(s -> s.startsWith("1"));

        boolean a = supplier.get().anyMatch(s -> true);
        boolean b = supplier.get().noneMatch(s -> false);
        System.out.println("a = " + a);
        System.out.println("b = " + b);
    }

    @Test
    public void test8() {
        List<One> list = generateFlatMapData(10, 10);
        List<Many> manies = list.stream().flatMap(one -> one.getManies().stream()).collect(Collectors.toList());
        System.out.println("list = " + list);
        System.out.println("manies = " + manies);
    }

    private List<Integer> generateRandomList(int size) {
        Integer[] array = new Integer[size];
        Arrays.parallelSetAll(array, value -> ThreadLocalRandom.current().nextInt(size * 100));
        return Lists.newArrayList(array);
    }

    private int[] generateRandomArray(int size) {
        int[] array = new int[size];
        Arrays.parallelSetAll(array, value -> ThreadLocalRandom.current().nextInt(size * 100));
        return array;
    }

    private String[] generateRandomArrayOfStrings(int size) {
        String[] array = new String[size];
        Arrays.parallelSetAll(array, value -> ThreadLocalRandom.current().nextInt(size * 100) + "");
        return array;
    }

    public List<One> generateFlatMapData(int size, int oneToManySize) {
        List<One> ones = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            One one = new One("One: " + ThreadLocalRandom.current().nextInt(1000));
            for (int j = 0; j < oneToManySize; j++) {
                Many many = new Many(one.getName() + " Many: " + ThreadLocalRandom.current().nextInt(1000), one);
                one.getManies().add(many);
            }
            ones.add(one);
        }
        return ones;
    }
}
