package me.bayes.vertx.vest.binding;

import me.bayes.vertx.vest.VestApplication;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javax.annotation.PostConstruct;
import javax.ws.rs.Path;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DefaultRouteBindingHolderFactoryTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void onSampleControllerShouldCallPostConstruct() throws Exception {
        // Given
        VestApplication vestApplication = mock(VestApplication.class);
        RouteBindingHolderFactory sut = new DefaultRouteBindingHolderFactory(vestApplication);

        // When
        when(vestApplication.getClasses()).thenReturn(Collections.singleton(TestController.class));
        sut.build();

        // Then
        assertThat(TestController.hasCalled).isTrue();
    }

    @Test
    public void onControllerWithWrongPostConstructMethodShouldThrowException() throws Exception {
        // Given
        VestApplication vestApplication = mock(VestApplication.class);
        RouteBindingHolderFactory sut = new DefaultRouteBindingHolderFactory(vestApplication);

        // Then
        expectedException.expect(IllegalArgumentException.class);

        // When
        when(vestApplication.getClasses()).thenReturn(Collections.singleton(IncorrectTestController.class));
        sut.build();
    }

    @Path("")
    public static class TestController {

        static boolean hasCalled;

        @PostConstruct
        public void run() {
            hasCalled = true;
        }
    }

    @Path("")
    public static class IncorrectTestController {

        @PostConstruct
        public void run(Object someValue) {
        }
    }
}
