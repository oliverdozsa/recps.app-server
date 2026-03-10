-- Languages
INSERT INTO language (id, iso_name) VALUES (1, 'en');
INSERT INTO language (id, iso_name) VALUES (2, 'hu');

-- Source pages
INSERT INTO source_page (id, name, language_id) VALUES (1, 'BBC Good Food',  1);
INSERT INTO source_page (id, name, language_id) VALUES (2, 'Mindmegette',    2);
INSERT INTO source_page (id, name, language_id) VALUES (3, 'AllRecipes',     1);
INSERT INTO source_page (id, name, language_id) VALUES (4, 'Street Kitchen', 2);

-- Ingredients (just identity, no data)
INSERT INTO ingredient (id) VALUES (1); -- chicken breast
INSERT INTO ingredient (id) VALUES (2); -- garlic
INSERT INTO ingredient (id) VALUES (3); -- olive oil
INSERT INTO ingredient (id) VALUES (4); -- tomato
INSERT INTO ingredient (id) VALUES (5); -- onion
INSERT INTO ingredient (id) VALUES (6); -- milk
INSERT INTO ingredient (id) VALUES (7); -- egg
INSERT INTO ingredient (id) VALUES (8); -- sugar
INSERT INTO ingredient (id) VALUES (9);  -- bean
INSERT INTO ingredient (id) VALUES (10); -- flour
INSERT INTO ingredient (id) VALUES (11); -- potato
INSERT INTO ingredient (id) VALUES (12); -- sour cream
INSERT INTO ingredient (id) VALUES (13); -- carrot
INSERT INTO ingredient (id) VALUES (14); -- bell pepper
INSERT INTO ingredient (id) VALUES (15); -- rice
INSERT INTO ingredient (id) VALUES (16); -- mushroom
INSERT INTO ingredient (id) VALUES (17); -- butter
INSERT INTO ingredient (id) VALUES (18); -- apple
INSERT INTO ingredient (id) VALUES (19); -- cucumber
INSERT INTO ingredient (id) VALUES (20); -- vinegar
INSERT INTO ingredient (id) VALUES (21); -- dill

-- Ingredient names (multilingual)
-- Chicken breast
INSERT INTO ingredient_name (id, name, relevance_score, ingredient_id, language_id) VALUES (1,  'Chicken Breast',  100, 1, 1);
INSERT INTO ingredient_name (id, name, relevance_score, ingredient_id, language_id) VALUES (2,  'Csirkemell',      100, 1, 2);

-- Garlic
INSERT INTO ingredient_name (id, name, relevance_score, ingredient_id, language_id) VALUES (4,  'Garlic',          100, 2, 1);
INSERT INTO ingredient_name (id, name, relevance_score, ingredient_id, language_id) VALUES (5,  'Fokhagyma',       100, 2, 2);

-- Olive oil
INSERT INTO ingredient_name (id, name, relevance_score, ingredient_id, language_id) VALUES (7,  'Olive Oil',       100, 3, 1);
INSERT INTO ingredient_name (id, name, relevance_score, ingredient_id, language_id) VALUES (8,  'Olívaolaj',       100, 3, 2);

-- Tomato
INSERT INTO ingredient_name (id, name, relevance_score, ingredient_id, language_id) VALUES (10, 'Tomato',          100, 4, 1);
INSERT INTO ingredient_name (id, name, relevance_score, ingredient_id, language_id) VALUES (11, 'Paradicsom',      100, 4, 2);

-- Onion
INSERT INTO ingredient_name (id, name, relevance_score, ingredient_id, language_id) VALUES (13, 'Onion',           100, 5, 1);
INSERT INTO ingredient_name (id, name, relevance_score, ingredient_id, language_id) VALUES (14, 'Hagyma',          100, 5, 2);

-- Milk
INSERT INTO ingredient_name (id, name, relevance_score, ingredient_id, language_id) VALUES (16, 'Milk',            100, 6, 1);
INSERT INTO ingredient_name (id, name, relevance_score, ingredient_id, language_id) VALUES (17, 'Tej',             100, 6, 2);

