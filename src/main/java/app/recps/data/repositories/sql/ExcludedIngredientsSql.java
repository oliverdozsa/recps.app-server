package app.recps.data.repositories.sql;

import java.util.List;
import java.util.stream.Collectors;

class ExcludedIngredientsSql {
    public static String filterBy(List<Long> excludedIngredients) {
        if (excludedIngredients == null || excludedIngredients.isEmpty()) {
            return "";
        }

        return "r.id NOT IN(" + subqueryFor(excludedIngredients) + ") ";
    }

    private static String subqueryFor(List<Long> excludedIngredients) {
        var ingredientIds = excludedIngredients.stream().map(Object::toString)
                .collect(Collectors.joining(","));

        return "SELECT ri.recipe_id FROM recipe_ingredient ri " +
                "WHERE ri.ingredient_id IN (" + ingredientIds + ") " +
                "GROUP BY ri.recipe_id ";
    }
}
