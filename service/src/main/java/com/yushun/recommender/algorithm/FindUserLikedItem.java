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
}
