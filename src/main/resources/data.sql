-- Insertar en la tabla orders
INSERT INTO orders (id) VALUES (1);
INSERT INTO orders (id) VALUES (2);
INSERT INTO orders (id) VALUES (3);

-- Insertar en la tabla de colección generada automáticamente (order_books)
INSERT INTO order_books (order_id, books) VALUES (1, 1), (1, 2);
INSERT INTO order_books (order_id, books) VALUES (2, 4);
INSERT INTO order_books (order_id, books) VALUES (3, 4), (3, 1), (3, 2);