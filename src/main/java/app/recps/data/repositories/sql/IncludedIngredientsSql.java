package app.recps.data.repositories.sql;

import app.recps.rest.requests.RecipeSearchRequest;
import app.recps.rest.requests.RecipeSearchRequest.IngredientGroupWithRelation;

import java.util.List;
import java.util.stream.Collectors;

class IncludedIngredientsSql {
    public static String filterBy(List<IngredientGroupWithRelation> includedIngredientGroups) {
        if (includedIngredientGroups == null || includedIngredientGroups.isEmpty()) {
            return "";
        }

        var conditionsSize = includedIngredientGroups.size();

        // r.id in (subquery_1) <AND / OR> r.id in (subquery_2) ...
        var conditionsWithRelations = includedIngredientGroups.subList(0, conditionsSize - 1);
        var conditionsWithRelationsSql = conditionsWithRelations.stream()
                .map(c -> "r.id IN (" + subQueryFor(c.group()) + ") " + c.relation() + " ")
                .collect(Collectors.joining());

        // relation is ignored for last condition
        var lastCondition = includedIngredientGroups.get(conditionsSize - 1);
        var lastConditionSql = "r.id IN (" + subQueryFor(lastCondition.group()) + ") ";

        return conditionsWithRelationsSql + lastConditionSql;
    }

    private static String subQueryFor(RecipeSearchRequest.IngredientGroup ingredientGroup) {
        if(Boolean.TRUE.equals(ingredientGroup.asPercent())) {
            return subQueryForMinMatchAsPercent(ingredientGroup);
        } else {
            return subQueryForMinMatchAsCount(ingredientGroup);
        }
    }

    private static String subQueryForMinMatchAsCount(RecipeSearchRequest.IngredientGroup ingredientGroup) {
        var ingredientIds = ingredientGroup.ids().stream().map(Object::toString)
                .collect(Collectors.joining(","));

        return "SELECT ri.recipe_id FROM recipe_ingredient ri " +
                "WHERE ri.ingredient_id IN (" + ingredientIds + ") " +
                "GROUP BY ri.recipe_id " +
                "HAVING COUNT(ri.ingredient_id) >= " + ingredientGroup.minMatch();
    }

    private static String subQueryForMinMatchAsPercent(RecipeSearchRequest.IngredientGroup ingredientGroup) {
        var percentNormalized = Math.max(0, ingredientGroup.minMatch());
        percentNormalized = Math.min(100, percentNormalized);

        var ingredientIds = ingredientGroup.ids().stream().map(Object::toString)
                .collect(Collectors.joining(","));

        return "SELECT re.id FROM recipe re " +
                "JOIN recipe_ingredient ri ON re.id = ri.recipe_id " +
                "WHERE ri.ingredient_id IN (" + ingredientIds + ") " +
                "GROUP BY re.id " +
                "HAVING ((COUNT(ri.ingredient_id) * 1.0) / (re.num_of_ingredients * 1.0)) * 100 >= " + percentNormalized;
    }
}
