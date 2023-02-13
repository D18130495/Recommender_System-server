package com.yushun.recommender.algorithm;

import java.io.*;
import java.nio.file.Files;
import java.util.*;

import static com.yushun.recommender.algorithm.CFUtils.*;

/**
 * <p>
 * Item-Based CF
 * </p>
 *
 * @author yushun zeng
 * @since 2023-1-29
 */

public class ItemCF {
    static Map<String, Integer> itemIDMap = new HashMap<>();// item id map
    static Map<Integer, String> idToItemMap = new HashMap<>();// item id to item name map
    static Map<String, HashMap<String, Double>> itemMap = new HashMap<>(); // item all user rating map

    static Map<String, Integer> userIDMap = new HashMap<>();// user id map
    static Map<Integer, String> idToUserMap = new HashMap<>();// user id to user name map
    static Map<String, HashMap<String, Double>> userMap = new HashMap<>(); // user rated item list map

    static double[][] simMatrix; // item sim matrix
    static int TOP_K = 30; // select sim item number
    static int TOP_N = 20; // top recommendation number

    //
    public static List<String> simUserItemListResult(String email, List<UserRatingItemVo> itemList, String type) throws IOException {
        readData(itemList, type);

        return recommend(email);
    }

    // use for scheduled task, to calculate the item similarity
    public static void generateSimilarityItemTxt(List<UserRatingItemVo> itemList, String type) throws IOException {
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

                HashMap<String, Double> curentUserMap = new HashMap<String,Double>();

                curentUserMap.put(newItem.getItemId(), Double.parseDouble(newItem.getRate()));
                userMap.put(newItem.getUserId(), curentUserMap);
            }else {
                HashMap<String, Double> curentUserMap = userMap.get(newItem.getUserId());

                curentUserMap.put(newItem.getItemId(), Double.parseDouble(newItem.getRate()));
                userMap.put(newItem.getUserId(), curentUserMap);
            }
        }
    }

    public static void itemSimilarity() {
        // initial item similarity matrix
        simMatrix = new double[itemMap.size()][itemMap.size()];

        int itemCount = 0;

        // loop each item and find similarity by Jaccard
        for(Map.Entry<String, HashMap<String, Double>> itemEntry1 : itemMap.entrySet()) {

            // get all the user who rated this item
            Set<String> ratedUserSet1 = new HashSet<>();

            for(Map.Entry<String, Double> userEntry : itemEntry1.getValue().entrySet()) {
                // store all the rating user in the set
                ratedUserSet1.add(userEntry.getKey());
            }

            int ratedUserSize1 = ratedUserSet1.size(); // all rating for the first item

            // loop other items
            for(Map.Entry<String, HashMap<String, Double>> itemEntry2 : itemMap.entrySet()) {
                // skip the calculated item
                if(itemIDMap.get(itemEntry2.getKey()) > itemIDMap.get(itemEntry1.getKey())) {
                    // get all the user who rated this item
                    Set<String> ratedUserSet2 = new HashSet<>();

                    for(Map.Entry<String, Double> userEntry : itemEntry2.getValue().entrySet()) {
                        ratedUserSet2.add(userEntry.getKey());
                    }

                    int ratedUserSize2 = ratedUserSet2.size(); // all rating for the second item

                    // calculate item similarity by using Jaccard
                    int sameUerSize = CFUtils.interCount(ratedUserSet1, ratedUserSet2); // get inter Set number count

                    double similarity = sameUerSize / (ratedUserSize1 + ratedUserSize2 - sameUerSize);
//                    double similarity = sameUerSize / (Math.sqrt(ratedUserSize1 * ratedUserSize2));
                    // put sim in the matrix
                    simMatrix[itemIDMap.get(itemEntry1.getKey())][itemIDMap.get(itemEntry2.getKey())] = similarity;
                    simMatrix[itemIDMap.get(itemEntry2.getKey())][itemIDMap.get(itemEntry1.getKey())] = similarity;
                }
            }

            itemCount++;
        }
    }

    public static void sameItemList(String type) throws IOException{
        BufferedWriter bufferedWriter;

        if(type.equals("movie")) {
            String resultFile = "itemMovieSimilarity.txt";
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(new File(resultFile).toPath()),"UTF-8"));
        }else {
            String resultFile = "itemBookSimilarity.txt";
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(new File(resultFile).toPath()),"UTF-8"));
        }

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

            for(Map.Entry<Integer, Double> entry : simMap.entrySet()) {
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

        // write the same item map to the itemSimilarity.txt
        for(Integer itemId:nearestItemMap.keySet()) {
            bufferedWriter.append(idToItemMap.get(itemId) + " ");

            for(Integer simItemId:nearestItemMap.get(itemId)) {
                bufferedWriter.append(idToItemMap.get(simItemId) + " ");
            }

            bufferedWriter.newLine();
            bufferedWriter.flush();
        }

        bufferedWriter.flush();
        bufferedWriter.close();
    }

    public static List<String> recommend(String email) throws IOException{
        BufferedReader bufferedReader = CFUtils.readSimMovies();

        String line;
        String[] SplitLine;

        Map<String, HashSet<String>> nearestItemMap = new HashMap<>();


        while((line = bufferedReader.readLine()) != null) {
            SplitLine = line.split(" ");

            HashSet<String> nearestItemSet = new HashSet<>();

            for(int i = 1; i <= 30; i++) {
                nearestItemSet.add(SplitLine[i]);
            }

            nearestItemMap.put(SplitLine[0], nearestItemSet);
        }

        //循环每个用户，循环每个产品,计算用户对没有买过的产品的打分，取TOP_N得分最高的产品进行推荐
        for(int i = 0; i < userMap.size(); i++) {
            if(!idToUserMap.get(i).equals(email)) continue;

            // get current user all rated item
            HashSet<String> currentUserSet = new HashSet<>();
            Map<String, Double> preRatingMap = new HashMap<>();

            for(Map.Entry<String, Double> entry : userMap.get(idToUserMap.get(i)).entrySet()) {
                currentUserSet.add(idToItemMap.get(Integer.parseInt(entry.getKey()))); // put rated item to the list by item real id
            }

            // loop each item in the all item list
            for(int j = 0; j < itemMap.size(); j++) {
                double preRating = 0;

                // if this user rated this item skip
                if(currentUserSet.contains(idToItemMap.get(j)))
                    continue;

                // find if this item contain in the user rated list
                Set<String> interSet = stringInterSet(currentUserSet, nearestItemMap.get(idToItemMap.get(j)));//获取当前用户的购买列表与产品相似品的交集

                // if interSet is empty rate as 0
                if(!interSet.isEmpty()) {
                    preRating = 0;

                    for(Map.Entry<String, Double> entry : itemMap.get(idToItemMap.get(j)).entrySet()) {
                        preRating += entry.getValue();
                    }

                    preRating = preRating / itemMap.get(idToItemMap.get(j)).entrySet().size();
                    preRating = interSet.size() * preRating;
                }else
                    preRating = 0;

                preRatingMap.put(idToItemMap.get(j), preRating);
            }

            preRatingMap = sortMapByValues(preRatingMap);

            if(!preRatingMap.isEmpty()) {
//                bfw.append(idToUserMap.get(i)+":");
            }

            List<String> simUserItemListResult = new ArrayList<>();

            //推荐TOP_N个产品
            int recCount = 0;
            for(Map.Entry<String, Double> entry : preRatingMap.entrySet()) {
                if(recCount < TOP_N) {
                    simUserItemListResult.add(entry.getKey());
                    recCount ++;
                }
            }

            return simUserItemListResult;
        }

        return null;
    }
}
