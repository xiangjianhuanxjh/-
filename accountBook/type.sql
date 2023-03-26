/*
 Navicat Premium Data Transfer

 Source Server         : Saitama
 Source Server Type    : MySQL
 Source Server Version : 50737 (5.7.37-log)
 Source Host           : localhost:3306
 Source Schema         : accountbook

 Target Server Type    : MySQL
 Target Server Version : 50737 (5.7.37-log)
 File Encoding         : 65001

 Date: 02/03/2023 16:00:18
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for type
-- ----------------------------
DROP TABLE IF EXISTS `type`;
CREATE TABLE `type`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1904275468 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of type
-- ----------------------------
INSERT INTO `type` VALUES (1, '任意');
INSERT INTO `type` VALUES (2, '餐饮');
INSERT INTO `type` VALUES (3, '购物');
INSERT INTO `type` VALUES (4, '交通');
INSERT INTO `type` VALUES (5, '转账');
INSERT INTO `type` VALUES (6, '学习费用');

SET FOREIGN_KEY_CHECKS = 1;
