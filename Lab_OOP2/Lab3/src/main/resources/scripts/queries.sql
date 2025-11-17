-- Users
SELECT * FROM users WHERE id = ?;
INSERT INTO users(username, email) VALUES (?, ?) RETURNING id;
UPDATE users SET username = ?, email = ? WHERE id = ?;
DELETE FROM users WHERE id = ?;

-- Functions
SELECT * FROM functions WHERE id = ?;
SELECT * FROM functions WHERE user_id = ?;
INSERT INTO functions(user_id, name, expression, description) VALUES (?, ?, ?, ?) RETURNING id;
UPDATE functions SET name = ?, expression = ?, description = ? WHERE id = ?;
DELETE FROM functions WHERE id = ?;

-- ComputedPoints
SELECT * FROM computed_points WHERE id = ?;
SELECT * FROM computed_points WHERE function_id = ? ORDER BY x;
INSERT INTO computed_points(function_id, x, y) VALUES (?, ?, ?) RETURNING id;
UPDATE computed_points SET x = ?, y = ? WHERE id = ?;
DELETE FROM computed_points WHERE id = ?;