-- Egg
INSERT INTO ingredient_name (id, name, relevance_score, ingredient_id, language_id) VALUES (19, 'Egg',             100, 7, 1);
INSERT INTO ingredient_name (id, name, relevance_score, ingredient_id, language_id) VALUES (20, 'Tojás',           100, 7, 2);

-- Sugar
INSERT INTO ingredient_name (id, name, relevance_score, ingredient_id, language_id) VALUES (22, 'Sugar',           100, 8, 1);
INSERT INTO ingredient_name (id, name, relevance_score, ingredient_id, language_id) VALUES (23, 'Cukor',           100, 8, 2);

-- Bean
INSERT INTO ingredient_name (id, name, relevance_score, ingredient_id, language_id) VALUES (24, 'Bab',             100, 9, 1);
INSERT INTO ingredient_name (id, name, relevance_score, ingredient_id, language_id) VALUES (25, 'Bean',            100, 9, 2);

-- Flour
INSERT INTO ingredient_name (id, name, relevance_score, ingredient_id, language_id) VALUES (26, 'Flour',           100, 10, 1);
INSERT INTO ingredient_name (id, name, relevance_score, ingredient_id, language_id) VALUES (27, 'Liszt',           100, 10, 2);

-- Potato
INSERT INTO ingredient_name (id, name, relevance_score, ingredient_id, language_id) VALUES (28, 'Potato',          100, 11, 1);
INSERT INTO ingredient_name (id, name, relevance_score, ingredient_id, language_id) VALUES (29, 'Burgonya',        100, 11, 2);

-- Sour cream
INSERT INTO ingredient_name (id, name, relevance_score, ingredient_id, language_id) VALUES (30, 'Sour Cream',      100, 12, 1);
INSERT INTO ingredient_name (id, name, relevance_score, ingredient_id, language_id) VALUES (31, 'Tejföl',          100, 12, 2);

-- Carrot
INSERT INTO ingredient_name (id, name, relevance_score, ingredient_id, language_id) VALUES (32, 'Carrot',          100, 13, 1);
INSERT INTO ingredient_name (id, name, relevance_score, ingredient_id, language_id) VALUES (33, 'Sárgarépa',       100, 13, 2);

-- Bell pepper
INSERT INTO ingredient_name (id, name, relevance_score, ingredient_id, language_id) VALUES (34, 'Bell Pepper',     100, 14, 1);
INSERT INTO ingredient_name (id, name, relevance_score, ingredient_id, language_id) VALUES (35, 'Paprika',         100, 14, 2);

-- Rice
INSERT INTO ingredient_name (id, name, relevance_score, ingredient_id, language_id) VALUES (36, 'Rice',            100, 15, 1);
INSERT INTO ingredient_name (id, name, relevance_score, ingredient_id, language_id) VALUES (37, 'Rizs',            100, 15, 2);

-- Mushroom
INSERT INTO ingredient_name (id, name, relevance_score, ingredient_id, language_id) VALUES (38, 'Mushroom',        100, 16, 1);
INSERT INTO ingredient_name (id, name, relevance_score, ingredient_id, language_id) VALUES (39, 'Gomba',           100, 16, 2);

-- Butter
INSERT INTO ingredient_name (id, name, relevance_score, ingredient_id, language_id) VALUES (40, 'Butter',          100, 17, 1);
INSERT INTO ingredient_name (id, name, relevance_score, ingredient_id, language_id) VALUES (41, 'Vaj',             100, 17, 2);

-- Apple
INSERT INTO ingredient_name (id, name, relevance_score, ingredient_id, language_id) VALUES (42, 'Apple',           100, 18, 1);
INSERT INTO ingredient_name (id, name, relevance_score, ingredient_id, language_id) VALUES (43, 'Alma',            100, 18, 2);

-- Cucumber
INSERT INTO ingredient_name (id, name, relevance_score, ingredient_id, language_id) VALUES (44, 'Cucumber',        100, 19, 1);
INSERT INTO ingredient_name (id, name, relevance_score, ingredient_id, language_id) VALUES (45, 'Uborka',          100, 19, 2);

