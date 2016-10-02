/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50051
Source Host           : localhost:3306
Source Database       : scan

Target Server Type    : MYSQL
Target Server Version : 50051
File Encoding         : 65001

Date: 2016-10-01 16:48:49
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for goods
-- ----------------------------
DROP TABLE IF EXISTS `goods`;
CREATE TABLE `goods` (
  `material` varchar(100) default '' COMMENT '材料',
  `name` varchar(100) default '' COMMENT '名称',
  `code` varchar(100) NOT NULL default '' COMMENT '代号',
  `length` varchar(100) default '' COMMENT '长度',
  `width` varchar(100) default '' COMMENT '宽度',
  `quantity` varchar(100) default '' COMMENT '数量',
  `stripes` varchar(100) default '' COMMENT '纹理方向',
  `banding` varchar(100) default '' COMMENT '封边信息',
  `hole` varchar(100) default '' COMMENT '排孔信息',
  `package` varchar(100) default '' COMMENT '包号',
  `remark` varchar(200) default '' COMMENT '备注',
  PRIMARY KEY  (`code`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for scan
-- ----------------------------
DROP TABLE IF EXISTS `scan`;
CREATE TABLE `scan` (
  `code` varchar(100) NOT NULL COMMENT '代号',
  `tag` varchar(10) default '是' COMMENT '是否已分拣扫码',
  PRIMARY KEY  (`code`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
