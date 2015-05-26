package com.test.generators;

import com.test.beans.Gender;
import com.test.beans.Person;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
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
        return IntStream.range(1, 10).boxed().collect(Collectors.toList());
    }

    public static List<Person> people(int size) {
        try {
            Path path = new File(DataGenerator.class.getClassLoader().getResource("names.txt").toURI()).toPath();
            List<String> names = Files.readAllLines(path);

            // generate people
            List<Person> people = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                people.add(new Person(names.get(i), Math.random() > 0.5 ? Gender.MALE : Gender.FEMALE, (int)(Math.random() * 100)));
            }
            return people;
        } catch (URISyntaxException | IOException e) {
            logger.error("Can't read names from file.", e);
            throw new RuntimeException("Check logs.");
        }
    }

    public static void main(String[] args) {
        //DataGenerator.class.getClassLoader().getResource("names.txt");
    }
}
