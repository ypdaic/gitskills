/*
Navicat MySQL Data Transfer

Source Server         : local
Source Server Version : 50719
Source Host           : localhost:3306
Source Database       : temp

Target Server Type    : MYSQL
Target Server Version : 50719
File Encoding         : 65001

Date: 2019-02-26 15:38:10
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `user_list`
-- ----------------------------
DROP TABLE IF EXISTS `user_list`;
CREATE TABLE `user_list` (
  `id` bigint(22) NOT NULL,
  `user_name` varchar(50) NOT NULL,
  `area_code` varchar(5) NOT NULL,
  `email` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_list
-- ----------------------------
INSERT INTO `user_list`(`id`,`user_name`,`area_code`,`email`) VALUES ('1', 'mark', '010', 'mark@enjoy.com');
INSERT INTO `user_list`(`id`,`user_name`,`area_code`,`email`) VALUES ('2', 'lison', '021', 'lison@enjoy.com');
INSERT INTO `user_list`(`id`,`user_name`,`area_code`,`email`) VALUES ('3', 'james', '022', 'james@enjoy.com');
INSERT INTO `user_list`(`id`,`user_name`,`area_code`,`email`) VALUES ('4', 'king', '023', 'king@enjoy.com');
INSERT INTO `user_list`(`id`,`user_name`,`area_code`,`email`) VALUES ('5', 'deer', '021', 'deer@enjoy.com');
INSERT INTO `user_list`(`id`,`user_name`,`area_code`,`email`) VALUES ('6', 'peter', '022', 'peter@enjoy.com');
INSERT INTO `user_list`(`id`,`user_name`,`area_code`,`email`) VALUES ('7', 'm_1', '010', 'm_1@enjoy.com');
INSERT INTO `user_list`(`id`,`user_name`,`area_code`,`email`) VALUES ('8', 'm_2', '022', 'm_2@enjoy.com');
INSERT INTO `user_list`(`id`,`user_name`,`area_code`,`email`) VALUES ('9', 'm_3', '021', 'm_3@enjoy.com');