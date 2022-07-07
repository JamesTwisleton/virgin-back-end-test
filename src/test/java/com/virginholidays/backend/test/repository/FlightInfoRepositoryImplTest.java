package com.virginholidays.backend.test.repository;

import com.virginholidays.backend.test.api.Flight;
import com.virginholidays.backend.test.configuration.DataSourceConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ResourceLoader;

import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * The FlightInfoRepositoryImpl unit tests. This provides some insight for the type of testing
 * the applicant should be carrying out.
 *
 * @author Geoff Perks
 */
@ExtendWith(MockitoExtension.class)
public class FlightInfoRepositoryImplTest {

    @Mock
    private ResourceLoader resourceLoader;

    @Mock
    private DataSourceConfiguration dataSourceConfiguration;

    @InjectMocks
    private FlightInfoRepositoryImpl repository;

    @BeforeEach
    public void beforeEach() {
        when(dataSourceConfiguration.getCsvLocation()).thenReturn("flights.csv");
    }

    @Test
    public void testFindAll() throws ExecutionException, InterruptedException {

        // prepare
        ClassLoader classLoader = mock(ClassLoader.class);
        URL resource = getClass().getResource("/flights.csv");

        when(resourceLoader.getClassLoader()).thenReturn(classLoader);
        when(classLoader.getResource(anyString())).thenReturn(resource);

        // act
        Optional<List<Flight>> maybeFlights = repository
                .findAll()
                .toCompletableFuture()
                .get();

        // assert
        assertThat(maybeFlights.isPresent(), equalTo(true));
        assertThat(maybeFlights.get().size(), equalTo(27));

        assertThat(maybeFlights.get().get(0).departureTime(), equalTo(LocalTime.of(9, 0)));
        assertThat(maybeFlights.get().get(0).destination(), equalTo("Antigua"));
        assertThat(maybeFlights.get().get(0).iata(), equalTo("ANU"));
        assertThat(maybeFlights.get().get(0).flightNo(), equalTo("VS033"));
        assertThat(maybeFlights.get().get(0).days().size(), equalTo(1));
        assertThat(maybeFlights.get().get(0).days().get(0), equalTo(DayOfWeek.TUESDAY));

        assertThat(maybeFlights.get().get(11).departureTime(), equalTo(LocalTime.of(15, 35)));
        assertThat(maybeFlights.get().get(11).destination(), equalTo("Las Vegas"));
        assertThat(maybeFlights.get().get(11).iata(), equalTo("LAS"));
        assertThat(maybeFlights.get().get(11).flightNo(), equalTo("VS044"));
        assertThat(maybeFlights.get().get(11).days().size(), equalTo(7));
        assertThat(maybeFlights.get().get(11).days().get(0), equalTo(DayOfWeek.SUNDAY));
        assertThat(maybeFlights.get().get(11).days().get(1), equalTo(DayOfWeek.MONDAY));
        assertThat(maybeFlights.get().get(11).days().get(2), equalTo(DayOfWeek.TUESDAY));
        assertThat(maybeFlights.get().get(11).days().get(3), equalTo(DayOfWeek.WEDNESDAY));
        assertThat(maybeFlights.get().get(11).days().get(4), equalTo(DayOfWeek.THURSDAY));
        assertThat(maybeFlights.get().get(11).days().get(5), equalTo(DayOfWeek.FRIDAY));
        assertThat(maybeFlights.get().get(11).days().get(6), equalTo(DayOfWeek.SATURDAY));
    }

    @Test
    public void testFindAllOnDayOfWeek() throws ExecutionException, InterruptedException {

        // prepare
        ClassLoader classLoader = mock(ClassLoader.class);
        URL resource = getClass().getResource("/flights.csv");

        when(resourceLoader.getClassLoader()).thenReturn(classLoader);
        when(classLoader.getResource(anyString())).thenReturn(resource);

        // act
        // This date (7th July 2022) is a Thursday. So what should be returned is all flights that are on Thursdays.
        Optional<List<Flight>> maybeFlights =
                repository.findAllFlightsOnDay(LocalDate.of(2022, 7, 7))
                        .toCompletableFuture()
                        .get();


        // assert
        assertThat(maybeFlights.isPresent(), equalTo(true));
        assertThat(maybeFlights.get().size(), equalTo(7));
        maybeFlights.get()
                .forEach(flight -> assertThat(flight.days().contains(DayOfWeek.THURSDAY), equalTo(true)));

        // verify ordering
        assertThat(maybeFlights.get().get(0).departureTime(), equalTo(LocalTime.of(10, 00)));
        assertThat(maybeFlights.get().get(1).departureTime(), equalTo(LocalTime.of(10, 15)));
        assertThat(maybeFlights.get().get(2).departureTime(), equalTo(LocalTime.of(10, 25)));
        assertThat(maybeFlights.get().get(3).departureTime(), equalTo(LocalTime.of(11, 05)));
        assertThat(maybeFlights.get().get(4).departureTime(), equalTo(LocalTime.of(11, 35)));
        assertThat(maybeFlights.get().get(5).departureTime(), equalTo(LocalTime.of(13, 00)));
        assertThat(maybeFlights.get().get(6).departureTime(), equalTo(LocalTime.of(15, 35)));
    }
}
