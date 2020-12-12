CREATE TABLE inventories (
    id INT GENERATED ALWAYS AS IDENTITY,
    name VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE categories (
    id INT GENERATED ALWAYS AS IDENTITY,
    name VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE products (
    id INT GENERATED ALWAYS AS IDENTITY,
    category_id INT NOT NULL,
    name VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_category FOREIGN KEY(category_id) REFERENCES categories(id) ON DELETE CASCADE
);

CREATE TABLE stocks (
    product_id INT NOT NULL,
    inventory_id INT NOT NULL,
    quantity INT DEFAULT 0,
    PRIMARY KEY (product_id, inventory_id),
    CONSTRAINT fk_product FOREIGN KEY(product_id) REFERENCES products(id) ON DELETE CASCADE,
    CONSTRAINT fk_inventory FOREIGN KEY(inventory_id) REFERENCES inventories(id) ON DELETE CASCADE
);
