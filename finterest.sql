/*
 Navicat Premium Data Transfer

 Source Server         : recommender_system
 Source Server Type    : MySQL
 Source Server Version : 50740
 Source Host           : 63.35.221.13:3306
 Source Schema         : recommender_system

 Target Server Type    : MySQL
 Target Server Version : 50740
 File Encoding         : 65001

 Date: 31/03/2023 18:36:49
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for com_user
-- ----------------------------
DROP TABLE IF EXISTS `com_user`;
CREATE TABLE `com_user`  (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'User id',
  `username` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'User name',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'User password',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'User email',
  `avatar` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'User avatar',
  `policy` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'T represent accept, F represent reject, U represent Undefined',
  `type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'S represent system login, G represent google login',
  `role` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'Role of the user',
  `create_time` timestamp NULL DEFAULT NULL COMMENT 'User created time',
  `update_time` timestamp NULL DEFAULT NULL COMMENT 'Last modified time',
  `is_deleted` tinyint(1) NULL DEFAULT NULL COMMENT 'Is deleted',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 64 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for ui_book_favourite
-- ----------------------------
DROP TABLE IF EXISTS `ui_book_favourite`;
CREATE TABLE `ui_book_favourite`  (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'Book user favourite id',
  `isbn` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'MongoDB isbn',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'User email',
  `favourite` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'T represent like, N represent normal, F represent unlike',
  `create_time` timestamp NULL DEFAULT NULL COMMENT 'User created time',
  `update_time` timestamp NULL DEFAULT NULL COMMENT 'Last modified time',
  `is_deleted` tinyint(1) NULL DEFAULT NULL COMMENT 'Is deleted',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 801 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for ui_book_rating
-- ----------------------------
DROP TABLE IF EXISTS `ui_book_rating`;
CREATE TABLE `ui_book_rating`  (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'Book user rating id',
  `isbn` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'MongoDB book ISBN',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'User email',
  `rating` float NULL DEFAULT NULL COMMENT 'Rating score',
  `create_time` timestamp NULL DEFAULT NULL COMMENT 'User created time',
  `update_time` timestamp NULL DEFAULT NULL COMMENT 'Last modified time',
  `is_deleted` tinyint(1) NOT NULL COMMENT 'Is deleted',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 209 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for ui_movie_favourite
-- ----------------------------
DROP TABLE IF EXISTS `ui_movie_favourite`;
CREATE TABLE `ui_movie_favourite`  (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'Movie user favourite id',
  `movieId` int(10) NULL DEFAULT NULL COMMENT 'MongoDB movie ID',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'User email',
  `favourite` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'T represent like, F represent unlike',
  `create_time` timestamp NULL DEFAULT NULL COMMENT 'User created time',
  `update_time` timestamp NULL DEFAULT NULL COMMENT 'Last modified time',
  `is_deleted` tinyint(1) NULL DEFAULT NULL COMMENT 'Is deleted',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 415 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for ui_movie_rating
-- ----------------------------
DROP TABLE IF EXISTS `ui_movie_rating`;
CREATE TABLE `ui_movie_rating`  (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'Movie user rating id',
  `movieId` int(10) NULL DEFAULT NULL COMMENT 'MongoDB movie ID',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'User email',
  `rating` float NULL DEFAULT NULL COMMENT 'Rating score',
  `create_time` timestamp NULL DEFAULT NULL COMMENT 'User created time',
  `update_time` timestamp NULL DEFAULT NULL COMMENT 'Last modified time',
  `is_deleted` tinyint(1) NULL DEFAULT NULL COMMENT 'Is deleted',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 150 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
