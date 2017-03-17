ALTER TABLE shopping.order_detail ADD order_detail_id INT(11) UNSIGNED;
ALTER TABLE shopping.order_detail DROP PRIMARY KEY;
ALTER TABLE shopping.order_detail ADD PRIMARY KEY (order_detail_id);