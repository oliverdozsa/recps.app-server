package app.recps.rest;

import app.recps.rest.requests.RecipeSearchRequest;
import app.recps.rest.requests.RecipeSearchRequest.IngredientGroup;
import app.recps.rest.requests.RecipeSearchRequest.IngredientGroupRelation;
import app.recps.rest.requests.RecipeSearchRequest.IngredientGroupWithRelation;
import app.recps.rest.responses.RecipeSearchResponse;
import app.recps.testbases.RecpsAppTestBase;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class RecipeSearchByExcludedIngredientsTest extends RecpsAppTestBase {
    @Test
    public void excludedIngredientsOnly() {
        var byQuery = new RecipeSearchRequest();

        // Excluded: chicken breast, garlic, olive oil
        byQuery.excludedIngredients = List.of(1L, 2L, 3L);

        var response = rest.recipes.search(byQuery);

        assertThat(response.items(), hasSize(8));
        assertThat(response.totalCount(), is(8L));
        var recipeNames = response.items().stream().map(RecipeSearchResponse::name).toList();
        assertThat(recipeNames, not(containsInAnyOrder("Garlic Chicken", "Tomato & Onion Salad", "Fokhagymás csirkemell",
                "Tomato Soup", "Paradicsomleves", "Lecsó", "Rántott csirkemell", "Gombaleves")));
    }

    @Test
    public void excludedIngredientRemovesSubsetOfIncludedResults() {
        var byQuery = new RecipeSearchRequest();

        // Included: chicken breast → Garlic Chicken, Fokhagymás csirkemell, Rántott csirkemell
        byQuery.includedIngredientGroups = List.of(
                new IngredientGroupWithRelation(IngredientGroup.of(1, 1L), IngredientGroupRelation.OR)
        );
        // Excluded: egg → removes Rántott csirkemell
        byQuery.excludedIngredients = List.of(7L);

        var response = rest.recipes.search(byQuery);

        assertThat(response.items(), hasSize(2));
        assertThat(response.totalCount(), is(2L));
        var recipeNames = response.items().stream().map(RecipeSearchResponse::name).toList();
        assertThat(recipeNames, containsInAnyOrder("Garlic Chicken", "Fokhagymás csirkemell"));
    }

    @Test
    public void excludedIngredientRemovesRecipesFromLargerIncludedSet() {
        var byQuery = new RecipeSearchRequest();

        // Included: onion → 8 recipes
        byQuery.includedIngredientGroups = List.of(
                new IngredientGroupWithRelation(IngredientGroup.of(1, 5L), IngredientGroupRelation.OR)
        );
        // Excluded: potato → removes Krumplifőzelék, Rakott krumpli
        byQuery.excludedIngredients = List.of(11L);

        var response = rest.recipes.search(byQuery);

        assertThat(response.items(), hasSize(6));
        assertThat(response.totalCount(), is(6L));
        var recipeNames = response.items().stream().map(RecipeSearchResponse::name).toList();
        assertThat(recipeNames, containsInAnyOrder(
                "Tomato & Onion Salad", "Onion with beans", "Tomato Soup",
                "Paradicsomleves", "Lecsó", "Gombaleves"));
    }

    @Test
    public void includedAndExcludedAreNotDistinctExcludeOverrides() {
        var byQuery = new RecipeSearchRequest();

        // Included: tomato, garlic → 6 garlic recipes + 5 tomato
        byQuery.includedIngredientGroups = List.of(
                new IngredientGroupWithRelation(IngredientGroup.of(1, 4L, 2L), IngredientGroupRelation.OR)
        );
        // Excluded: bell pepper, garlic → removes 6 garlic + 2 bell pepper recipes
        byQuery.excludedIngredients = List.of(14L, 2L);

        var response = rest.recipes.search(byQuery);

        assertThat(response.items(), hasSize(1));
        assertThat(response.totalCount(), is(1L));
        var recipeNames = response.items().stream().map(RecipeSearchResponse::name).toList();
        assertThat(recipeNames, containsInAnyOrder("Tomato & Onion Salad"));
    }
}
