/*
Navicat MySQL Data Transfer

Source Server         : local
Source Server Version : 50719
Source Host           : localhost:3306
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 50719
File Encoding         : 65001

Date: 2019-02-26 15:24:57
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `post`
-- ----------------------------
DROP TABLE IF EXISTS `post`;
CREATE TABLE `post` (
  `post_code` varchar(255) NOT NULL COMMENT '邮政编码',
  `area_code` varchar(255) NOT NULL COMMENT '对应的区域编码',
  PRIMARY KEY (`post_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of post
-- ----------------------------
INSERT INTO `post` VALUES ('410001', '010');
INSERT INTO `post` VALUES ('410002', '021');
INSERT INTO `post` VALUES ('410003', '022');
INSERT INTO `post` VALUES ('410004', '023');
