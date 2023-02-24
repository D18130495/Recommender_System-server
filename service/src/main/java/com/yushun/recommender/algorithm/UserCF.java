package com.yushun.recommender.algorithm;

import java.io.*;
import java.util.*;

/**
 * <p>
 * UserCF
 * </p>
 *
 * @author yushun zeng
 * @since 2023-2-4
 */

public class UserCF {
    static Map<String,Integer> itemIDMap = new HashMap<>(); // itemId list
    static Map<Integer,String> idToItemMap = new HashMap<>(); // fake id to the item real id
    static Map<String,Integer> userIDMap = new HashMap<>(); // userId list
    static Map<Integer,String> idToUserMap = new HashMap<>(); // fake id to the user real name
    static Map<String,HashMap<String,Double>> userMap = new HashMap<>(); // user rate for the item

    static double[][] simMatrix; // user sim matrix
    static int TOP_K = 12;  // top sim user

    public static List<String> simUserItemListResult(String email, List<UserRatingItemVo> itemList, String type) throws IOException {
        readData(itemList, type);
        userDistance();

        return simUserItemList(email);
    }

    public static List<String> simUserListResult(String email, List<UserRatingItemVo> itemList, String type) throws IOException {
        readData(itemList, type);
        userDistance();

        return simUserList(email);
    }

    public static void readData(List<UserRatingItemVo> itemList, String type) throws IOException {
        BufferedReader bufferedReader = CFUtils.readFile(type);
        String line;
        String[] SplitLine;

        int itemId = 0; // fake item id
        int userId = 0; // fake user id

        // for default
        while((line = bufferedReader.readLine()) != null) {
            SplitLine = line.split(" ");

            // initial item map
            if(!itemIDMap.containsKey(SplitLine[1])) {
                itemIDMap.put(SplitLine[1], itemId);
                idToItemMap.put(itemId, SplitLine[1]);

                itemId ++;
            }

            // initial user map
            if(!userMap.containsKey(SplitLine[0])) {

                userIDMap.put(SplitLine[0], userId);
                idToUserMap.put(userId, SplitLine[0]);

                userId++;

                // user rate map
                HashMap<String, Double> curentUserMap = new HashMap<>();
                // add current user rate to the rate list
                curentUserMap.put(SplitLine[1], Double.parseDouble(SplitLine[2]));
                userMap.put(SplitLine[0], curentUserMap);
            }else { // add additional user rate
                HashMap<String, Double> curentUserMap = userMap.get(SplitLine[0]);
                curentUserMap.put(SplitLine[1], Double.parseDouble(SplitLine[2]));
                userMap.put(SplitLine[0], curentUserMap);
            }
        }

        // for system user
        for(UserRatingItemVo newItem:itemList) {
            if(!itemIDMap.containsKey(newItem.getItemId())) {
                itemIDMap.put(newItem.getItemId(), itemId);
                idToItemMap.put(itemId, newItem.getItemId());

                itemId ++;
            }

            if(!userMap.containsKey(newItem.getUserId())) {

                userIDMap.put(newItem.getUserId(), userId);
                idToUserMap.put(userId, newItem.getUserId());

                userId++;

                HashMap<String, Double> curentUserMap = new HashMap<>();

                curentUserMap.put(newItem.getItemId(), Double.parseDouble(newItem.getRate()));
                userMap.put(newItem.getUserId(), curentUserMap);
            }else {
                HashMap<String, Double> curentUserMap = userMap.get(newItem.getUserId());
                curentUserMap.put(newItem.getItemId(), Double.parseDouble(newItem.getRate()));
                userMap.put(newItem.getUserId(), curentUserMap);
            }
        }
    }

    // user sim
    public static void userDistance() {
        // initial sim matrix
        simMatrix = new double[userMap.size()][userMap.size()];

        // calculate user sim
        for(Map.Entry<String, HashMap<String,Double>> userEntry_1 : userMap.entrySet()) {
            // get user rate list and convert to the rate array
            double[] ratings_1 = new double[itemIDMap.size()]; // initial rate array

            for(Map.Entry<String, Double> itemEntry : userEntry_1.getValue().entrySet()) {
                ratings_1[itemIDMap.get(itemEntry.getKey())] = itemEntry.getValue(); // set the rate
            }

            // other user
            for(Map.Entry<String, HashMap<String,Double>> userEntry_2 : userMap.entrySet()) {
                // skip if the userId less than previous
                if(userIDMap.get(userEntry_2.getKey())>userIDMap.get(userEntry_1.getKey())) {
                    // get user rate list and convert to the rate array
                    double[] ratings_2 = new double[itemIDMap.size()];

                    for(Map.Entry<String, Double> itemEntry : userEntry_2.getValue().entrySet()) {
                        ratings_2[itemIDMap.get(itemEntry.getKey())] = itemEntry.getValue();
                    }

                    // use rate array to find user sim, use euclidean
                    double square_sum = 0;
                    double similarity = 0;

                    for(int i = 0; i < ratings_1.length; i++) {
                        square_sum += Math.pow((ratings_1[i] - ratings_2[i]), 2);
                    }

                    similarity = 1 / (1 + Math.sqrt(square_sum));

                    simMatrix[userIDMap.get(userEntry_1.getKey())][userIDMap.get(userEntry_2.getKey())] = similarity;
                    simMatrix[userIDMap.get(userEntry_2.getKey())][userIDMap.get(userEntry_1.getKey())] = similarity;
                }
            }
        }
    }

