ALTER TABLE `order` ADD `shipping_address_id` INT(11) UNSIGNED NOT NULL;
ALTER TABLE `order` ADD `billing_address_id` INT(11) UNSIGNED NOT NULL;

ALTER TABLE `order` ADD CONSTRAINT `shipping_address_id_FK_order` FOREIGN KEY (`shipping_address_id`) REFERENCES `address`(`address_id`);
ALTER TABLE `order` ADD CONSTRAINT `billing_address_id_FK_order` FOREIGN KEY (`billing_address_id`) REFERENCES `address`(`address_id`);

ALTER TABLE `order` ADD INDEX `shipping_address_id_IDX_order` (`shipping_address_id`);
ALTER TABLE `order` ADD INDEX `billing_address_id_IDX_order` (`billing_address_id`);

ALTER TABLE `tax_details` CHANGE `zipcode` `state` VARCHAR(15);

ALTER TABLE `address` CHANGE `state` `state` VARCHAR(15);
ALTER TABLE `address` ADD CONSTRAINT `state_FK_address` FOREIGN KEY (`state`) REFERENCES `tax_details`(`state`);
ALTER TABLE `address` ADD INDEX `state_IDX_address` (`state`);

ALTER TABLE `subscription` ADD `coupon_id` VARCHAR(15) NOT NULL;
ALTER TABLE `subscription` ADD CONSTRAINT `coupon_id_FK_subscription` FOREIGN KEY (`coupon_id`) REFERENCES `offer`(`coupon_id`);
ALTER TABLE `subscription` ADD INDEX `coupon_id_IDX_subscription` (`coupon_id`);