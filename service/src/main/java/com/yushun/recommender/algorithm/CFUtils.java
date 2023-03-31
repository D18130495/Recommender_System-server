package com.yushun.recommender.algorithm;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

public class CFUtils {
    // read file
    public static BufferedReader readFile(String type) throws IOException {
        String file = "";

        if(type.equals("movie")) {
//            file = "D:\\Desktop\\D18130495\\4th_sem1\\FYP\\Project\\Recommender_System-server\\movie.txt";
            file = "/root/app/back-end/data/movie.txt";
        }else if(type.equals("book")) {
//            file = "D:\\Desktop\\D18130495\\4th_sem1\\FYP\\Project\\Recommender_System-server\\book.txt";
            file = "/root/app/back-end/data/book.txt";
        }

        return new BufferedReader(new InputStreamReader(Files.newInputStream(new File(file).toPath()), StandardCharsets.UTF_8));
    }

    // read sim movie file
    public static BufferedReader readSimMovies() throws IOException {
//        String file = "D:\\Desktop\\D18130495\\4th_sem1\\FYP\\Project\\Recommender_System-server\\itemMovieSimilarity.txt";
        String file = "/root/app/back-end/data/itemMovieSimilarity.txt";

        return new BufferedReader(new InputStreamReader(Files.newInputStream(new File(file).toPath()), StandardCharsets.UTF_8));
    }

    // read sim book file
    public static BufferedReader readSimBooks() throws IOException {
//        String file = "D:\\Desktop\\D18130495\\4th_sem1\\FYP\\Project\\Recommender_System-server\\itemBookSimilarity.txt";
        String file = "/root/app/back-end/data/itemBookSimilarity.txt";

        return new BufferedReader(new InputStreamReader(Files.newInputStream(new File(file).toPath()), StandardCharsets.UTF_8));
    }

    // sort map
    public static <K extends Comparable, V extends Comparable> Map<K, V> sortMapByValues(Map<K, V> aMap) {
        HashMap<K, V> finalOut = new LinkedHashMap<>();
        aMap.entrySet().stream().sorted((p1, p2) -> p2.getValue().compareTo(p1.getValue())).collect(Collectors.toList())
                .forEach(ele -> finalOut.put(ele.getKey(), ele.getValue()));
        return finalOut;
    }

    // get the union number of two set
    public static int interCount(Set<String> set_a, Set<String> set_b) {
        int samObj = 0;

        for(String obj:set_a) {
            if(set_b.contains(obj)) {
                samObj++;
            }
        }

        return samObj;
    }

    // get the union set of two set
    public static Set<Integer> interSet(Set<Integer> set_a, Set<Integer> set_b) {
        Set<Integer> tempSet = new HashSet<>();

        for(Integer obj:set_a) {
            if(set_b.contains(obj)) {
                tempSet.add(obj);
            }
        }

        return tempSet;
    }

    public static Set<String> stringInterSet(Set<String> set_a, Set<String> set_b) {
        Set<String> tempSet = new HashSet<>();

        for(String obj:set_a) {
            if(set_b.contains(obj)) {
                tempSet.add(obj);
            }
        }

        return tempSet;
    }
}
