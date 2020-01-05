/*
Navicat MySQL Data Transfer

Source Server         : local
Source Server Version : 50719
Source Host           : localhost:3306
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 50719
File Encoding         : 65001

Date: 2019-02-26 15:48:37
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `order_item_merchant`
-- ----------------------------
DROP TABLE IF EXISTS `order_item_merchant`;
CREATE TABLE `order_item_merchant` (
  `id` bigint(22) NOT NULL,
  `order_id` bigint(22) NOT NULL,
  `item_no` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of order_item_merchant
-- ----------------------------
INSERT INTO `order_item_merchant`(`id`,`order_id`,`item_no`) VALUES ('1', '1', 'order1-item1');
INSERT INTO `order_item_merchant`(`id`,`order_id`,`item_no`) VALUES ('2', '1', 'order1-item2');
INSERT INTO `order_item_merchant`(`id`,`order_id`,`item_no`) VALUES ('3', '2', 'order2-item1');
INSERT INTO `order_item_merchant`(`id`,`order_id`,`item_no`) VALUES ('4', '3', 'order3-item1');
INSERT INTO `order_item_merchant`(`id`,`order_id`,`item_no`) VALUES ('5', '3', 'order3-item2');
INSERT INTO `order_item_merchant`(`id`,`order_id`,`item_no`) VALUES ('6', '3', 'order3-item3');
INSERT INTO `order_item_merchant`(`id`,`order_id`,`item_no`) VALUES ('7', '4', 'order4-item1');
INSERT INTO `order_item_merchant`(`id`,`order_id`,`item_no`) VALUES ('8', '5', 'order5-item1');

INSERT INTO `order_item_merchant`(`id`,`order_id`,`item_no`) VALUES ('9', '7', 'order7-item1');
