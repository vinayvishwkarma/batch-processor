package com.finago.interview.task.helper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * A custom {@link Answer} implementation used in unit testing to detect unintended
 * calls to unmocked methods during test setup or teardown phases.
 *
 * <p>This class is particularly useful for enforcing strict mocking policies by failing tests
 * when a method is invoked without being explicitly stubbed during {@code @BeforeEach},
 * {@code @AfterEach}, {@code @BeforeAll}, or {@code @AfterAll} lifecycle methods.
 *
 * <p>To use it, pass an instance of this class as the {@code defaultAnswer} when creating
 * a mock with Mockito:
 * <pre>
 *     UnmockedMethodAnswer answer = new UnmockedMethodAnswer();
 *     answer.activate();
 *     MyService mock = Mockito.mock(MyService.class, answer);
 * </pre>
 *
 * <p>Call {@link #activate()} to start enforcing detection, and {@link #deactivate()} to stop.
 * This is useful for scoping it only to setup/teardown phases.
 *
 * <p>Example use case: Prevent accidental invocation of real methods during JUnit lifecycle stages.
 *
 */
public class UnmockedMethodAnswer implements Answer<Object> {

    private boolean active;

    public void activate() {
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }

    @Override
    public Object answer(InvocationOnMock invocation) throws Throwable {
        if (this.active && !invocation.getMethod().getName().equals("finalize") && this.calledFromBeforeAndAfter()) {
            Assert.fail("Unmocked method called: " + invocation.getMethod().getName() + " in class: " + invocation.getMethod().getDeclaringClass().getName());
        }
        return Mockito.RETURNS_DEFAULTS.answer(invocation);
    }

    private boolean calledFromBeforeAndAfter() throws ClassNotFoundException {
        for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
            if (element.getClassName().startsWith("com.finago")) {
                try {
                    Class<?> clazz = ClassLoader.getSystemClassLoader().loadClass(element.getClassName());
                    Method method = clazz.getDeclaredMethod(element.getMethodName());
                    if (this.hasAnnotations(method, BeforeEach.class, AfterEach.class, BeforeAll.class,
                            AfterAll.class)) {
                        return true;
                    }
                } catch (NoSuchMethodException e) {}
            }
        }
        return false;
    }

    @SafeVarargs
    private boolean hasAnnotations(final Method method, final Class<? extends Annotation>... annotations) {
        for (Class<? extends Annotation> annotation : annotations) {
            if (method.getAnnotation(annotation) != null) {
                return true;
            }
        }
        return false;
    }
}
