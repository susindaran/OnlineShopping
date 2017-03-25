ALTER TABLE shopping.order_detail ADD order_detail_id INT(11) UNSIGNED NOT NULL;
ALTER TABLE shopping.order_detail DROP PRIMARY KEY;
ALTER TABLE shopping.order_detail ADD PRIMARY KEY (order_detail_id);
ALTER TABLE shopping.order_detail CHANGE order_detail_id order_detail_id INT(11) UNSIGNED NOT NULL AUTO_INCREMENT;
ALTER TABLE shopping.order_detail ADD CONSTRAINT order_product_UK_order_detail UNIQUE (order_id, product_id);

ALTER TABLE shopping.cart ADD cart_id INT(11) UNSIGNED NOT NULL;
ALTER TABLE shopping.cart DROP PRIMARY KEY;
ALTER TABLE shopping.cart ADD PRIMARY KEY (cart_id);
ALTER TABLE shopping.cart CHANGE cart_id cart_id INT(11) UNSIGNED NOT NULL AUTO_INCREMENT;
ALTER TABLE shopping.cart ADD CONSTRAINT customer_product_UK_cart UNIQUE (customer_id, product_id);

ALTER TABLE shopping.product ADD CONSTRAINT product_id_UK_product UNIQUE KEY (product_id);

ALTER TABLE shopping.shipment ADD order_id INT(11) UNSIGNED NOT NULL;
ALTER TABLE shopping.shipment ADD CONSTRAINT order_id_FK_shipment FOREIGN KEY (order_id) REFERENCES `order` (order_id) ON DELETE CASCADE ON UPDATE CASCADE;