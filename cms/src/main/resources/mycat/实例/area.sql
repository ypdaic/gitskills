/*
Navicat MySQL Data Transfer

Source Server         : local
Source Server Version : 50719
Source Host           : localhost:3306
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 50719
File Encoding         : 65001

Date: 2019-02-26 15:24:30
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `area`
-- ----------------------------
DROP TABLE IF EXISTS `area`;
CREATE TABLE `area` (
  `area_code` varchar(5) NOT NULL COMMENT '邮编',
  `area_name` varchar(50) NOT NULL COMMENT '区域名',
  PRIMARY KEY (`area_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of area
-- ----------------------------
INSERT INTO `area` VALUES ('010', '北京');
INSERT INTO `area` VALUES ('021', '上海');
INSERT INTO `area` VALUES ('022', '天津');
INSERT INTO `area` VALUES ('023', '重庆');
