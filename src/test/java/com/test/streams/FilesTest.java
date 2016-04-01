package com.test.streams;

import com.test.timed.LoggingTimedTest;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.StringJoiner;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Matija Vižintin
 * Date: 28. 08. 2015
 * Time: 15.08
 */
public class FilesTest extends LoggingTimedTest {

    /**
     * This test shows how can streams be used to manipulate files. Note that streams from files have to be closed (or auto-closed with
     * try-with-resources).
     *
     * @throws IOException
     */
    @Test
    public void manipulateFiles() throws IOException {

        // list files
        try (Stream<Path> stream = Files.list(Paths.get(""))) {
            String joined = stream
                    .map(Path::toString)                    // map to string
                    .filter(s -> !s.startsWith("."))        // skip invisible
                    .sorted()                               // sort
                    .collect(Collectors.joining(";"));      // join
            System.out.printf("All files joined: %s\n\n", joined);
        }

        // find file
        try (Stream<Path> stream = Files.find(Paths.get(""), 1, (path, basicFileAttributes) -> path.toString().contains("src"))) {
            String joined = stream.map(Path::toString).collect(Collectors.joining());
            System.out.printf("All files filtered by 'src': %s\n\n", joined);
        }

        // walk through files
        try (Stream<Path> stream = Files.walk(Paths.get(""), 3)) {
            Collector<Path, StringJoiner, String> customCollector = Collector.of(
                    () -> new StringJoiner("\n"),
                    (stringJoiner, path) -> stringJoiner.add(path.toString().contains(".") ? " --> " + path.toString() : path.toString()),
                    StringJoiner::merge,
                    StringJoiner::toString);
            String joined = stream.filter(path -> !path.toString().startsWith(".")).collect(customCollector);
            System.out.printf("All files to depth 3: %s\n\n", joined);
        }

        // read file
        try (Stream<String> stream = Files.lines(Paths.get("build.gradle"))) {
            System.out.println("Reading from pom files lines that don't start with '<'");
            stream.map(String::trim).filter(s -> !s.isEmpty() && !s.startsWith("<")).forEach(System.out::println);
        }
    }
}
