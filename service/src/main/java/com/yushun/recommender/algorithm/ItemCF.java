package com.yushun.recommender.algorithm;

import java.io.*;
import java.nio.file.Files;
import java.util.*;

/**
 * <p>
 * ItemCF
 * </p>
 *
 * @author yushun zeng
 * @since 2023-2-4
 */

public class ItemCF {
    static Map<String, Integer> itemIDMap; // item id map
    static Map<Integer, String> idToItemMap; // item id to item name map
    static Map<String, HashMap<String, Double>> itemMap; // item all user rating map

    static Map<String, Integer> userIDMap; // user id map
    static Map<Integer, String> idToUserMap; // user id to user name map
    static Map<String, HashMap<String, Double>> userMap; // user rated item list map

    static double[][] simMatrix; // item sim matrix
    // TODO
    static int TOP_K = 60; // select sim item number
    static int TOP_N = 60; // top recommendation number

    public static void initial() {
        itemIDMap = new HashMap<>();
        idToItemMap = new HashMap<>();
        itemMap = new HashMap<>();
        userIDMap = new HashMap<>();
        idToUserMap = new HashMap<>();
        userMap = new HashMap<>();
    }

    public static List<String> simItemResult(String email, List<UserRatingItemVo> itemList, String type) throws IOException {
        initial();

        readData(itemList, type);
        itemSimilarity();

        return recommend(email);
    }

    public static void generateSimilarityItemTxt(List<UserRatingItemVo> itemList, String type) throws IOException {
        initial();

        readData(itemList, type);
        itemSimilarity();
        sameItemList(type);
    }

    // read all the data from MongoDB(default) and MySQL(system)
    public static void readData(List<UserRatingItemVo> itemList, String type) throws IOException {
        BufferedReader bufferedReader = CFUtils.readFile(type);
        String line;
        String[] SplitLine;

        int itemId = 0; // item count
        int userId = 0; // user count

        while((line = bufferedReader.readLine()) != null){
            SplitLine = line.split(" ");

            // MongoDB(default)
            // if not have this item, put the item in the itemMap and itemIdMap
            if(!itemIDMap.containsKey(SplitLine[1])) {
                // store current user rating
                HashMap<String, Double> currentUserMap = new HashMap<>();

                currentUserMap.put(SplitLine[0], Double.parseDouble(SplitLine[2]));

                // put user rating in this item
                itemMap.put(SplitLine[1], currentUserMap);
                itemIDMap.put(SplitLine[1], itemId);
                idToItemMap.put(itemId, SplitLine[1]);

                itemId ++;
            }else {  // if this item exist, update the map
                // get this item with all user rating
                HashMap<String, Double> currentUserMap = itemMap.get(SplitLine[1]);

                // add new user rating
                currentUserMap.put(SplitLine[0], Double.parseDouble(SplitLine[2]));
                itemMap.put(SplitLine[1], currentUserMap);
            }

            // if user not exist in the map, add new user
            if(!userMap.containsKey(SplitLine[0])) {
                // put user fake id and real id in map
                userIDMap.put(SplitLine[0], userId);
                idToUserMap.put(userId, SplitLine[0]);

                userId++;

                // creat new user map and put the rating in
                HashMap<String, Double> currentUserMap = new HashMap<String,Double>();

                // add this new user rating data to the user map
                currentUserMap.put(SplitLine[1], Double.parseDouble(SplitLine[2]));
                userMap.put(SplitLine[0], currentUserMap);
            }else { // if this user exist, get user rating list and add new rating
                HashMap<String, Double> currentUserMap = userMap.get(SplitLine[0]);

                // add new rating data to this exist user map
                currentUserMap.put(SplitLine[1], Double.parseDouble(SplitLine[2]));
                userMap.put(SplitLine[0], currentUserMap);
            }
        }

        // MySQL(system)
        for(UserRatingItemVo newItem:itemList) {
            if(!itemIDMap.containsKey(newItem.getItemId())) {
                HashMap<String, Double> currentUserMap = new HashMap<>();

                currentUserMap.put(newItem.getUserId(), Double.parseDouble(newItem.getRate()));

                itemMap.put(newItem.getItemId(), currentUserMap);
                itemIDMap.put(newItem.getItemId(), itemId);
                idToItemMap.put(itemId, newItem.getItemId());

                itemId ++;
            }else {
                HashMap<String, Double> currentUserMap = itemMap.get(newItem.getItemId());

                currentUserMap.put(newItem.getUserId(), Double.parseDouble(newItem.getRate()));
                itemMap.put(newItem.getItemId(), currentUserMap);
            }

            if(!userMap.containsKey(newItem.getUserId())) {
                userIDMap.put(newItem.getUserId(), userId);
                idToUserMap.put(userId, newItem.getUserId());

                userId++;

                HashMap<String, Double> currentUserMap = new HashMap<>();

                currentUserMap.put(newItem.getItemId(), Double.parseDouble(newItem.getRate()));
                userMap.put(newItem.getUserId(), currentUserMap);
            }else {
                HashMap<String, Double> currentUserMap = userMap.get(newItem.getUserId());

                currentUserMap.put(newItem.getItemId(), Double.parseDouble(newItem.getRate()));
                userMap.put(newItem.getUserId(), currentUserMap);
            }
        }
    }

