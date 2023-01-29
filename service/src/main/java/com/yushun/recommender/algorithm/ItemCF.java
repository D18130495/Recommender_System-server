//package com.yushun.recommender.algorithm;
//
//import com.yushun.recommender.repository.MovieRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.io.IOException;
//import java.util.*;
//import java.util.stream.Collectors;
//
///**
// * <p>
// * Item-Based CF
// * </p>
// *
// * @author yushun zeng
// * @since 2023-1-29
// */
//
//public class ItemCF {
//    static Map<String, Integer> itemIDMap = new HashMap<String, Integer>(); // item id
//    static Map<Integer, String> idToItemMap = new HashMap<Integer, String>(); // item id to item name
//    static Map<String, HashMap<String, Double>> itemMap = new HashMap<>(); // all the user rate for each item
//
//    static Map<String, Integer> userIDMap = new HashMap<String, Integer>(); // user id list
//    static Map<Integer, String> idToUserMap = new HashMap<Integer, String>(); // user id to username
//    static Map<String, HashMap<String, Double>> userMap = new HashMap<String, HashMap<String, Double>>(); // user rate for all the item
//
//    static double[][] simMatrix; // item similar matrix
//    static int TOP_K = 25;  // select similar item
//    static int TOP_N = 20;  // recommended item number
//
//    @Autowired
//    private MovieRepository movieRepository;
//
//    // 读取用户UI交互
//    public static void readUI() throws IOException{
//        int itemId = 0; // item count
//        int userId = 0; // user count
//
//        while((line = bfr_ui.readLine()) != null){
//
//
//            //如果不包含当前产品，存入产品map以及产品idmap中
//            if(!itemIDMap.containsKey(SplitLine[1])) {
//                HashMap<String, Double> currentUserMap = new HashMap<>();//存入当前的用户评分
//                currentUserMap.put(SplitLine[0], Double.parseDouble(SplitLine[2]));	//用户-评分
//                itemMap.put(SplitLine[1], currentUserMap); //在itemMap中存入产品-评分
//                itemIDMap.put(SplitLine[1], itemId);
//                idToItemMap.put(itemId, SplitLine[1]);
//                itemId ++;
//            }else {  //如果已经存在，进行Map更新
//                HashMap<String, Double> currentUserMap = itemMap.get(SplitLine[1]); //获取已有产品所包含的评分
//                currentUserMap.put(SplitLine[0], Double.parseDouble(SplitLine[2]));//加入新的用户-评分
//                itemMap.put(SplitLine[1], currentUserMap);
//            }
//
//            //如果不包含当前的用户，存入map中
//            if(!userMap.containsKey(SplitLine[0])) {
//
//                userIDMap.put(SplitLine[0], userId);
//                idToUserMap.put(userId, SplitLine[0]);
//
//                userId++;
//                //新建Map用于存储当前用户的评分列表
//                HashMap<String, Double> curentUserMap = new HashMap<String,Double>();
//                //将当前用户评分加入当前评分列表中
//                curentUserMap.put(SplitLine[1], Double.parseDouble(SplitLine[2]));
//                userMap.put(SplitLine[0], curentUserMap);
//            }else { //如果已存在当前用户，将该用户先前的评分拿出来，再加入新的评分
//                HashMap<String, Double> curentUserMap = userMap.get(SplitLine[0]);
//                curentUserMap.put(SplitLine[1], Double.parseDouble(SplitLine[2]));
//                userMap.put(SplitLine[0], curentUserMap);
//            }
//        }
//    }
//
//    //获取产品之间的相似性
//    public static void item_similarity() {
//        //初始化用户相似矩阵
//        simMatrix = new double[itemMap.size()][itemMap.size()];
//        int itemCount = 0;
//        //循环每个产品计算相似性:Jaccard 相似性
//        for(Map.Entry<String, HashMap<String, Double>> itemEntry_1 : itemMap.entrySet()) {
//            System.out.println("计算"+itemCount);
//            //获取为当前产品评分的所有用户
//            Set<String> ratedUserSet_1 = new HashSet<>();
//            for(Map.Entry<String, Double> userEntry : itemEntry_1.getValue().entrySet()) {
//                //将已评分用户存入set集合中
//                ratedUserSet_1.add(userEntry.getKey());
//            }
//
//            int ratedUserSize_1 = ratedUserSet_1.size();//第一个产品所有评论数
//
//            //循环其他产品
//            for(Map.Entry<String, HashMap<String, Double>> itemEntry_2 : itemMap.entrySet()) {
//                //首先判断第二个产品的id是否大于第一个，是的话再进行计算，避免重复计算
//                if(itemIDMap.get(itemEntry_2.getKey())>itemIDMap.get(itemEntry_1.getKey())) {
//                    //同样获取为当前产品评分的所有用户
//                    Set<String> ratedUserSet_2 = new HashSet<>();
//                    for(Map.Entry<String, Double> userEntry : itemEntry_2.getValue().entrySet()) {
//                        ratedUserSet_2.add(userEntry.getKey());
//                    }
//                    //通过jaccard相似度计算产品相似度
//
//                    int ratedUserSize_2 = ratedUserSet_2.size();//第二个产品所有评论数
//                    int sameUerSize = interCount(ratedUserSet_1,ratedUserSet_2); //取两个集合的交集的数量
//
//                    double similarity = sameUerSize/(Math.sqrt(ratedUserSize_1*ratedUserSize_2));
//                    //把相似性存入相似矩阵中
//                    simMatrix[itemIDMap.get(itemEntry_1.getKey())][itemIDMap.get(itemEntry_2.getKey())] = similarity;
//                    simMatrix[itemIDMap.get(itemEntry_2.getKey())][itemIDMap.get(itemEntry_1.getKey())] = similarity;
//                }
//            }
//            itemCount++;
//
////			for (int i = 0; i < simMatrix.length; i++) {
////				for (int j = 0; j < simMatrix.length; j++) {
////					System.out.print(simMatrix[i][j]+" ");
////				}
////				System.out.println();
////			}
//        }
//    }
//
//    //根据产品的相似性进行推荐
//    public static void recommend() throws IOException{
//        String resultFile = "data//topicAttack_101_ItemCF_result.txt";
//        BufferedWriter bfw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(resultFile)),"UTF-8"));
//
//        //根据item相似度获取每个item最相似的TOP_K个产品
//        Map<Integer, HashSet<Integer>> nearestItemMap = new HashMap<>();
//
//        for(int i = 0;i<itemMap.size();i++) {
//            Map<Integer, Double> simMap = new HashMap<>();
//            for(int j = 0;j<itemMap.size();j++) {
//                simMap.put(j,simMatrix[i][j]);
//            }
//
//            //对产品相似性进行排序
//            simMap = sortMapByValues(simMap);
//
//            int simItemCount = 0;
//            HashSet<Integer> nearestItemSet = new HashSet<>();
//            for(Map.Entry<Integer, Double> entry : simMap.entrySet()) {
//                if(simItemCount<TOP_K) {
//                    nearestItemSet.add(entry.getKey()); //获取相似itemID存入集合中
//                    simItemCount++;
//                }else
//                    break;
//            }
//            //相似物品结果存入map中
//            nearestItemMap.put(i,nearestItemSet);
//        }
//
//        //循环每个用户，循环每个产品,计算用户对没有买过的产品的打分，取TOP_N得分最高的产品进行推荐
//        for(int i = 0;i<userMap.size();i++) {
//            System.out.println("为用户"+i+"推荐");
//            //获取当前用户所有评论过的产品
//            HashSet<Integer> currentUserSet = new HashSet<>();
//            Map<String,Double> preRatingMap = new HashMap<String,Double>();
//
//            for(Map.Entry<String, Double> entry :userMap.get(idToUserMap.get(i)).entrySet()) {
//                currentUserSet.add(itemIDMap.get(entry.getKey())); //将该用户评论过的产品以产品id的形式存入集合中
//            }
//
//            //循环每个产品
//            for(int j = 0;j<itemMap.size();j++) {
//                double preRating = 0;
//                double sumSim = 0;
//
//                //首先判断用户购买的列表中是否包含当前商品，如果包含直接跳过
//                if(currentUserSet.contains(j))
//                    continue;
//
//                //判断当前产品的近邻中是否包含这个产品
//                Set<Integer> interSet = interSet(currentUserSet, nearestItemMap.get(j));//获取当前用户的购买列表与产品相似品的交集
//
//                //如果交集为空，则该产品预测评分为0
//                if(!interSet.isEmpty()) {
//                    for(int item :interSet) {
//                        sumSim += simMatrix[j][item];
//                        preRating += simMatrix[j][item]* userMap.get(idToUserMap.get(i)).get(idToItemMap.get(item));
//
//                    }
//
//                    if(sumSim != 0) {
//                        preRating = preRating/sumSim; //如果相似性之和不为0计算得分，否则得分为0
//                    }else
//                        preRating = 0;
//                }else  //如果交集为空的话，直接评分为0
//                    preRating = 0;
//                preRatingMap.put(idToItemMap.get(j), preRating);
//            }
//            preRatingMap = sortMapByValues(preRatingMap);
//
//            if(!preRatingMap.isEmpty()) {
//                bfw.append(idToUserMap.get(i)+":");
//            }
//
//            //推荐TOP_N个产品
//            int recCount = 0;
//            for(Map.Entry<String, Double> entry : preRatingMap.entrySet()) {
//                if(recCount < TOP_N) {
//                    bfw.append(entry.getKey() + " ");
//                    recCount ++;
//                    bfw.flush();
//                }
//            }
//            bfw.newLine();
//            bfw.flush();
//        }
//        bfw.flush();
//        bfw.close();
//    }
//
//    // get two sets union
//    public static int interCount(Set<String> set_a,Set<String> set_b) {
//        int samObj = 0;
//        for(Object obj:set_a) {
//            if(set_b.contains(obj))
//                samObj++;
//        }
//        return samObj;
//    }
//
//    // get two sets union number
//    public static Set<Integer> interSet(Set<Integer> set_a, Set<Integer> set_b) {
//        Set<Integer> tempSet = new HashSet<>();
//        for(Object obj:set_a) {
//            if(set_b.contains(obj))
//                tempSet.add((Integer) obj);
//        }
//        return tempSet;
//    }
//
//    // sort map in descending order
//    public static <K extends Comparable, V extends Comparable> Map<K, V> sortMapByValues(Map<K, V> aMap) {
//        HashMap<K, V> finalOut = new LinkedHashMap<>();
//        aMap.entrySet().stream().sorted((p1, p2) -> p2.getValue().compareTo(p1.getValue())).collect(Collectors.toList())
//                .forEach(ele -> finalOut.put(ele.getKey(), ele.getValue()));
//        return finalOut;
//    }
//
//    public static void main(String[] args) throws IOException {
//        readUI();
//        item_similarity();
//        recommend();
//    }
//}
