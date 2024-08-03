CREATE TABLE  IF NOT EXISTS `product` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(100) NOT NULL,
    `price` DECIMAL(10, 2) NOT NULL,
    `stock` INT NOT NULL,
    `created_at` date NOT NULL,
    `created_by` varchar(20) NOT NULL,
    `updated_at` date DEFAULT NULL,
    `updated_by` varchar(20) DEFAULT NULL
);


CREATE TABLE  IF NOT EXISTS `ORDER_TABLE` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `status` VARCHAR(50) NOT NULL DEFAULT 'Pending'
);

CREATE TABLE  IF NOT EXISTS `order_item` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `order_id` INT NOT NULL,
    `product_id` INT NOT NULL,
    `quantity` INT NOT NULL,
    FOREIGN KEY (`order_id`) REFERENCES `ORDER_TABLE`(`id`),
    FOREIGN KEY (`product_id`) REFERENCES `product`(`id`),
    UNIQUE (order_id, product_id)
);

