package app.recps.data.repositories.sql;

import app.recps.rest.requests.RecipeSearchRequest;

public class RecipeSearchSql {
    private RecipeSearchRequest request;

    public static String from(RecipeSearchRequest request) {
        var instance = new RecipeSearchSql();
        instance.request = request;

        return instance.build();
    }

    private String build() {
        var filter = new RecipeSearchFilterSql(request);
        return "SELECT * from recipe r " + (isAnyFilterUsed() ? "WHERE " : "") +
                filter.byIncludedIngredients() + ANDBeforeExcluded() +
                filter.byExcludedIngredients() + ANDBeforeName() +
                filter.byName();
    }

    private boolean isAnyFilterUsed() {
        return isIncludedIngredientsUsed() || isExcludedIngredientsUsed() || isFilterByNameUsed();
    }

    private boolean isIncludedIngredientsUsed() {
        return request.includedIngredientGroups != null && !request.includedIngredientGroups.isEmpty();
    }

    private boolean isExcludedIngredientsUsed() {
        return request.excludedIngredients != null && !request.excludedIngredients.isEmpty();
    }

    private boolean isFilterByNameUsed() {
        return request.filterByName != null && !request.filterByName.isEmpty();
    }

    private String ANDBeforeExcluded() {
        return isIncludedIngredientsUsed() && isExcludedIngredientsUsed() ? "AND " : "";
    }

    private String ANDBeforeName() {
        return (isIncludedIngredientsUsed() || isExcludedIngredientsUsed())
                && isFilterByNameUsed() ? "AND " : "";
    }

    private RecipeSearchSql() {
    }
}
