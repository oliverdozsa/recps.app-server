CREATE EXTENSION IF NOT EXISTS pg_trgm;

CREATE INDEX idx_trgm_recipe_name ON recipe USING GIN (name gin_trgm_ops);