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

 Date: 06/08/2019 09:24:50
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission`  (
  `id` int(16) NOT NULL AUTO_INCREMENT,
  `parent_id` int(16) NULL DEFAULT NULL COMMENT '父级id',
  `pmn_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '权限类型:module(模块),page(页面)',
  `status` tinyint(2) NULL DEFAULT 1 COMMENT '权限状态,0:禁用,1:启用',
  `creator_id` int(16) NULL DEFAULT NULL,
  `updater_id` int(16) NULL DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `modify_time` datetime NULL DEFAULT NULL,
  `detail` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_permission
-- ----------------------------
INSERT INTO `sys_permission` VALUES (1, 0, '博客', 'module', 1, 1, NULL, '2019-07-30 15:57:17', NULL, NULL);

SET FOREIGN_KEY_CHECKS = 1;
