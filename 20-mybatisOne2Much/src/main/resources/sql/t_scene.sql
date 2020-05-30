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

 Date: 30/05/2020 20:56:47
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_scene
-- ----------------------------
DROP TABLE IF EXISTS `t_scene`;
CREATE TABLE `t_scene`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `scene_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `scene_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_scene
-- ----------------------------
INSERT INTO `t_scene` VALUES (1, 'scene0001', 'adf');
INSERT INTO `t_scene` VALUES (2, 'scene0002', 'adf');
INSERT INTO `t_scene` VALUES (3, 'scene0003', 'er');
INSERT INTO `t_scene` VALUES (4, 'scene0004', '43');

SET FOREIGN_KEY_CHECKS = 1;
