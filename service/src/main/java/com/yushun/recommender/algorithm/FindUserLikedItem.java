package com.yushun.recommender.algorithm;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class FindUserLikedItem {
    int TOP_K = 20;  // top sim user

    public List<String> simUserRatedItemList(List<String> simUserListResult, String type) throws IOException {
        String file = "";

        if(type.equals("movie")) {
            file = "movie2.txt";
        }else if(type.equals("book")) {
            file = "book2.txt";
        }

        BufferedReader bfr_ui = new BufferedReader(new InputStreamReader(Files.newInputStream(new File(file).toPath()), StandardCharsets.UTF_8));
        List<String> recommendedList = new ArrayList<>();
        String line;
        String[] SplitLine;

        while((line = bfr_ui.readLine()) != null) {
            SplitLine = line.split(" ");

            if(simUserListResult.contains(SplitLine[0])) {
                recommendedList.add(SplitLine[1]);
            }
        }

        return recommendedList;
    }
}
