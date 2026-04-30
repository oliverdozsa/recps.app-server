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

    public String bySourcePages() {
        if (request.sourcePages == null || request.sourcePages.isEmpty()) {
            return "";
        }

        var ids = request.sourcePages.stream().map(Object::toString).collect(java.util.stream.Collectors.joining(","));
        return "r.source_page_id IN (" + ids + ")";
    }

    public String byCountIngredients() {
        if (request.countIngredients == null) {
            return "";
        }

        if (request.countIngredients.min() != null && request.countIngredients.max() != null) {
            return "r.num_of_ingredients BETWEEN :countIngredientsMin AND :countIngredientsMax";
        } else if (request.countIngredients.min() != null) {
            return "r.num_of_ingredients >= :countIngredientsMin";
        } else if (request.countIngredients.max() != null) {
            return "r.num_of_ingredients <= :countIngredientsMax";
        }

        return "";
    }
}
