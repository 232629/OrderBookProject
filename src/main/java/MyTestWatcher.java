import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

import java.util.Optional;

public class MyTestWatcher implements TestWatcher {

    @Override
    public void testAborted(ExtensionContext extensionContext, Throwable throwable) {
        System.out.println(extensionContext.getDisplayName() + ": ABORTED");

    }

    @Override
    public void testDisabled(ExtensionContext extensionContext, Optional<String> optional) {
        System.out.println(extensionContext.getDisplayName() + ": DISABLED");

    }

    @Override
    public void testFailed(ExtensionContext extensionContext, Throwable throwable) {
        System.out.println(extensionContext.getDisplayName() + ": FAILED");

    }

    @Override
    public void testSuccessful(ExtensionContext extensionContext) {
        System.out.println(extensionContext.getDisplayName() + ": SUCCESSFUL");

    }
}