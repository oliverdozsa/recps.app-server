package app.recps.testbases;

import io.quarkus.logging.Log;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class TestLoggingExtension implements BeforeEachCallback, AfterEachCallback {

    @Override
    public void beforeEach(ExtensionContext ctx) {
        Log.infof(">>> %s.%s", ctx.getRequiredTestClass().getSimpleName(), ctx.getRequiredTestMethod().getName());
    }

    @Override
    public void afterEach(ExtensionContext ctx) {
        Log.infof("<<< %s.%s [%s]", ctx.getRequiredTestClass().getSimpleName(),
                ctx.getRequiredTestMethod().getName(),
                ctx.getExecutionException().isPresent() ? "FAILED" : "PASSED");
    }
}
