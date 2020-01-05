/*
Navicat MySQL Data Transfer

Source Server         : local
Source Server Version : 50719
Source Host           : localhost:3306
Source Database       : temp

Target Server Type    : MYSQL
Target Server Version : 50719
File Encoding         : 65001

Date: 2019-02-26 15:37:57
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `orderlist_merchant`
-- ----------------------------
DROP TABLE IF EXISTS `orderlist_merchant`;
CREATE TABLE `orderlist_merchant` (
  `merchant_user_id` bigint(22) NOT NULL,
  `id` bigint(22) NOT NULL,
  `order_no` varchar(50) NOT NULL,
  `buyer_user_id` bigint(22) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `index_order_merchant` (`merchant_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of orderlist_merchant
-- ----------------------------
INSERT INTO `orderlist_merchant`(`id`,`order_no`,`buyer_user_id`,`merchant_user_id`) VALUES ('1', 'order01', '1', '7');
INSERT INTO `orderlist_merchant`(`id`,`order_no`,`buyer_user_id`,`merchant_user_id`) VALUES ('2', 'order02', '2', '9');
INSERT INTO `orderlist_merchant`(`id`,`order_no`,`buyer_user_id`,`merchant_user_id`) VALUES ('3', 'order03', '3', '8');
INSERT INTO `orderlist_merchant`(`id`,`order_no`,`buyer_user_id`,`merchant_user_id`) VALUES ('4', 'order04', '4', '8');
INSERT INTO `orderlist_merchant`(`id`,`order_no`,`buyer_user_id`,`merchant_user_id`) VALUES ('5', 'order05', '5', '7');
INSERT INTO `orderlist_merchant`(`id`,`order_no`,`buyer_user_id`,`merchant_user_id`) VALUES ('7', 'order07', '120', '180');

select * from orderlist_merchant where merchant_user_id = 180;