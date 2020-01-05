/*
Navicat MySQL Data Transfer

Source Server         : local
Source Server Version : 50719
Source Host           : localhost:3306
Source Database       : temp

Target Server Type    : MYSQL
Target Server Version : 50719
File Encoding         : 65001

Date: 2019-02-26 15:38:15
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `user_login`
-- ----------------------------
DROP TABLE IF EXISTS `user_login`;
CREATE TABLE `user_login` (
  `login_name` varchar(50) NOT NULL COMMENT '登录名',
  `user_id` bigint(22) NOT NULL COMMENT '用户在user表中的id',
  PRIMARY KEY (`login_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_login
-- ----------------------------
INSERT INTO `user_login`(`login_name`,`user_id`) VALUES ('mark','1');
INSERT INTO `user_login`(`login_name`,`user_id`) VALUES ('13311111111','1');

INSERT INTO `user_login`(`login_name`,`user_id`) VALUES ('lison','2');
INSERT INTO `user_login`(`login_name`,`user_id`) VALUES ('13322222222','2');

INSERT INTO `user_login`(`login_name`,`user_id`) VALUES ('james','3');
INSERT INTO `user_login`(`login_name`,`user_id`) VALUES ('13333333333','3');

INSERT INTO `user_login`(`login_name`,`user_id`) VALUES ('king','4');
INSERT INTO `user_login`(`login_name`,`user_id`) VALUES ('13355555555','4');

INSERT INTO `user_login`(`login_name`,`user_id`) VALUES ('deer','5');
INSERT INTO `user_login`(`login_name`,`user_id`) VALUES ('13366666666','5');

INSERT INTO `user_login`(`login_name`,`user_id`) VALUES ('peter','6');
INSERT INTO `user_login`(`login_name`,`user_id`) VALUES ('13377777777','6');

INSERT INTO `user_login`(`login_name`,`user_id`) VALUES ('m_1','7');
INSERT INTO `user_login`(`login_name`,`user_id`) VALUES ('13388888888','7');

INSERT INTO `user_login`(`login_name`,`user_id`) VALUES ('m_2','8');
INSERT INTO `user_login`(`login_name`,`user_id`) VALUES ('13399999999','8');

INSERT INTO `user_login`(`login_name`,`user_id`) VALUES ('m_3','9');
INSERT INTO `user_login`(`login_name`,`user_id`) VALUES ('13300000000','9');