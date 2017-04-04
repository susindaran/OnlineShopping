ALTER TABLE shopping.subscription ADD subscription_id INT(11) UNSIGNED NOT NULL;
ALTER TABLE shopping.subscription DROP PRIMARY KEY;
ALTER TABLE shopping.subscription ADD PRIMARY KEY (subscription_id);
ALTER TABLE shopping.subscription CHANGE subscription_id subscription_id INT(11) UNSIGNED NOT NULL AUTO_INCREMENT;
ALTER TABLE shopping.subscription ADD CONSTRAINT customer_product_UK_subscription UNIQUE (customer_id, product_id);
ALTER TABLE shopping.subscription MODIFY COLUMN coupon_id varchar(15) NULL;