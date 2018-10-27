/*
Navicat MySQL Data Transfer

Source Server         : zonghy
Source Server Version : 50621
Source Host           : localhost:3306
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 50621
File Encoding         : 65001

Date: 2015-06-26 17:13:16
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `bookinfo`
-- ----------------------------
DROP TABLE IF EXISTS `bookinfo`;
CREATE TABLE `bookinfo` (
  `book_id` int(11) NOT NULL AUTO_INCREMENT,
  `book_name` varchar(50) NOT NULL,
  `book_price` double(10,0) DEFAULT NULL,
  `book_author` varchar(20) NOT NULL,
  `book_img` varchar(100) DEFAULT NULL,
  `book_content` varchar(100) DEFAULT NULL,
  `book_read` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`book_id`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=gb2312;

-- ----------------------------
-- Records of bookinfo
-- ----------------------------
INSERT INTO `bookinfo` VALUES ('1', '匆匆那年', '89', '九夜茴', 'http://192.168.146.1:8080/AndroidReader/image/book/ccnn.jpg', 'http://192.168.146.1:8080/AndroidReader/txt/ccnn.txt', 'http://192.168.146.1:8080/AndroidReader/read/ccnn1.txt');
INSERT INTO `bookinfo` VALUES ('2', '匆匆那年', '89', '九夜茴', 'http://192.168.146.1:8080/AndroidReader/image/book/ccnn.jpg', 'http://192.168.146.1:8080/AndroidReader/txt/ccnn.txt', 'http://192.168.146.1:8080/AndroidReader/read/ccnn1.txt');
INSERT INTO `bookinfo` VALUES ('3', '匆匆那年', '89', '九夜茴', 'http://192.168.146.1:8080/AndroidReader/image/book/ccnn.jpg', 'http://192.168.146.1:8080/AndroidReader/txt/ccnn.txt', 'http://192.168.146.1:8080/AndroidReader/read/ccnn1.txt');
INSERT INTO `bookinfo` VALUES ('4', '匆匆那年', '89', '九夜茴', 'http://192.168.146.1:8080/AndroidReader/image/book/ccnn.jpg', 'http://192.168.146.1:8080/AndroidReader/txt/ccnn.txt', 'http://192.168.146.1:8080/AndroidReader/read/ccnn1.txt');

-- ----------------------------
-- Table structure for `userinfo`
-- ----------------------------
DROP TABLE IF EXISTS `userinfo`;
CREATE TABLE `userinfo` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(64) NOT NULL,
  `pswd` varchar(64) NOT NULL,
  `money` double(10,0) NOT NULL DEFAULT '0',
  `userload` tinyint(10) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=9 DEFAULT CHARSET=gb2312;

-- ----------------------------
-- Records of userinfo
-- ----------------------------
INSERT INTO `userinfo` VALUES ('3', 'guan111', 'haha111', '1001', '1');
INSERT INTO `userinfo` VALUES ('4', 'guan', '1', '1001', '1');
INSERT INTO `userinfo` VALUES ('5', '111', '111', '0', '0');
INSERT INTO `userinfo` VALUES ('6', '234243', 'hjklh', '0', '0');
INSERT INTO `userinfo` VALUES ('7', '1111', '1111', '0', '0');
INSERT INTO `userinfo` VALUES ('8', 'guan456', '111', '0', '0');
