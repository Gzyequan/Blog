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

 Date: 06/08/2019 09:25:13
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` int(16) NOT NULL AUTO_INCREMENT,
  `nickname` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `realname` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `age` int(3) NULL DEFAULT NULL,
  `mobilephone` varchar(16) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `address` varchar(300) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `birthday` datetime NULL DEFAULT NULL,
  `password` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `status` tinyint(1) NULL DEFAULT 1 COMMENT '用户状态:0->注销,1->正常,2->禁用',
  `create_time` datetime NOT NULL,
  `modify_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_mobilephone`(`mobilephone`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'yequan', '叶权', 23, '15210482684', 'beijing ', '2010-07-07 00:00:00', '3f5342f172d02f9abb8d365170be1964', 1, '2019-07-01 10:00:00', NULL);
INSERT INTO `sys_user` VALUES (3, 'yequan', '叶权222', 23, '15210482444', 'beijing', '2019-06-28 14:14:03', '3f5342f172d02f9abb8d365170be1964', 1, '2019-07-01 10:00:00', NULL);
INSERT INTO `sys_user` VALUES (6, 'ls', '李四', 23, '15210482469', 'beijing', '2019-06-28 14:14:03', '3f5342f172d02f9abb8d365170be1964', 1, '2019-07-01 10:00:00', NULL);
INSERT INTO `sys_user` VALUES (7, 'rt', 'rtty', 23, '15210482499', 'beijing', '2019-06-28 14:14:03', '3f5342f172d02f9abb8d365170be1964', 1, '2019-07-01 10:00:00', NULL);
INSERT INTO `sys_user` VALUES (8, 'rtt', '通融通融', 23, '15210482899', 'beijing', '2019-06-28 14:14:03', '3f5342f172d02f9abb8d365170be1964', 1, '2019-07-01 10:00:00', NULL);
INSERT INTO `sys_user` VALUES (9, 'kobe brynt', '科比布莱恩特', 23, '15210482399', 'beijing', '2019-06-28 14:14:03', '3f5342f172d02f9abb8d365170be1964', 1, '2019-07-01 10:00:00', NULL);
INSERT INTO `sys_user` VALUES (10, 'Stephen curry', '库里', 23, '15210482391', 'beijing', '2019-06-28 14:14:03', '3f5342f172d02f9abb8d365170be1964', 1, '2019-07-01 10:00:00', NULL);
INSERT INTO `sys_user` VALUES (11, 'klay tompson', '汤普森', 23, '15210482345', 'beijing', '2019-06-28 14:14:03', '3f5342f172d02f9abb8d365170be1964', 1, '2019-07-01 10:00:00', NULL);
INSERT INTO `sys_user` VALUES (12, 'k', '小卡', 23, '15210485345', 'beijing', '2019-06-28 14:14:03', '3f5342f172d02f9abb8d365170be1964', 1, '2019-07-01 10:00:00', NULL);
INSERT INTO `sys_user` VALUES (13, 'Onel', '奥尼尔大鲨鱼', 23, '15210481345', 'losangel', '2019-06-28 14:14:03', '3f5342f172d02f9abb8d365170be1964', 1, '2019-07-01 10:00:00', NULL);
INSERT INTO `sys_user` VALUES (14, NULL, '李白', NULL, '18912345678', NULL, NULL, '3f5342f172d02f9abb8d365170be1964', 1, '2019-07-01 10:00:00', NULL);
INSERT INTO `sys_user` VALUES (15, 'Onel', '奥尼尔', 23, '15210481395', 'beijing', '2019-06-28 14:14:03', '12f70d795925f0d78b85849887cac8e9', 1, '2019-07-01 10:00:00', NULL);
INSERT INTO `sys_user` VALUES (16, 'David', '浓眉', 23, '15212481395', '洛杉矶', '2019-06-28 14:14:03', '12f70d795925f0d78b85849887cac8e9', 1, '2019-07-01 10:00:00', NULL);
INSERT INTO `sys_user` VALUES (17, 'Kuzima', '库兹马', 23, '18212481395', '洛杉矶', '2019-06-28 14:14:03', '12f70d795925f0d78b85849887cac8e9', 1, '2019-07-01 10:00:00', NULL);
INSERT INTO `sys_user` VALUES (18, 'YaoMing', '姚明', 40, '18912481395', '上海', '1980-08-06 08:00:00', '12f70d795925f0d78b85849887cac8e9', 1, '2019-07-01 10:00:00', NULL);
INSERT INTO `sys_user` VALUES (19, 'SunYang', '孙杨', 34, '18912481398', '北京', '1985-01-01 08:00:00', '3f5342f172d02f9abb8d365170be1964', 1, '2019-07-29 09:03:04', '2019-07-29 09:18:28');
INSERT INTO `sys_user` VALUES (20, 'LiBai', '李白', 1000, '18912461398', '长安', '1019-04-06 08:00:00', '3f5342f172d02f9abb8d365170be1964', 0, '2019-07-29 09:37:54', '2019-07-29 09:44:45');
INSERT INTO `sys_user` VALUES (21, 'JayChou', '周杰伦', 34, '15210482685', '台北', '1985-04-06 08:00:00', '3f5342f172d02f9abb8d365170be1964', 1, '2019-08-01 12:51:35', NULL);

SET FOREIGN_KEY_CHECKS = 1;
