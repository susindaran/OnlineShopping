ALTER TABLE `subscription` ADD `shipping_address_id` INT(11) UNSIGNED NOT NULL;
ALTER TABLE `subscription` ADD `billing_address_id` INT(11) UNSIGNED NOT NULL;

ALTER TABLE `subscription` ADD CONSTRAINT `shipping_address_id_FK_subscription` FOREIGN KEY (`shipping_address_id`) REFERENCES `address`(`address_id`);
ALTER TABLE `subscription` ADD CONSTRAINT `billing_address_id_FK_subscription` FOREIGN KEY (`billing_address_id`) REFERENCES `address`(`address_id`);

ALTER TABLE `subscription` ADD INDEX `shipping_address_id_IDX_subscription` (`shipping_address_id`);
ALTER TABLE `subscription` ADD INDEX `billing_address_id_IDX_subscription` (`billing_address_id`);