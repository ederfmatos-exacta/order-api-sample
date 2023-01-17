CREATE TABLE order_items
(
    id         INTEGER            NOT NULL AUTO_INCREMENT PRIMARY KEY,
    uuid       VARCHAR(36) UNIQUE NOT NULL,
    name       VARCHAR(100)       NOT NULL,
    quantity   INTEGER            NOT NULL,
    amount     DECIMAL(6, 2)      NOT NULL,
    order_id   INTEGER            NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_order_items_order_id FOREIGN KEY (order_id) REFERENCES orders (id)
);

CREATE INDEX idx_order_uuid ON order_items (order_id)