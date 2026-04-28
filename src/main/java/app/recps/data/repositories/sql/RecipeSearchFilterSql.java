package app.recps.data.repositories.sql;

import app.recps.rest.requests.RecipeSearchRequest;

class RecipeSearchFilterSql {
    private final RecipeSearchRequest request;

    public RecipeSearchFilterSql(RecipeSearchRequest request) {
        this.request = request;
    }

    public String byIncludedIngredients() {
        return IncludedIngredientsSql.filterBy(request.includedIngredientGroups);
    }

    public String byExcludedIngredients() {
        return ExcludedIngredientsSql.filterBy(request.excludedIngredients);
    }

    public String byName() {
        if (request.filterByName == null || request.filterByName.isEmpty()) {
            return "";
        }

        return "r.name ILIKE :filterByName";
    }

    public String byPrepTime() {
        if (request.prepTime == null) {
            return "";
        }

        if (request.prepTime.min() != null && request.prepTime.max() != null) {
            return "r.cooking_time BETWEEN :prepTimeMin AND :prepTimeMax";
        } else if (request.prepTime.min() != null) {
            return "r.cooking_time >= :prepTimeMin";
        } else if (request.prepTime.max() != null) {
            return "r.cooking_time <= :prepTimeMax";
        }

        return "";
    }
}