    public static List<String> simUserList(String email) {
        // put the user sim in the map. and sort, find the top k sim user
        for(int i = 0; i < userMap.size(); i++) {
            Map<Integer,Double> simMap = new HashMap<>();
            Map<String,Double> preRatingMap = new HashMap<>();

            for(int j = 0; j < userMap.size(); j++) {
                simMap.put(j, simMatrix[i][j]);
            }

            simMap = CFUtils.sortMapByValues(simMap);

            // find top k sim user
            int userCount = 0;

            ArrayList<Integer> simUserList = new ArrayList<>();

            for(Map.Entry<Integer, Double> entry : simMap.entrySet()) {
                if(userCount < 60) {
                    simUserList.add(entry.getKey());
                }

                userCount ++;
            }

            // find this user
            if(!idToUserMap.get(i).equals(email)) continue;

            List<String> simUserListResult = new ArrayList<>();

            for(Integer fakeUserId:simUserList) {
                simUserListResult.add(idToUserMap.get(fakeUserId));
            }

            return simUserListResult;
        }

        return null;
    }


    public static List<String> simUserItemList(String email) {
        // put the user sim in the map. and sort, find the top k sim user
        for(int i = 0; i < userMap.size(); i++) {
            Map<Integer,Double> simMap = new HashMap<>();
            Map<String,Double> preRatingMap = new HashMap<>();

            for(int j = 0;j<userMap.size();j++) {
                simMap.put(j, simMatrix[i][j]);
            }

            simMap = CFUtils.sortMapByValues(simMap);

            int userCount = 0;

            ArrayList<Integer> simUserList = new ArrayList<>();

            for(Map.Entry<Integer, Double> entry : simMap.entrySet()) {
                if(userCount < TOP_K) {
                    simUserList.add(entry.getKey());
                }

                userCount ++;
            }

            // find this user
            if(!idToUserMap.get(i).equals(email)) continue;

            HashSet<String> currentUserSet = new HashSet<>();

            for(Map.Entry<String, Double> entry :userMap.get(idToUserMap.get(i)).entrySet()) {
                currentUserSet.add(entry.getKey());
            }

            // get sim user item list
            // rated item
            HashSet<String> currentFriendSet = new HashSet<String>();

            for(int user : simUserList) {
                for(Map.Entry<String, Double> entry :userMap.get(idToUserMap.get(user)).entrySet()) {
                    currentFriendSet.add(entry.getKey());
                }
            }

            // not rated item
            HashSet<String> unRatingSet = new HashSet<String>();
            for(String item : currentFriendSet) {
                if(!currentUserSet.contains(item)) {
                    unRatingSet.add(item);
                }
            }

            // skip if user not have unrated item
            if(unRatingSet.isEmpty())
                continue;

            // predict the unrated item
            for(String item : unRatingSet) {
                double totalRating = 0;
                double totalSim = 0;
                double preRating = 0;

                // find rated user and rate the item
                for(int user : simUserList) {

                    // get current user rate list
                    for(Map.Entry<String, Double> entry : userMap.get(idToUserMap.get(user)).entrySet()) {
                        if(entry.getKey() == item) {
                            totalRating += entry.getValue() * simMatrix[i][user];
                            totalSim += simMatrix[i][user];
                        }
                    }

                    // get predict rate score
                    preRating = totalRating/totalSim;
                    preRatingMap.put(item, preRating);

                }
            }

            List<String> simUserItemListResult = new ArrayList<>();

            // sort result
            preRatingMap = CFUtils.sortMapByValues(preRatingMap);

            // top k items
            int recCount = 0;

            for(Map.Entry<String, Double> entry : preRatingMap.entrySet()) {
                if(recCount < 60) {
                    simUserItemListResult.add(entry.getKey());
                    recCount ++;
                }
            }

            return simUserItemListResult;
        }
        return null;
    }
}
