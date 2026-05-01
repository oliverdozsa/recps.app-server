package app.recps.rest;

import app.recps.rest.responses.SourcePageResponse;
import app.recps.testbases.RecpsAppTestBase;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class SourcePagesTest extends RecpsAppTestBase {
    @Test
    public void getSourcePagesReturnsAllSourcePages() {
        List<SourcePageResponse> results = rest.sourcePages.getAll();

        assertThat(results, hasSize(4));
    }

    @Test
    public void getSourcePagesReturnsCorrectNames() {
        List<SourcePageResponse> results = rest.sourcePages.getAll();

        var names = results.stream().map(SourcePageResponse::name).toList();
        assertThat(names, containsInAnyOrder("BBC Good Food", "Mindmegette", "AllRecipes", "Street Kitchen"));
    }

    @Test
    public void getSourcePagesReturnsCorrectIds() {
        List<SourcePageResponse> results = rest.sourcePages.getAll();

        var bbcGoodFood  = results.stream().filter(p -> "BBC Good Food".equals(p.name())).findFirst().orElseThrow();
        var mindmegette  = results.stream().filter(p -> "Mindmegette".equals(p.name())).findFirst().orElseThrow();
        var allRecipes   = results.stream().filter(p -> "AllRecipes".equals(p.name())).findFirst().orElseThrow();
        var streetKitchen = results.stream().filter(p -> "Street Kitchen".equals(p.name())).findFirst().orElseThrow();

        assertThat(bbcGoodFood.id(), is(1L));
        assertThat(mindmegette.id(), is(2L));
        assertThat(allRecipes.id(), is(3L));
        assertThat(streetKitchen.id(), is(4L));
    }

    @Test
    public void getSourcePagesReturnsCorrectLanguageIds() {
        List<SourcePageResponse> results = rest.sourcePages.getAll();

        // BBC Good Food and AllRecipes are English (language_id=1)
        var englishPages = results.stream().filter(p -> p.languageId() == 1L).toList();
        var englishNames = englishPages.stream().map(SourcePageResponse::name).toList();
        assertThat(englishNames, containsInAnyOrder("BBC Good Food", "AllRecipes"));

        // Mindmegette and Street Kitchen are Hungarian (language_id=2)
        var hungarianPages = results.stream().filter(p -> p.languageId() == 2L).toList();
        var hungarianNames = hungarianPages.stream().map(SourcePageResponse::name).toList();
        assertThat(hungarianNames, containsInAnyOrder("Mindmegette", "Street Kitchen"));
    }
}
