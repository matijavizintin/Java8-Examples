package com.test.generators;

import com.google.common.base.Preconditions;
import com.test.beans.Person;
import com.test.flatmap.Many;
import com.test.flatmap.One;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by Matija Vižintin
 * Date: 16. 05. 2015
 * Time: 14.20
 */
public class DataGenerator {
    private static final Logger logger = Logger.getLogger("DataGenerator");

    public static List<String> strings(int stringSize, int number) {

        List<String> strings = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            strings.add(RandomStringUtils.randomAlphabetic(stringSize));
        }
        return strings;
    }

    public static List<Integer> integers(int size) {
        return IntStream.range(1, size).boxed().collect(Collectors.toList());
    }

    public static List<Double> randomDoubles(int size) {
        return IntStream.range(0, size).boxed().map(operand -> ThreadLocalRandom.current().nextDouble()).collect(Collectors.toList());
    }

    public static Map<Integer, String> mapNamesToIntegers(int size) {
        try {
            Path path = new File(DataGenerator.class.getClassLoader().getResource("names.txt").toURI()).toPath();
            List<String> names = Files.readAllLines(path);
            return IntStream.range(0, size).boxed().collect(
                    Collectors.groupingBy(
                            Function.<Integer>identity(), Collectors.reducing("", names::get, (s, s2) -> s2)));
        } catch (URISyntaxException | IOException e) {
            logger.error("Can't read names from file.", e);
            throw new RuntimeException("Check logs.");
        }
    }

    public static List<Person> people(int size) {
        try {
            Path path = new File(DataGenerator.class.getClassLoader().getResource("names.txt").toURI()).toPath();
            List<String> names = Files.readAllLines(path);

            // generate people
            List<Person> people = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                //people.add(new Person(names.get(i), Math.random() > 0.5 ? Gender.MALE : Gender.FEMALE, (int)(Math.random() * 100)));
            }
            return people;
        } catch (URISyntaxException | IOException e) {
            logger.error("Can't read names from file.", e);
            throw new RuntimeException("Check logs.");
        }
    }

    public static List<One> ones(int onesSize, int maniesSize) {
        try {
            Path path = new File(DataGenerator.class.getClassLoader().getResource("names.txt").toURI()).toPath();
            List<String> names = Files.readAllLines(path);

            // generate ones with manies
            int globalNamesCounter = 0;
            List<One> ones = new ArrayList<>();
            for (int i = 0; i < onesSize; i++) {
                One one = new One(names.get(globalNamesCounter++));
                for (int j = 0; j < maniesSize; j++) {
                    Many many = new Many(names.get(globalNamesCounter++), one);
                    one.getManies().add(many);
                }
                ones.add(one);
            }
            return ones;
        } catch (URISyntaxException | IOException e) {
            logger.error("Can't read names from file.", e);
            throw new RuntimeException("Check logs.");
        }
    }

    public static void main(String[] args) {
        URL url = DataGenerator.class.getClassLoader().getResource("names.txt");
        Preconditions.checkNotNull(url);
        File file = new File(url.getPath());

        try (BufferedReader br = Files.newBufferedReader(file.toPath())) {
            String line = null;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            // pass
        }
    }
}
