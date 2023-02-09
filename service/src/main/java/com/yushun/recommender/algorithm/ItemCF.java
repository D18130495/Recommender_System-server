package com.yushun.recommender.algorithm;

import java.io.*;
import java.util.*;

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
    static int TOP_K = 60; // select sim item number
    static int TOP_N = 20; // top recommendation number

    //
    public static List<String> simUserItemListResult(String email, List<UserRatingItemVo> itemList, String type) throws IOException {
        readData(itemList, type);
        item_similarity();
        simItemList(email);

        return null;
    }

    // use for scheduled task, to calculate the item similarity
    public static void generateSimilarityItemTxt(String email, List<UserRatingItemVo> itemList, String type) throws IOException {
        readData(itemList, type);
        item_similarity();
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

    public static void item_similarity() {
        //初始化用户相似矩阵
        simMatrix = new double[itemMap.size()][itemMap.size()];

        int itemCount = 0;

        //循环每个产品计算相似性:Jaccard 相似性
        for(Map.Entry<String, HashMap<String, Double>> itemEntry_1 : itemMap.entrySet()) {

            //获取为当前产品评分的所有用户
            Set<String> ratedUserSet_1 = new HashSet<>();
            for(Map.Entry<String, Double> userEntry : itemEntry_1.getValue().entrySet()) {
                //将已评分用户存入set集合中
                ratedUserSet_1.add(userEntry.getKey());
            }

            int ratedUserSize_1 = ratedUserSet_1.size();//第一个产品所有评论数

            //循环其他产品
            for(Map.Entry<String, HashMap<String, Double>> itemEntry_2 : itemMap.entrySet()) {
                //首先判断第二个产品的id是否大于第一个，是的话再进行计算，避免重复计算
                if(itemIDMap.get(itemEntry_2.getKey())>itemIDMap.get(itemEntry_1.getKey())) {
                    //同样获取为当前产品评分的所有用户
                    Set<String> ratedUserSet_2 = new HashSet<>();
                    for(Map.Entry<String, Double> userEntry : itemEntry_2.getValue().entrySet()) {
                        ratedUserSet_2.add(userEntry.getKey());
                    }
                    //通过jaccard相似度计算产品相似度

                    int ratedUserSize_2 = ratedUserSet_2.size(); //第二个产品所有评论数
                    int sameUerSize = CFUtils.interCount(ratedUserSet_1, ratedUserSet_2); //取两个集合的交集的数量

                    double similarity = sameUerSize / (ratedUserSize_1 + ratedUserSize_2 - sameUerSize);
//                    double similarity = sameUerSize/(Math.sqrt(ratedUserSize_1 * ratedUserSize_2));
                    //把相似性存入相似矩阵中
                    simMatrix[itemIDMap.get(itemEntry_1.getKey())][itemIDMap.get(itemEntry_2.getKey())] = similarity;
                    simMatrix[itemIDMap.get(itemEntry_2.getKey())][itemIDMap.get(itemEntry_1.getKey())] = similarity;
                }
            }

            itemCount++;
        }
    }

    //根据产品的相似性进行推荐
    public static void simItemList(String email) throws IOException{
        String resultFile = "item_similarity.txt";
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(resultFile)),"UTF-8"));

        //根据item相似度获取每个item最相似的TOP_K个产品
        Map<Integer, HashSet<Integer>> nearestItemMap = new HashMap<>();

        for(int i = 0; i < itemMap.size(); i++) {
            Map<Integer, Double> simMap = new HashMap<>();
            for(int j = 0; j<itemMap.size(); j++) {
                simMap.put(j, simMatrix[i][j]);
            }

            //对产品相似性进行排序
            simMap = CFUtils.sortMapByValues(simMap);

            int simItemCount = 0;
            HashSet<Integer> nearestItemSet = new HashSet<>();
            for(Map.Entry<Integer, Double> entry : simMap.entrySet()) {
                if(simItemCount<TOP_K) {
                    nearestItemSet.add(entry.getKey()); //获取相似itemID存入集合中
                    simItemCount++;
                }else
                    break;
            }
            //相似物品结果存入map中
            nearestItemMap.put(i, nearestItemSet);
        }

        for(Integer itemId:nearestItemMap.keySet()) {
            bufferedWriter.append(idToItemMap.get(itemId) + ":");

            for(Integer simItemId:nearestItemMap.get(itemId)) {
                bufferedWriter.append(simItemId + " ");
                bufferedWriter.flush();
            }
            bufferedWriter.newLine();
            bufferedWriter.flush();
        }

        bufferedWriter.flush();
        bufferedWriter.close();

        System.out.println("ok");


        //循环每个用户，循环每个产品,计算用户对没有买过的产品的打分，取TOP_N得分最高的产品进行推荐
        for(int i = 0; i < userMap.size(); i++) {
            //获取当前用户所有评论过的产品
            HashSet<Integer> currentUserSet = new HashSet<>();
            Map<String,Double> preRatingMap = new HashMap<String,Double>();

            for(Map.Entry<String, Double> entry :userMap.get(idToUserMap.get(i)).entrySet()) {
                currentUserSet.add(itemIDMap.get(entry.getKey())); //将该用户评论过的产品以产品id的形式存入集合中
            }

            //循环每个产品
            for(int j = 0;j<itemMap.size();j++) {
                double preRating = 0;
                double sumSim = 0;

                //首先判断用户购买的列表中是否包含当前商品，如果包含直接跳过
                if(currentUserSet.contains(j))
                    continue;

                //判断当前产品的近邻中是否包含这个产品
                Set<Integer> interSet = CFUtils.interSet(currentUserSet, nearestItemMap.get(j));//获取当前用户的购买列表与产品相似品的交集

                //如果交集为空，则该产品预测评分为0
                if(!interSet.isEmpty()) {
                    for(int item :interSet) {
                        sumSim += simMatrix[j][item];
                        preRating += simMatrix[j][item]* userMap.get(idToUserMap.get(i)).get(idToItemMap.get(item));

                    }

                    if(sumSim != 0) {
                        preRating = preRating/sumSim; //如果相似性之和不为0计算得分，否则得分为0
                    }else
                        preRating = 0;
                }else  //如果交集为空的话，直接评分为0
                    preRating = 0;
                preRatingMap.put(idToItemMap.get(j), preRating);
            }
            preRatingMap = CFUtils.sortMapByValues(preRatingMap);

            if(!idToUserMap.get(i).equals(email)) continue;

            //推荐TOP_N个产品
            int recCount = 0;

            for(Map.Entry<String, Double> entry : preRatingMap.entrySet()) {
                if(recCount < TOP_N) {
                    System.out.println(entry.getKey());
                    recCount ++;
                }
            }
        }
    }
}
