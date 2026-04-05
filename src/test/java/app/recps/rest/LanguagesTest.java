package app.recps.rest;

import app.recps.rest.responses.LanguageResponse;
import app.recps.testbases.RecpsAppTestBase;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class LanguagesTest extends RecpsAppTestBase {
    @Test
    public void getAllLanguagesReturnsAllLanguages() {
        List<LanguageResponse> results = rest.languages.getAll();

        assertThat(results, hasSize(2));

        var isoNames = results.stream().map(LanguageResponse::isoName).toList();
        assertThat(isoNames, containsInAnyOrder("en", "hu"));
    }

    @Test
    public void getAllLanguagesReturnsCorrectIds() {
        List<LanguageResponse> results = rest.languages.getAll();

        var en = results.stream().filter(l -> "en".equals(l.isoName())).findFirst().orElseThrow();
        var hu = results.stream().filter(l -> "hu".equals(l.isoName())).findFirst().orElseThrow();

        assertThat(en.id(), is(1L));
        assertThat(hu.id(), is(2L));
    }
}
