ALTER TABLE `order_detail` ADD `coupon_id` varchar(15) DEFAULT NULL;
ALTER TABLE `order_detail` ADD CONSTRAINT `coupon_id_FK_order_detail` FOREIGN KEY (`coupon_id`) REFERENCES `offer` (`coupon_id`) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE `order_detail` ADD INDEX `coupon_id_IDX_order_detail` (`coupon_id`);