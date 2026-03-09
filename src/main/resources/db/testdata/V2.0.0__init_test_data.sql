-- Languages
INSERT INTO language (id, iso_name) VALUES (1, 'en');
INSERT INTO language (id, iso_name) VALUES (2, 'hu');
INSERT INTO language (id, iso_name) VALUES (3, 'de');

-- Source pages
INSERT INTO source_page (id, name, language_id) VALUES (1, 'BBC Good Food', 1);
INSERT INTO source_page (id, name, language_id) VALUES (2, 'Mindmegette', 2);
INSERT INTO source_page (id, name, language_id) VALUES (3, 'Chefkoch', 3);

-- Ingredients (just identity, no data)
INSERT INTO ingredient (id) VALUES (1); -- chicken breast
INSERT INTO ingredient (id) VALUES (2); -- garlic
INSERT INTO ingredient (id) VALUES (3); -- olive oil
INSERT INTO ingredient (id) VALUES (4); -- tomato
INSERT INTO ingredient (id) VALUES (5); -- onion

-- Ingredient names (multilingual)
-- Chicken breast
INSERT INTO ingredient_name (id, name, relevance_score, ingredient_id, language_id) VALUES (1,  'Chicken Breast',  100, 1, 1);
INSERT INTO ingredient_name (id, name, relevance_score, ingredient_id, language_id) VALUES (2,  'Csirkemell',      100, 1, 2);
INSERT INTO ingredient_name (id, name, relevance_score, ingredient_id, language_id) VALUES (3,  'Hähnchenbrust',   100, 1, 3);

-- Garlic
INSERT INTO ingredient_name (id, name, relevance_score, ingredient_id, language_id) VALUES (4,  'Garlic',          100, 2, 1);
INSERT INTO ingredient_name (id, name, relevance_score, ingredient_id, language_id) VALUES (5,  'Fokhagyma',       100, 2, 2);
INSERT INTO ingredient_name (id, name, relevance_score, ingredient_id, language_id) VALUES (6,  'Knoblauch',       100, 2, 3);

-- Olive oil
INSERT INTO ingredient_name (id, name, relevance_score, ingredient_id, language_id) VALUES (7,  'Olive Oil',       100, 3, 1);
INSERT INTO ingredient_name (id, name, relevance_score, ingredient_id, language_id) VALUES (8,  'Olívaolaj',       100, 3, 2);
INSERT INTO ingredient_name (id, name, relevance_score, ingredient_id, language_id) VALUES (9,  'Olivenöl',        100, 3, 3);

-- Tomato
INSERT INTO ingredient_name (id, name, relevance_score, ingredient_id, language_id) VALUES (10, 'Tomato',          100, 4, 1);
INSERT INTO ingredient_name (id, name, relevance_score, ingredient_id, language_id) VALUES (11, 'Paradicsom',      100, 4, 2);
INSERT INTO ingredient_name (id, name, relevance_score, ingredient_id, language_id) VALUES (12, 'Tomate',          100, 4, 3);

-- Onion
INSERT INTO ingredient_name (id, name, relevance_score, ingredient_id, language_id) VALUES (13, 'Onion',           100, 5, 1);
INSERT INTO ingredient_name (id, name, relevance_score, ingredient_id, language_id) VALUES (14, 'Hagyma',          100, 5, 2);
INSERT INTO ingredient_name (id, name, relevance_score, ingredient_id, language_id) VALUES (15, 'Zwiebel',         100, 5, 3);

-- Alternative names
INSERT INTO ingredient_alternative_name (id, name, ingredient_name_id) VALUES (1, 'Chicken Fillet',   1); -- alt for EN chicken breast
INSERT INTO ingredient_alternative_name (id, name, ingredient_name_id) VALUES (2, 'Chicken Cutlet',   1);
INSERT INTO ingredient_alternative_name (id, name, ingredient_name_id) VALUES (3, 'Csirkefilé',       2); -- alt for HU chicken breast
INSERT INTO ingredient_alternative_name (id, name, ingredient_name_id) VALUES (4, 'Wild Garlic',      4); -- alt for EN garlic
INSERT INTO ingredient_alternative_name (id, name, ingredient_name_id) VALUES (5, 'Plum Tomato',      10); -- alt for EN tomato
INSERT INTO ingredient_alternative_name (id, name, ingredient_name_id) VALUES (6, 'Cherry Tomato',    10);
INSERT INTO ingredient_alternative_name (id, name, ingredient_name_id) VALUES (7, 'Koktélparadicsom', 11); -- alt for HU tomato

-- Recipes
INSERT INTO recipe (id, name, cooking_time, num_of_ingredients, source_page_id) VALUES (1, 'Garlic Chicken',         2, 3, 1);
INSERT INTO recipe (id, name, cooking_time, num_of_ingredients, source_page_id) VALUES (2, 'Tomato & Onion Salad',   0, 3, 1);
INSERT INTO recipe (id, name, cooking_time, num_of_ingredients, source_page_id) VALUES (3, 'Fokhagymás csirkemell', 2, 3, 2);
INSERT INTO recipe (id, name, cooking_time, num_of_ingredients, source_page_id) VALUES (4, 'Knoblauch Hähnchen',     2, 3, 3);

-- Recipe ingredients
-- Garlic Chicken (EN)
INSERT INTO recipe_ingredient (id, ingredient_id, recipe_id) VALUES (1, 1, 1); -- chicken breast
INSERT INTO recipe_ingredient (id, ingredient_id, recipe_id) VALUES (2, 2, 1); -- garlic
INSERT INTO recipe_ingredient (id, ingredient_id, recipe_id) VALUES (3, 3, 1); -- olive oil

-- Tomato & Onion Salad (EN)
INSERT INTO recipe_ingredient (id, ingredient_id, recipe_id) VALUES (4, 4, 2); -- tomato
INSERT INTO recipe_ingredient (id, ingredient_id, recipe_id) VALUES (5, 5, 2); -- onion
INSERT INTO recipe_ingredient (id, ingredient_id, recipe_id) VALUES (6, 3, 2); -- olive oil

-- Fokhagymás csirkemell (HU)
INSERT INTO recipe_ingredient (id, ingredient_id, recipe_id) VALUES (7, 1, 3); -- chicken breast
INSERT INTO recipe_ingredient (id, ingredient_id, recipe_id) VALUES (8, 2, 3); -- garlic
INSERT INTO recipe_ingredient (id, ingredient_id, recipe_id) VALUES (9, 3, 3); -- olive oil

-- Knoblauch Hähnchen (DE)
INSERT INTO recipe_ingredient (id, ingredient_id, recipe_id) VALUES (10, 1, 4); -- chicken breast
INSERT INTO recipe_ingredient (id, ingredient_id, recipe_id) VALUES (11, 2, 4); -- garlic
INSERT INTO recipe_ingredient (id, ingredient_id, recipe_id) VALUES (12, 3, 4); -- olive oil

SELECT setval('language_id_seq', (SELECT MAX(id) FROM ingredient));
SELECT setval('source_page_id_seq', (SELECT MAX(id) FROM ingredient));
SELECT setval('ingredient_id_seq', (SELECT MAX(id) FROM ingredient));
SELECT setval('ingredient_name_id_seq', (SELECT MAX(id) FROM ingredient_name));
SELECT setval('ingredient_alternative_name_id_seq', (SELECT MAX(id) FROM ingredient_name));
SELECT setval('recipe_id_seq', (SELECT MAX(id) FROM ingredient_name));
SELECT setval('recipe_ingredient_id_seq', (SELECT MAX(id) FROM ingredient_name));