-- Vinegar
INSERT INTO ingredient_name (id, name, relevance_score, ingredient_id, language_id) VALUES (46, 'Vinegar',         100, 20, 1);
INSERT INTO ingredient_name (id, name, relevance_score, ingredient_id, language_id) VALUES (47, 'Ecet',            100, 20, 2);

-- Dill
INSERT INTO ingredient_name (id, name, relevance_score, ingredient_id, language_id) VALUES (48, 'Dill',            100, 21, 1);
INSERT INTO ingredient_name (id, name, relevance_score, ingredient_id, language_id) VALUES (49, 'Kapor',           100, 21, 2);


-- Alternative names
INSERT INTO ingredient_alternative_name (id, name, ingredient_name_id) VALUES (1, 'Chicken Fillet',   1); -- alt for EN chicken breast
INSERT INTO ingredient_alternative_name (id, name, ingredient_name_id) VALUES (2, 'Chicken Cutlet',   1);
INSERT INTO ingredient_alternative_name (id, name, ingredient_name_id) VALUES (3, 'Csirkemellfilé',   2); -- alt for HU chicken breast
INSERT INTO ingredient_alternative_name (id, name, ingredient_name_id) VALUES (4, 'Wild Garlic',      4); -- alt for EN garlic
INSERT INTO ingredient_alternative_name (id, name, ingredient_name_id) VALUES (5, 'Plum Tomato',      10); -- alt for EN tomato
INSERT INTO ingredient_alternative_name (id, name, ingredient_name_id) VALUES (6, 'Cherry Tomato',    10);
INSERT INTO ingredient_alternative_name (id, name, ingredient_name_id) VALUES (7, 'Koktélparadicsom', 11); -- alt for HU tomato

-- Recipes
INSERT INTO recipe (id, name, cooking_time, num_of_ingredients, source_page_id) VALUES (1, 'Garlic Chicken',         2, 3, 1);
INSERT INTO recipe (id, name, cooking_time, num_of_ingredients, source_page_id) VALUES (2, 'Tomato & Onion Salad',   0, 3, 1);
INSERT INTO recipe (id, name, cooking_time, num_of_ingredients, source_page_id) VALUES (3, 'Fokhagymás csirkemell',  2, 3, 2);
INSERT INTO recipe (id, name, cooking_time, num_of_ingredients, source_page_id) VALUES (4, 'Custard',                1, 3, 1);
INSERT INTO recipe (id, name, cooking_time, num_of_ingredients, source_page_id) VALUES (5,  'Onion with beans',       0, 2, 1);
-- EN - AllRecipes
INSERT INTO recipe (id, name, cooking_time, num_of_ingredients, source_page_id) VALUES (6,  'Tomato Soup',            1, 4, 3);
INSERT INTO recipe (id, name, cooking_time, num_of_ingredients, source_page_id) VALUES (7,  'Pancakes',               0, 4, 3);
-- HU - Mindmegette
INSERT INTO recipe (id, name, cooking_time, num_of_ingredients, source_page_id) VALUES (8,  'Paradicsomleves',        1, 4, 2);
INSERT INTO recipe (id, name, cooking_time, num_of_ingredients, source_page_id) VALUES (9,  'Tejberizs',              0, 3, 2);
INSERT INTO recipe (id, name, cooking_time, num_of_ingredients, source_page_id) VALUES (10, 'Krumplifőzelék',         1, 4, 2);
INSERT INTO recipe (id, name, cooking_time, num_of_ingredients, source_page_id) VALUES (11, 'Rakott krumpli',         2, 4, 2);
INSERT INTO recipe (id, name, cooking_time, num_of_ingredients, source_page_id) VALUES (12, 'Savanyú uborkasaláta',  0, 3, 2);
-- HU - Street Kitchen
INSERT INTO recipe (id, name, cooking_time, num_of_ingredients, source_page_id) VALUES (13, 'Lecsó',                  1, 4, 4);
INSERT INTO recipe (id, name, cooking_time, num_of_ingredients, source_page_id) VALUES (14, 'Rántott csirkemell',     1, 4, 4);
INSERT INTO recipe (id, name, cooking_time, num_of_ingredients, source_page_id) VALUES (15, 'Gombaleves',             1, 4, 4);
INSERT INTO recipe (id, name, cooking_time, num_of_ingredients, source_page_id) VALUES (16, 'Almás rétes',            2, 4, 4);

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

