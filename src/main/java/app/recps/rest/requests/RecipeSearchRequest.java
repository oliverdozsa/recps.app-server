package app.recps.rest.requests;

import java.util.List;

public record RecipeSearchRequest(List<IngredientGroupWithRelation> includedIngredientGroups) {

    public record IngredientGroupWithRelation(IngredientGroup group, IngredientGroupRelation relation) {
    }

    public record IngredientGroup(List<Long> ids, Integer minMatch) {
        public static IngredientGroup of(Integer minMatch, Long ...id) {
            return new IngredientGroup(List.of(id), minMatch);
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
}
