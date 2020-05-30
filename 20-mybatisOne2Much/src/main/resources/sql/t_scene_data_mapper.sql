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

 Date: 30/05/2020 20:56:59
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_scene_data_mapper
-- ----------------------------
DROP TABLE IF EXISTS `t_scene_data_mapper`;
CREATE TABLE `t_scene_data_mapper`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `scene_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `data_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_scene_data_mapper
-- ----------------------------
INSERT INTO `t_scene_data_mapper` VALUES (1, 'scene0001', 'data0001');
INSERT INTO `t_scene_data_mapper` VALUES (2, 'scene0001', 'data0002');
INSERT INTO `t_scene_data_mapper` VALUES (3, 'scene0001', 'data0003');
INSERT INTO `t_scene_data_mapper` VALUES (4, 'scene0002', 'data0003');
INSERT INTO `t_scene_data_mapper` VALUES (5, 'scene0002', 'data0005');
INSERT INTO `t_scene_data_mapper` VALUES (6, 'scene0003', 'data0004');
INSERT INTO `t_scene_data_mapper` VALUES (7, 'scene0003', 'data0005');
INSERT INTO `t_scene_data_mapper` VALUES (8, 'scene0004', 'data0005');

SET FOREIGN_KEY_CHECKS = 1;
