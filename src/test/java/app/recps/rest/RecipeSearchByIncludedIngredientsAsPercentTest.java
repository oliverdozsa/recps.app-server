package app.recps.rest;

import app.recps.testbases.RecpsAppTestBase;
import app.recps.rest.requests.RecipeSearchRequest;
import app.recps.rest.requests.RecipeSearchRequest.IngredientGroup;
import app.recps.rest.requests.RecipeSearchRequest.IngredientGroupWithRelation;
import app.recps.rest.responses.RecipeSearchResponse;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class RecipeSearchByIncludedIngredientsAsPercentTest extends RecpsAppTestBase {
    @Test
    public void singleGroupWith100PercentMatchReturnsOnlyRecipesFullyCoveredByTheGroup() {
        // Group: chicken, garlic, olive oil — minMatch 100% (as percent)
        // Only recipes whose every ingredient is in the group qualify:
        // - Garlic Chicken (r1):          3/3 = 100%  ✓
        // - Fokhagymás csirkemell (r3):   3/3 = 100%  ✓
        // - Tomato & Onion Salad (r2):    1/3 =  33%
        // - Tomato Soup (r6):             2/4 =  50%
        // - Lecsó (r13):                  1/4 =  25%
        var percentGroup = new IngredientGroup(List.of(1L, 2L, 3L), 100, true);
        var groupWithRelation = new IngredientGroupWithRelation(percentGroup, RecipeSearchRequest.IngredientGroupRelation.OR);

        var byQuery = new RecipeSearchRequest();
        byQuery.ingredientLanguageId = 1L;
        byQuery.includedIngredientGroups = List.of(groupWithRelation);

        var response = rest.recipes.search(byQuery);

        assertThat(response.items(), hasSize(2));
        assertThat(response.totalCount(), is(2L));
        var recipeNames = response.items().stream().map(RecipeSearchResponse::name).toList();
        assertThat(recipeNames, containsInAnyOrder("Garlic Chicken", "Fokhagymás csirkemell"));
    }

    @Test
    public void singleGroupWith50PercentMatch() {
        // Group: tomato, onion — minMatch 50% (as percent)
        // - Tomato & Onion Salad (r2): 2/3 = 66%  ✓
        // - Onion with beans (r5):     1/2 = 50%  ✓
        // - Tomato Soup (r6):          2/4 = 50%  ✓
        // - Paradicsomleves (r8):      2/4 = 50%  ✓
        // - Krumplifőzelék (r10):      1/4 = 25%
        // - Rakott krumpli (r11):      1/4 = 25%
        // - Lecsó (r13):               2/4 = 50%  ✓
        // - Gombaleves (r15):          1/4 = 25%
        var percentGroup = new IngredientGroup(List.of(4L, 5L), 50, true);
        var groupWithRelation = new IngredientGroupWithRelation(percentGroup, RecipeSearchRequest.IngredientGroupRelation.OR);

        var byQuery = new RecipeSearchRequest();
        byQuery.ingredientLanguageId = 1L;
        byQuery.includedIngredientGroups = List.of(groupWithRelation);

        var response = rest.recipes.search(byQuery);

        assertThat(response.items(), hasSize(5));
        assertThat(response.totalCount(), is(5L));
        var recipeNames = response.items().stream().map(RecipeSearchResponse::name).toList();
        assertThat(recipeNames, containsInAnyOrder(
                "Tomato & Onion Salad", "Onion with beans", "Tomato Soup", "Paradicsomleves", "Lecsó"));
    }

    @Test
    public void percentMatch100WithGroupThatNoRecipeIsFullyCoveredByReturnsEmpty() {
        // Group: chicken, garlic — no recipe consists exclusively of these two.
        // Recipes that contain any of them still have additional ingredients, so all fall below 100%.
        var percentGroup = new IngredientGroup(List.of(1L, 2L), 100, true);
        var groupWithRelation = new IngredientGroupWithRelation(percentGroup, RecipeSearchRequest.IngredientGroupRelation.OR);

        var byQuery = new RecipeSearchRequest();
        byQuery.ingredientLanguageId = 1L;
        byQuery.includedIngredientGroups = List.of(groupWithRelation);

        var response = rest.recipes.search(byQuery);

        assertThat(response.items(), hasSize(0));
        assertThat(response.totalCount(), is(0L));
    }

    @Test
    public void percentGreaterThan100IsClampedTo100() {
        // Group: chicken, garlic, olive oil with minMatch = 200 — should behave identically to 100%.
        var percentGroup = new IngredientGroup(List.of(1L, 2L, 3L), 200, true);
        var groupWithRelation = new IngredientGroupWithRelation(percentGroup, RecipeSearchRequest.IngredientGroupRelation.OR);

        var byQuery = new RecipeSearchRequest();
        byQuery.ingredientLanguageId = 1L;
        byQuery.includedIngredientGroups = List.of(groupWithRelation);

        var response = rest.recipes.search(byQuery);

        assertThat(response.items(), hasSize(2));
        var recipeNames = response.items().stream().map(RecipeSearchResponse::name).toList();
        assertThat(recipeNames, containsInAnyOrder("Garlic Chicken", "Fokhagymás csirkemell"));
    }

    @Test
    public void percentGroupCombinedWithCountGroupViaAnd() {
        // Percent group A: tomato, onion ≥ 50% → r2, r5, r6, r8, r13
        // Count   group B: olive oil (min 1)   → r1, r2, r3, r6, r13
        // AND intersection → r2, r6, r13
        var percentGroup = new IngredientGroupWithRelation(
                new IngredientGroup(List.of(4L, 5L), 50, true),
                RecipeSearchRequest.IngredientGroupRelation.AND);
        var countGroup = new IngredientGroupWithRelation(
                IngredientGroup.of(1, 3L),
                RecipeSearchRequest.IngredientGroupRelation.AND);

        var byQuery = new RecipeSearchRequest();
        byQuery.ingredientLanguageId = 1L;
        byQuery.includedIngredientGroups = List.of(percentGroup, countGroup);

        var response = rest.recipes.search(byQuery);

        assertThat(response.items(), hasSize(3));
        assertThat(response.totalCount(), is(3L));
        var recipeNames = response.items().stream().map(RecipeSearchResponse::name).toList();
        assertThat(recipeNames, containsInAnyOrder("Tomato & Onion Salad", "Tomato Soup", "Lecsó"));
    }

    @Test
    public void twoPercentGroupsWithOrRelationProducesUnion() {
        // Percent group A: chicken, garlic, olive oil ≥ 100% → Garlic Chicken (r1), Fokhagymás csirkemell (r3)
        // Percent group B: tomato, onion, garlic, bell pepper ≥ 75%:
        //   - Tomato Soup (r6):       3/4 = 75%  ✓
        //   - Paradicsomleves (r8):   4/4 = 100% ✓
        //   - Lecsó (r13):            3/4 = 75%  ✓
        //   - Tomato & Onion Salad (r2): 2/3 = 66%
        //   - Gombaleves (r15):       2/4 = 50%
        // OR union → r1, r3, r6, r8, r13
        var groupA = new IngredientGroupWithRelation(
                new IngredientGroup(List.of(1L, 2L, 3L), 100, true),
                RecipeSearchRequest.IngredientGroupRelation.OR);
        var groupB = new IngredientGroupWithRelation(
                new IngredientGroup(List.of(4L, 5L, 2L, 14L), 75, true),
                RecipeSearchRequest.IngredientGroupRelation.OR);

        var byQuery = new RecipeSearchRequest();
        byQuery.ingredientLanguageId = 1L;
        byQuery.includedIngredientGroups = List.of(groupA, groupB);

        var response = rest.recipes.search(byQuery);

        assertThat(response.items(), hasSize(5));
        assertThat(response.totalCount(), is(5L));
        var recipeNames = response.items().stream().map(RecipeSearchResponse::name).toList();
        assertThat(recipeNames, containsInAnyOrder(
                "Garlic Chicken", "Fokhagymás csirkemell", "Tomato Soup", "Paradicsomleves", "Lecsó"));
    }
}
