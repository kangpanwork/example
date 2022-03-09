package com.sanri.test.java8;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LambdaMain {
    public static void main(String[] args) throws IOException {
//        Arrays.asList("a","b","c").forEach(e -> System.out.println(e));

        // 显示指定参数类型，只能是包装类型
//        Arrays.asList(1,2,3).forEach((Integer e) -> System.out.println(e));


        final Collection<Streams.Task> tasks = Arrays.asList(
                new Streams.Task( Streams.Status.OPEN, 5 ),
                new Streams.Task( Streams.Status.OPEN, 13 ),
                new Streams.Task( Streams.Status.CLOSED, 8 )
        );

        int sum = tasks.stream().filter(task -> task.status == Streams.Status.OPEN)
                .mapToInt(Streams.Task::getPoints)
                .sum();
        System.out.println(sum);


        Integer totalPoints = tasks.stream()
                .parallel()
                .map(Streams.Task::getPoints)
                .reduce(0, Integer::sum);
        System.out.println(totalPoints);

        Map<Streams.Status, List<Streams.Task>> collect = tasks.stream().collect(Collectors.groupingBy(Streams.Task::getStatus));
        System.out.println(collect);

        // Calculate the weight of each tasks (as percent of total points)
        final Collection< String > result = tasks
                .stream()                                        // Stream< String >
                .mapToInt( Streams.Task::getPoints )             // IntStream
                .asLongStream()                                  // LongStream
                .mapToDouble( points -> (double)points / totalPoints )   // DoubleStream
                .boxed()                                         // Stream< Double >
                .mapToLong( weigth -> ( long )( weigth * 100 ) ) // LongStream
                .mapToObj( percentage -> percentage + "%" )      // Stream< String>
                .collect( Collectors.toList() );                 // List< String >

        System.out.println( result );


        String filename  = "D:\\help\\OneDrive\\devtoolsuse\\chromePlugins\\csdnplugin/content-script.js";
        final Path path = new File( filename ).toPath();
        try( Stream< String > lines = Files.lines( path, StandardCharsets.UTF_8 ) ) {
            lines.onClose( () -> System.out.println("Done!") ).forEach( System.out::println );
        }

        //flatmap
        List<List<String>> flatMap = Arrays.asList(Arrays.asList("a","b","c"),Arrays.asList("d","e","f"),Arrays.asList("m","n","q"));
        List<String> collect1 = flatMap.stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
        System.out.println(collect1);

    }

    public static class Streams  {
        private enum Status {
            OPEN, CLOSED
        };

        private static final class Task {
            private final Status status;
            private final Integer points;

            Task( final Status status, final Integer points ) {
                this.status = status;
                this.points = points;
            }

            public Integer getPoints() {
                return points;
            }

            public Status getStatus() {
                return status;
            }

            @Override
            public String toString() {
                return String.format( "[%s, %d]", status, points );
            }
        }
    }
}
