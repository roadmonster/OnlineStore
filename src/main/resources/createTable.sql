CREATE TABLE users (
                       id BIGINT NOT NULL AUTO_INCREMENT,
                       name VARCHAR(255) NOT NULL,
                       email VARCHAR(255) NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       PRIMARY KEY (id)
);

CREATE TABLE products (
                          id BIGINT NOT NULL AUTO_INCREMENT,
                          name VARCHAR(255) NOT NULL,
                          price DECIMAL(10,2) NOT NULL,
                          PRIMARY KEY (id)
);

CREATE TABLE inventory (
                           id BIGINT NOT NULL AUTO_INCREMENT,
                           product_id BIGINT NOT NULL,
                           quantity INT NOT NULL,
                           PRIMARY KEY (id),
                           FOREIGN KEY (product_id) REFERENCES products(id)
);

CREATE TABLE sales (
                       id BIGINT NOT NULL AUTO_INCREMENT,
                       user_id BIGINT NOT NULL,
                       product_id BIGINT NOT NULL,
                       quantity INT NOT NULL,
                       price DECIMAL(10,2) NOT NULL,
                       purchase_date TIMESTAMP NOT NULL,
                       PRIMARY KEY (id),
                       FOREIGN KEY (user_id) REFERENCES users(id),
                       FOREIGN KEY (product_id) REFERENCES products(id)
);
