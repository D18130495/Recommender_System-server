package com.yushun.recommender.algorithm;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FindUserLikedItem {
    public List<String> simUserRatedItemList(List<String> simUserListResult, List<UserRatingItemVo> systemUserList, String type) throws IOException {
        BufferedReader bufferedReader = CFUtils.readFile(type);
        String line;
        String[] SplitLine;

        List<String> recommendedList = new ArrayList<>();

        while((line = bufferedReader.readLine()) != null) {
            SplitLine = line.split(" ");

//            boolean check = check(SplitLine[1], systemUserList);

//            if(!check) continue;
            if(simUserListResult.contains(SplitLine[0])) {
                if(!recommendedList.contains(SplitLine[1])) {
                    ArrayList<String> userList = new ArrayList<>();

                    for(UserRatingItemVo userRatingItemVo:systemUserList) {
                        userList.add(userRatingItemVo.getItemId());
                    }

                    if(!userList.contains(SplitLine[1])) {
                        recommendedList.add(SplitLine[1]);
                    }
                }
            }
        }

        return recommendedList;
    }

    // plan B
    public static List<String> itemBased(List<UserRatingItemVo> systemUserList, String type) throws IOException {
        BufferedReader bufferedReader;

        if(type.equals("movie")) {
            bufferedReader = CFUtils.readSimMovies();
        }else {
            bufferedReader = CFUtils.readSimBooks();
        }

        String line;
        String[] SplitLine;

        List<String> recommendedList = new ArrayList<>();

        while((line = bufferedReader.readLine()) != null) {
            SplitLine = line.split(" ");

            ArrayList<String> itemList = new ArrayList<>();

            for(UserRatingItemVo userRatingItemVo:systemUserList) {
                if(Float.parseFloat(userRatingItemVo.getRate()) > 0) {
                    itemList.add(userRatingItemVo.getItemId());
                }
            }

            if(itemList.contains(SplitLine[0])) {
                for(int i = 1; i < 18; i++) {
                    if(!recommendedList.contains(SplitLine[i]) && !itemList.contains(SplitLine[i])) {
                        recommendedList.add(SplitLine[i]);
                    }
                }
            }
        }

        return recommendedList;
    }

    public static boolean check(String itemId, List<UserRatingItemVo> systemUserList) {
        for(UserRatingItemVo item:systemUserList) {
            if(itemId.equals(item.getItemId())) {
                return false;
            }
        }

        return true;
    }
}