-- Custard (EN)
INSERT INTO recipe_ingredient (id, ingredient_id, recipe_id) VALUES (10, 6, 4); -- milk
INSERT INTO recipe_ingredient (id, ingredient_id, recipe_id) VALUES (11, 7, 4); -- egg
INSERT INTO recipe_ingredient (id, ingredient_id, recipe_id) VALUES (12, 8, 4); -- sugar

-- Onion with beans (EN)
INSERT INTO recipe_ingredient (id, ingredient_id, recipe_id) VALUES (13, 5,  5); -- onion
INSERT INTO recipe_ingredient (id, ingredient_id, recipe_id) VALUES (14, 9,  5); -- bean

-- Tomato Soup (EN) — overlaps: tomato, onion, garlic, olive oil
INSERT INTO recipe_ingredient (id, ingredient_id, recipe_id) VALUES (15, 4,  6); -- tomato
INSERT INTO recipe_ingredient (id, ingredient_id, recipe_id) VALUES (16, 5,  6); -- onion
INSERT INTO recipe_ingredient (id, ingredient_id, recipe_id) VALUES (17, 2,  6); -- garlic
INSERT INTO recipe_ingredient (id, ingredient_id, recipe_id) VALUES (18, 3,  6); -- olive oil

-- Pancakes (EN) — overlaps: milk, egg, sugar
INSERT INTO recipe_ingredient (id, ingredient_id, recipe_id) VALUES (19, 6,  7); -- milk
INSERT INTO recipe_ingredient (id, ingredient_id, recipe_id) VALUES (20, 7,  7); -- egg
INSERT INTO recipe_ingredient (id, ingredient_id, recipe_id) VALUES (21, 8,  7); -- sugar
INSERT INTO recipe_ingredient (id, ingredient_id, recipe_id) VALUES (22, 10, 7); -- flour

-- Paradicsomleves (HU) — overlaps: tomato, onion, garlic, bell pepper
INSERT INTO recipe_ingredient (id, ingredient_id, recipe_id) VALUES (23, 4,  8); -- tomato
INSERT INTO recipe_ingredient (id, ingredient_id, recipe_id) VALUES (24, 5,  8); -- onion
INSERT INTO recipe_ingredient (id, ingredient_id, recipe_id) VALUES (25, 2,  8); -- garlic
INSERT INTO recipe_ingredient (id, ingredient_id, recipe_id) VALUES (26, 14, 8); -- bell pepper

-- Tejberizs (HU) — overlaps: milk, sugar
INSERT INTO recipe_ingredient (id, ingredient_id, recipe_id) VALUES (27, 6,  9); -- milk
INSERT INTO recipe_ingredient (id, ingredient_id, recipe_id) VALUES (28, 8,  9); -- sugar
INSERT INTO recipe_ingredient (id, ingredient_id, recipe_id) VALUES (29, 15, 9); -- rice

-- Krumplifőzelék (HU) — overlaps: onion, sour cream, carrot
INSERT INTO recipe_ingredient (id, ingredient_id, recipe_id) VALUES (30, 11, 10); -- potato
INSERT INTO recipe_ingredient (id, ingredient_id, recipe_id) VALUES (31, 5,  10); -- onion
INSERT INTO recipe_ingredient (id, ingredient_id, recipe_id) VALUES (32, 13, 10); -- carrot
INSERT INTO recipe_ingredient (id, ingredient_id, recipe_id) VALUES (33, 12, 10); -- sour cream

