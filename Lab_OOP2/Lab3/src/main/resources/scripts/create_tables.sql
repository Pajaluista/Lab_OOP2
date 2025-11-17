-- users table
CREATE TABLE IF NOT EXISTS users (
  id SERIAL PRIMARY KEY,
  username VARCHAR(100) NOT NULL UNIQUE,
  email VARCHAR(200),
  created_at TIMESTAMP DEFAULT now()
);

-- functions table
CREATE TABLE IF NOT EXISTS functions (
  id SERIAL PRIMARY KEY,
  user_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
  name VARCHAR(200) NOT NULL,
  expression TEXT,
  description TEXT,
  created_at TIMESTAMP DEFAULT now()
);

-- computed_points table
CREATE TABLE IF NOT EXISTS computed_points (
  id SERIAL PRIMARY KEY,
  function_id INTEGER NOT NULL REFERENCES functions(id) ON DELETE CASCADE,
  x DOUBLE PRECISION NOT NULL,
  y DOUBLE PRECISION NOT NULL,
  computed_at TIMESTAMP DEFAULT now()
);

CREATE INDEX IF NOT EXISTS idx_functions_user ON functions(user_id);
CREATE INDEX IF NOT EXISTS idx_points_function ON computed_points(function_id);
CREATE INDEX IF NOT EXISTS idx_points_x ON computed_points(x);