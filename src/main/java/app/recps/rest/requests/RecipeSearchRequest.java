package app.recps.rest.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class RecipeSearchRequest {
    public List<@Valid IngredientGroupWithRelation> includedIngredientGroups;
    public List<Long> excludedIngredients;
    public String filterByName;

    @NotNull
    public Long ingredientLanguageId;

    @Min(1)
    @Max(25)
    public Integer limit = 15;

    @Min(0)
    public Long page = 0L;

    public OrderBy orderBy;
    public OrderDirection orderDirection;
    public PrepTime prepTime;
    public CountIngredients countIngredients;
    public List<Long> sourcePages;

    @Override
    public String toString() {
        return "{" +
                "\"includedIngredientGroups\": " + includedIngredientGroups + ", " +
                "\"excludedIngredients\": " + excludedIngredients + ", " +
                "\"filterByName\": " + "\"" + filterByName + "\"" + ", " +
                "\"ingredientLanguageId\": " + "\"" + ingredientLanguageId + "\"" + ", " +
                "\"limit\": " + "\"" + limit + "\"" + ", " +
                "\"page\": " + "\"" + page + "\"" + ", " +
                "\"orderBy\": " + "\"" + orderBy + "\"" + ", " +
                "\"orderDirection\": " + "\"" + orderDirection + "\"" + ", " +
                "\"prepTime\": " + prepTime + "\", " +
                "\"countIngredients\": " + countIngredients + "\", " +
                "\"sourcePages\": " + sourcePages +
                "}";
    }

    public record CountIngredients(Integer min, Integer max) {
        @Override
        public String toString() {
            return "{\"min\":" + min + ",\"max\":" + max + "}";
        }
    }

    public record PrepTime(Integer min, Integer max) {
        @Override
        public String toString() {
            return "{\"min\":" + min + ",\"max\":" + max + "}";
        }
    }

    public record IngredientGroupWithRelation(@Valid IngredientGroup group, IngredientGroupRelation relation) {
        public IngredientGroupWithRelation {
            if (relation == null) relation = IngredientGroupRelation.AND;
        }

        @Override
        public String toString() {
            return "{\"group\":" + group + ",\"relation\":\"" + relation + "\"}";
        }
    }

    public record IngredientGroup(@NotEmpty List<Long> ids, @Min(1) @NotNull Integer minMatch, Boolean asPercent) {
        public static IngredientGroup of(Integer minMatch, Long... id) {
            return new IngredientGroup(List.of(id), minMatch, false);
        }

        @Override
        public String toString() {
            return "{\"ids\":" + ids + ",\"minMatch\":" + minMatch + ",\"asPercent\":" + Boolean.TRUE.equals(asPercent) + "}";
        }
    }

    public enum IngredientGroupRelation {
        AND("AND"),
        OR("OR");

        private final String asString;

        IngredientGroupRelation(String asString) {
            this.asString = asString;
        }

        @Override
        public String toString() {
            return this.asString;
        }
    }

    public enum OrderBy {
        @JsonProperty("prepTime")
        PREP_TIME("cooking_time"),

        @JsonProperty("ingredientCount")
        INGREDIENT_COUNT("num_of_ingredients");

        public final String column;

        OrderBy(String column) {
            this.column = column;
        }
    }

    public enum OrderDirection {
        @JsonProperty("asc")
        ASC,

        @JsonProperty("desc")
        DESC
    }
}
