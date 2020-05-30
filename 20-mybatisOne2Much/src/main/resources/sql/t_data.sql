/*
 Navicat Premium Data Transfer

 Source Server         : win-mysql-3306
 Source Server Type    : MySQL
 Source Server Version : 80016
 Source Host           : localhost:3306
 Source Schema         : dev

 Target Server Type    : MySQL
 Target Server Version : 80016
 File Encoding         : 65001

 Date: 30/05/2020 20:55:24
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_data
-- ----------------------------
DROP TABLE IF EXISTS `t_data`;
CREATE TABLE `t_data`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `data_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `data_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_data
-- ----------------------------
INSERT INTO `t_data` VALUES (1, 'data0001', '111');
INSERT INTO `t_data` VALUES (2, 'data0002', '233');
INSERT INTO `t_data` VALUES (3, 'data0003', '34q53');
INSERT INTO `t_data` VALUES (4, 'data0004', 'cvzxgf');
INSERT INTO `t_data` VALUES (5, 'data0005', '42rg');

SET FOREIGN_KEY_CHECKS = 1;
