package app.recps.data.repositories.sql;

import app.recps.rest.requests.RecipeSearchRequest;

public class RecipeSearchSql {
    private RecipeSearchRequest request;

    public static String forSearch(RecipeSearchRequest request) {
        var instance = new RecipeSearchSql();
        instance.request = request;

        return instance.build();
    }

    public static String forCount(RecipeSearchRequest request) {
        var instance = new RecipeSearchSql();
        instance.request = request;

        return instance.buildForCount();
    }

    private String build() {
        return buildBase(false) +
                orderBy() +
                " LIMIT " + request.limit +
                " OFFSET " + request.limit * request.page;
    }

    private String orderBy() {
        if (request.orderBy == null) return "";
        var direction = request.orderDirection != null ? request.orderDirection.name() : "ASC";
        return " ORDER BY " + request.orderBy.column + " " + direction;
    }

    private String buildForCount() {
        return buildBase(true);
    }

    private String buildBase(boolean isForCount) {
        var filter = new RecipeSearchFilterSql(request);
        return "SELECT " + (isForCount ? "COUNT(" : "") + "*" + (isForCount ? ")" : "") + " from recipe r " + (isAnyFilterUsed() ? "WHERE " : "") +
                filter.byIncludedIngredients() + ANDBeforeExcluded() +
                filter.byExcludedIngredients() + ANDBeforeName() +
                filter.byName() + ANDBeforePrepTime() +
                filter.byPrepTime() + ANDBeforeCountIngredients() +
                filter.byCountIngredients();
    }

    private boolean isAnyFilterUsed() {
        return isIncludedIngredientsUsed() || isExcludedIngredientsUsed() || isFilterByNameUsed() || isPrepTimeUsed() || isCountIngredientUsed();
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

    private boolean isPrepTimeUsed() {
        return request.prepTime != null && (request.prepTime.min() != null || request.prepTime.max() != null);
    }

    private boolean isCountIngredientUsed() {
        return request.countIngredients != null && (request.countIngredients.min() != null || request.countIngredients.max() != null);
    }

    private String ANDBeforeExcluded() {
        return isIncludedIngredientsUsed() && isExcludedIngredientsUsed() ? "AND " : "";
    }

    private String ANDBeforeName() {
        return (isIncludedIngredientsUsed() || isExcludedIngredientsUsed())
                && isFilterByNameUsed() ? "AND " : "";
    }

    private String ANDBeforePrepTime() {
        return (isIncludedIngredientsUsed() || isExcludedIngredientsUsed() || isFilterByNameUsed())
                && isPrepTimeUsed() ? "AND " : "";
    }

    private String ANDBeforeCountIngredients() {
        return (isIncludedIngredientsUsed() || isExcludedIngredientsUsed() || isFilterByNameUsed() || isPrepTimeUsed())
                && isCountIngredientUsed() ? "AND " : "";
    }

    private RecipeSearchSql() {
    }
}