-- Rakott krumpli (HU) — overlaps: potato, egg, sour cream, onion
INSERT INTO recipe_ingredient (id, ingredient_id, recipe_id) VALUES (34, 11, 11); -- potato
INSERT INTO recipe_ingredient (id, ingredient_id, recipe_id) VALUES (35, 7,  11); -- egg
INSERT INTO recipe_ingredient (id, ingredient_id, recipe_id) VALUES (36, 12, 11); -- sour cream
INSERT INTO recipe_ingredient (id, ingredient_id, recipe_id) VALUES (37, 5,  11); -- onion

-- Savanyú uborkasaláta (HU) — isolated: no ingredients shared with any other recipe
INSERT INTO recipe_ingredient (id, ingredient_id, recipe_id) VALUES (38, 19, 12); -- cucumber
INSERT INTO recipe_ingredient (id, ingredient_id, recipe_id) VALUES (39, 20, 12); -- vinegar
INSERT INTO recipe_ingredient (id, ingredient_id, recipe_id) VALUES (40, 21, 12); -- dill

-- Lecsó (HU) — overlaps: tomato, onion, bell pepper, olive oil
INSERT INTO recipe_ingredient (id, ingredient_id, recipe_id) VALUES (41, 4,  13); -- tomato
INSERT INTO recipe_ingredient (id, ingredient_id, recipe_id) VALUES (42, 5,  13); -- onion
INSERT INTO recipe_ingredient (id, ingredient_id, recipe_id) VALUES (43, 14, 13); -- bell pepper
INSERT INTO recipe_ingredient (id, ingredient_id, recipe_id) VALUES (44, 3,  13); -- olive oil

-- Rántott csirkemell (HU) — overlaps: chicken breast, egg, flour
INSERT INTO recipe_ingredient (id, ingredient_id, recipe_id) VALUES (45, 1,  14); -- chicken breast
INSERT INTO recipe_ingredient (id, ingredient_id, recipe_id) VALUES (46, 7,  14); -- egg
INSERT INTO recipe_ingredient (id, ingredient_id, recipe_id) VALUES (47, 10, 14); -- flour
INSERT INTO recipe_ingredient (id, ingredient_id, recipe_id) VALUES (48, 12, 14); -- sour cream

-- Gombaleves (HU) — overlaps: onion, carrot, garlic
INSERT INTO recipe_ingredient (id, ingredient_id, recipe_id) VALUES (49, 16, 15); -- mushroom
INSERT INTO recipe_ingredient (id, ingredient_id, recipe_id) VALUES (50, 5,  15); -- onion
INSERT INTO recipe_ingredient (id, ingredient_id, recipe_id) VALUES (51, 13, 15); -- carrot
INSERT INTO recipe_ingredient (id, ingredient_id, recipe_id) VALUES (52, 2,  15); -- garlic

-- Almás rétes (HU) — overlaps: sugar, flour
INSERT INTO recipe_ingredient (id, ingredient_id, recipe_id) VALUES (53, 18, 16); -- apple
INSERT INTO recipe_ingredient (id, ingredient_id, recipe_id) VALUES (54, 8,  16); -- sugar
INSERT INTO recipe_ingredient (id, ingredient_id, recipe_id) VALUES (55, 10, 16); -- flour
INSERT INTO recipe_ingredient (id, ingredient_id, recipe_id) VALUES (56, 17, 16); -- butter


SELECT setval('language_id_seq', (SELECT MAX(id) FROM ingredient));
SELECT setval('source_page_id_seq', (SELECT MAX(id) FROM ingredient));
SELECT setval('ingredient_id_seq', (SELECT MAX(id) FROM ingredient));
SELECT setval('ingredient_name_id_seq', (SELECT MAX(id) FROM ingredient_name));
SELECT setval('ingredient_alternative_name_id_seq', (SELECT MAX(id) FROM ingredient_name));
SELECT setval('recipe_id_seq', (SELECT MAX(id) FROM ingredient_name));
SELECT setval('recipe_ingredient_id_seq', (SELECT MAX(id) FROM ingredient_name));