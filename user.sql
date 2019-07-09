/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50527
 Source Host           : localhost:3306
 Source Schema         : commonweb

 Target Server Type    : MySQL
 Target Server Version : 50527
 File Encoding         : 65001

 Date: 08/07/2019 11:12:26
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(16) NOT NULL AUTO_INCREMENT,
  `nickname` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `realname` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `age` int(3) NULL DEFAULT NULL,
  `mobilephone` varchar(16) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `address` varchar(300) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `birthday` datetime NULL DEFAULT NULL,
  `password` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `status` int(1) NULL DEFAULT 1 COMMENT '用户状态:-1->注销,0->禁用,1->正常',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `mobilephone`(`mobilephone`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;
