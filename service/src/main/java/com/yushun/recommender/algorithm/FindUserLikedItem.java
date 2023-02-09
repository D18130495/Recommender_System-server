package com.yushun.recommender.algorithm;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FindUserLikedItem {
    public static List<String> simUserRatedItemList(List<String> simUserListResult, String type) throws IOException {
        BufferedReader bufferedReader = CFUtils.readFile2(type);
        String line;
        String[] SplitLine;

        List<String> recommendedList = new ArrayList<>();

        while((line = bufferedReader.readLine()) != null) {
            SplitLine = line.split(" ");

            if(simUserListResult.contains(SplitLine[0])) {
                recommendedList.add(SplitLine[1]);
            }
        }

        return recommendedList;
    }
}
