package app.recps.data.repositories.sql;

import app.recps.rest.requests.RecipeSearchRequest;

public class RecipeSearchSql {
    private RecipeSearchRequest request;

    public static RecipeSearchSql from(RecipeSearchRequest request) {
        var instance = new RecipeSearchSql();
        instance.request = request;

        return instance;
    }

    public String build() {
        var filter = new RecipeSearchFilterSql(request);
        return "SELECT * from recipe r " + (isAnyFilterUsed() ? "WHERE " : "") +
                filter.byIncludedIngredients() + ANDBeforeExcluded() +
                filter.byExcludedIngredients();
    }

    private boolean isAnyFilterUsed() {
        return isIncludedIngredientsUsed() || isExcludedIngredientsUsed();
    }

    private boolean isIncludedIngredientsUsed() {
        return request.includedIngredientGroups != null && !request.includedIngredientGroups.isEmpty();
    }

    private boolean isExcludedIngredientsUsed() {
        return request.excludedIngredients != null && !request.excludedIngredients.isEmpty();
    }

    private String ANDBeforeExcluded() {
        return isIncludedIngredientsUsed() && isExcludedIngredientsUsed() ? "AND " : "";
    }

    private RecipeSearchSql() {
    }
}
