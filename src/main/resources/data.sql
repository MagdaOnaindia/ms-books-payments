INSERT INTO orders (id, order_date) VALUES (1, '2023-10-26 10:00:00.000');
INSERT INTO orders (id, order_date) VALUES (2, '2023-10-26 11:30:00.000');
INSERT INTO orders (id, order_date) VALUES (3, '2023-10-27 09:15:00.000');



-- Ítems para la Orden con ID 1
INSERT INTO order_items (order_id, book_id, quantity) VALUES (1, 101, 2);
INSERT INTO order_items (order_id, book_id, quantity) VALUES (1, 102, 1);

-- Ítems para la Orden con ID 2
INSERT INTO order_items (order_id, book_id, quantity) VALUES (2, 104, 3);

-- Ítems para la Orden con ID 3
INSERT INTO order_items (order_id, book_id, quantity) VALUES (3, 104, 1);
INSERT INTO order_items (order_id, book_id, quantity) VALUES (3, 101, 1);
INSERT INTO order_items (order_id, book_id, quantity) VALUES (3, 102, 5);