package app.recps.data.repositories;

import app.recps.rest.requests.RecipeSearchRequest.IngredientGroup;
import app.recps.rest.requests.RecipeSearchRequest.IngredientGroupWithRelation;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

class RecipesSearchSqlBuilder {
    private List<IngredientGroupWithRelation> includedIngredientsConditions = Collections.emptyList();

    public RecipesSearchSqlBuilder addIncludedIngredientsCondition(List<IngredientGroupWithRelation> groupsWithRelations) {
        if (groupsWithRelations != null) {
            this.includedIngredientsConditions = groupsWithRelations;
        }

        return this;
    }

    public String build() {
        return "SELECT * from recipe r WHERE " +
                buildIncludedIngredientsCondition();
    }

    private String buildIncludedIngredientsCondition() {
        if (includedIngredientsConditions.isEmpty()) {
            return "";
        }

        var conditionsSize = includedIngredientsConditions.size();

        // r.id in (subquery_1) <AND / OR> r.id in (subquery_2) ...
        var conditionsWithRelations = includedIngredientsConditions.subList(0, conditionsSize - 1);
        var conditionsWithRelationsJpql = conditionsWithRelations.stream()
                .map(c -> "r.id IN (" + subQueryForIngredientsGroup(c.group()) + ") " + c.relation() + " ")
                .collect(Collectors.joining());

        // no relation for last condition
        var lastCondition = includedIngredientsConditions.get(conditionsSize - 1);
        var lastConditionJpql = "r.id in (" + subQueryForIngredientsGroup(lastCondition.group()) + ")";

        return conditionsWithRelationsJpql + lastConditionJpql;
    }

    private String subQueryForIngredientsGroup(IngredientGroup ingredientGroup) {
        var ingredientIds = ingredientGroup.ids().stream().map(Object::toString)
                .collect(Collectors.joining(","));

        return "SELECT ri.recipe_id FROM recipe_ingredient ri " +
                "WHERE ri.ingredient_id in (" + ingredientIds + ") " +
                "GROUP BY ri.recipe_id " +
                "HAVING COUNT(ri.ingredient_id) > " + ingredientGroup.minMatch();
    }
}
