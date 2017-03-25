DROP SCHEMA IF EXISTS `shopping`;
CREATE SCHEMA IF NOT EXISTS `shopping` DEFAULT CHARACTER SET utf8;
USE `shopping`;

CREATE TABLE `customer` (
  `customer_id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `first_name` varchar(45) NOT NULL,
  `last_name` varchar(45) NOT NULL,
  `email_id` varchar(70) NOT NULL,
  `password` char(33) NOT NULL,
  PRIMARY KEY (`customer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `address` (
  `address_id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `customer_id` int(11) UNSIGNED NOT NULL,
  `phone` varchar(15) NOT NULL,
  `street` varchar(150) NOT NULL,
  `city` varchar(45) NOT NULL,
  `state` varchar(45) NOT NULL,
  `country` varchar(45) NOT NULL,
  `zipcode` varchar(15) NOT NULL,
  PRIMARY KEY (`address_id`),
  KEY `customer_id_IDX_address` (`customer_id`),
  CONSTRAINT `customer_id_FK_address` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`customer_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `product` (
  `id` int(10) UNSIGNED zerofill NOT NULL AUTO_INCREMENT,
  `product_id` varchar(15) NOT NULL,
  `product_name` varchar(45) NOT NULL,
  `quantity` int(5) DEFAULT '0',
  `price` double NOT NULL,
  `description` longtext NOT NULL,
  PRIMARY KEY (`id`),
  KEY `product_id_IDX_product` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `availability` (
  `product_id` varchar(15) NOT NULL,
  `zipcode` varchar(15) NOT NULL,
  PRIMARY KEY (`product_id`,`zipcode`),
  KEY `product_id_IDX_availability` (`product_id`),
  CONSTRAINT `product_id_FK_availability` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `offer` (
  `coupon_id` varchar(15) NOT NULL,
  `product_id` varchar(15) NOT NULL,
  `discount` int(4) DEFAULT '0',
  `expiry_date` datetime NOT NULL,
  PRIMARY KEY (`coupon_id`),
  KEY `product_id_IDX_offers` (`product_id`),
  CONSTRAINT `product_id_FK_offers` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `cart` (
  `customer_id` int(11) UNSIGNED NOT NULL,
  `product_id` varchar(15) NOT NULL,
  `quantity` int(5) NOT NULL,
  `coupon_id` varchar(15) DEFAULT NULL,
  PRIMARY KEY (`customer_id`,`product_id`),
  KEY `customer_id_IDX_cart` (`customer_id`),
  KEY `product_id_IDX_cart` (`product_id`),
  KEY `coupon_id_IDX_cart` (`coupon_id`),
  CONSTRAINT `coupon_id_FK_cart` FOREIGN KEY (`coupon_id`) REFERENCES `offer` (`coupon_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `customer_id_FK_cart` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`customer_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `product_id_FK_cart` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `card_detail` (
  `card_number` varchar(25) NOT NULL,
  `customer_id` int(11) UNSIGNED NOT NULL,
  `expiry_date` datetime NOT NULL,
  `name_on_card` varchar(75) NOT NULL,
  PRIMARY KEY (`card_number`),
  KEY `customer_id_IDX_card_detail` (`customer_id`),
  CONSTRAINT `customer_id_FK_card_detail` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`customer_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `category` (
  `category_id` varchar(5) NOT NULL,
  `category_name` varchar(100) NOT NULL,
  PRIMARY KEY (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `order` (
  `order_id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `customer_id` int(11) UNSIGNED NOT NULL,
  `order_status` varchar(45) NOT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`order_id`),
  KEY `customer_id_IDX_order` (`customer_id`),
  CONSTRAINT `customer_id_FK_order` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`customer_id`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `shipment` (
  `shipment_id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `delivery_due_date` datetime NOT NULL,
  `status` varchar(45) DEFAULT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`shipment_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `order_detail` (
  `order_id` int(11) UNSIGNED NOT NULL,
  `product_id` varchar(15) NOT NULL,
  `shipment_id` int(11) UNSIGNED NOT NULL,
  `quantity` int(11) NOT NULL,
  PRIMARY KEY (`order_id`,`product_id`),
  KEY `order_id_IDX_order_details` (`order_id`),
  KEY `prod_id_IDX_order_details` (`product_id`),
  KEY `shipment_id_IDX_order_details` (`shipment_id`),
  CONSTRAINT `order_id_FK_order_detail` FOREIGN KEY (`order_id`) REFERENCES `order` (`order_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `product_id_FK_order_detail` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `shipment_id_FK_order_detail` FOREIGN KEY (`shipment_id`) REFERENCES `shipment` (`shipment_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `payment` (
  `payment_id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `order_id` int(11) UNSIGNED NOT NULL,
  `transaction_type` varchar(50) NOT NULL,
  `amount` double NOT NULL,
  `reason` varchar(100) DEFAULT NULL,
  `ref_1` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`payment_id`),
  KEY `order_id_IDX_sample` (`order_id`),
  KEY `ref_1_IDX_sample` (`ref_1`),
  CONSTRAINT `order_id_FK_sample` FOREIGN KEY (`order_id`) REFERENCES `order` (`order_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `subscription` (
  `product_id` varchar(15) NOT NULL,
  `customer_id` int(11) UNSIGNED NOT NULL,
  `quantity` int(5) NOT NULL,
  `frequency_in_days` int(5) NOT NULL,
  `status` varchar(25) NOT NULL,
  `next_due_date` datetime NOT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`product_id`,`customer_id`),
  KEY `customer_id_IDX_subscription` (`customer_id`),
  KEY `product_id_IDX_subscription` (`product_id`),
  CONSTRAINT `customer_id_FK_subscription` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`customer_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `product_id_FK_subscription` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `tax_details` (
  `zipcode` varchar(15) NOT NULL,
  `tax` int(5) DEFAULT NULL,
  PRIMARY KEY (`zipcode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;