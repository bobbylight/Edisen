package org.fife.edisen.ui;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.InvocationInterceptor;
import org.junit.jupiter.api.extension.ReflectiveInvocationContext;

import javax.swing.*;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Runs Swing tests on the EDT.
 */
public class SwingRunnerExtension implements InvocationInterceptor {

    @Override
    public void interceptTestMethod(InvocationInterceptor.Invocation<Void> invocation,
                                    ReflectiveInvocationContext<Method> invocationContext,
                                    ExtensionContext extensionContext) throws Throwable {

        AtomicReference<Throwable> throwable = new AtomicReference<>();

        SwingUtilities.invokeAndWait(() -> {
            try {
                invocation.proceed();
            } catch (Throwable t) {
                throwable.set(t);
            }
        });
        Throwable t = throwable.get();
        if (t != null) {
            throw t;
        }
    }
}