    public static void itemSimilarity() {
        // initial item similarity matrix
        simMatrix = new double[itemMap.size()][itemMap.size()];

        // loop each item and find similarity by Jaccard
        for(Map.Entry<String, HashMap<String, Double>> itemEntry_1:itemMap.entrySet()) {
            // get all the user who rated this item
            Set<String> ratedUserSet_1 = new HashSet<>();

            for(Map.Entry<String, Double> userEntry:itemEntry_1.getValue().entrySet()) {
                // store all the rating user in the set
                ratedUserSet_1.add(userEntry.getKey());
            }

            // all rating for the first item
            int ratedUserSize_1 = ratedUserSet_1.size();

            // loop other items
            for(Map.Entry<String, HashMap<String, Double>> itemEntry_2:itemMap.entrySet()) {
                // skip the calculated item
                if(itemIDMap.get(itemEntry_2.getKey())>itemIDMap.get(itemEntry_1.getKey())) {
                    // get all the user who rated this item
                    Set<String> ratedUserSet_2 = new HashSet<>();

                    for(Map.Entry<String, Double> userEntry:itemEntry_2.getValue().entrySet()) {
                        ratedUserSet_2.add(userEntry.getKey());
                    }

                    int ratedUserSize_2 = ratedUserSet_2.size(); // all rating for the second item
                    int sameUerSize = CFUtils.interCount(ratedUserSet_1, ratedUserSet_2); // get inter Set number count

                    // calculate item similarity by using Jaccard
                    double similarity = sameUerSize / (Math.sqrt(ratedUserSize_1 * ratedUserSize_2));

                    // put sim in the matrix
                    simMatrix[itemIDMap.get(itemEntry_1.getKey())][itemIDMap.get(itemEntry_2.getKey())] = similarity;
                    simMatrix[itemIDMap.get(itemEntry_2.getKey())][itemIDMap.get(itemEntry_1.getKey())] = similarity;
                }
            }
        }
    }

    public static void sameItemList(String type) throws IOException{
        BufferedWriter bufferedWriter;
        String resultFile;

        if(type.equals("movie")) {
            resultFile = "itemMovieSimilarity.txt";
        }else {
            resultFile = "itemBookSimilarity.txt";
        }

        bufferedWriter = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(new File(resultFile).toPath()),"UTF-8"));

        // calculate nearestItemList
        Map<Integer, HashSet<Integer>> nearestItemMap = findNearestItem();

        // write the same item map to the itemSimilarity.txt
        for(Integer itemId:nearestItemMap.keySet()) {
            bufferedWriter.append(idToItemMap.get(itemId)).append(" ");

            for(Integer simItemId:nearestItemMap.get(itemId)) {
                bufferedWriter.append(idToItemMap.get(simItemId)).append(" ");
            }

            bufferedWriter.newLine();
            bufferedWriter.flush();
        }

        bufferedWriter.flush();
        bufferedWriter.close();
    }

    public static List<String> recommend(String email) {
        Map<Integer, HashSet<Integer>> nearestItemMap = findNearestItem();

        for(int i = 0; i < userMap.size(); i++) {
            // find current user
            if(!idToUserMap.get(i).equals(email)) continue;

            // get current user all rated item
            HashSet<Integer> currentUserSet = new HashSet<>();
            Map<String,Double> preRatingMap = new HashMap<>();

            for(Map.Entry<String, Double> entry:userMap.get(idToUserMap.get(i)).entrySet()) {
                // put all the user rated item fake id in the set
                currentUserSet.add(itemIDMap.get(entry.getKey()));
            }

            // each item
            for(int j = 0; j < itemMap.size(); j++) {
                double preRating = 0;
                double sumSim = 0;

                // if current user already rated current item, skip
                if(currentUserSet.contains(j))
                    continue;

                // find current item sim list and curren user rated list intersection
                Set<Integer> interSet = CFUtils.interSet(currentUserSet, nearestItemMap.get(j));

                // have intersection, start predict rate
                if(!interSet.isEmpty()) {
                    for(int item:interSet) {
                        // weighted arithmetic mean
                        sumSim += simMatrix[j][item];
                        preRating += simMatrix[j][item] * userMap.get(idToUserMap.get(i)).get(idToItemMap.get(item));
                    }

                    if(sumSim != 0) {
                        preRating = preRating / sumSim;
                    }else { // if same sum is 0, the predict rate is 0
                        preRating = 0;
                    }
                }else { // if no intersection, the predict rate is 0
                    preRating = 0;
                }

                preRatingMap.put(idToItemMap.get(j), preRating);
            }

            // sort the predict rate
            preRatingMap = CFUtils.sortMapByValues(preRatingMap);

            // result list
            List<String> simUserItemListResult = new ArrayList<>();

            // recommend top N item
            int recommendationCount = 0;

            for(Map.Entry<String, Double> entry:preRatingMap.entrySet()) {
                if(recommendationCount < TOP_N) {
                    simUserItemListResult.add(entry.getKey());
                    recommendationCount ++;
                }
            }

            return simUserItemListResult;
        }

        return null;
    }

    public static Map<Integer, HashSet<Integer>> findNearestItem() {
        // base on the item same matrix to find top n same item
        Map<Integer, HashSet<Integer>> nearestItemMap = new HashMap<>();

        for(int i = 0; i < itemMap.size(); i++) {
            Map<Integer, Double> simMap = new HashMap<>();

            for(int j = 0; j < itemMap.size(); j++) {
                simMap.put(j, simMatrix[i][j]);
            }

            // sort the item map
            simMap = CFUtils.sortMapByValues(simMap);

            int simItemCount = 0;

            HashSet<Integer> nearestItemSet = new HashSet<>();

            for(Map.Entry<Integer, Double> entry:simMap.entrySet()) {
                if(simItemCount < TOP_K) {
                    nearestItemSet.add(entry.getKey()); // get same item ID
                    simItemCount++;
                }else {
                    break;
                }
            }

            // put same item in the map
            nearestItemMap.put(i, nearestItemSet);
        }

        return nearestItemMap;
    }
}
