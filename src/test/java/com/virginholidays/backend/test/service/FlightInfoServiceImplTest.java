package com.virginholidays.backend.test.service;

import com.virginholidays.backend.test.TestResources;
import com.virginholidays.backend.test.repository.FlightInfoRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import static java.util.concurrent.CompletableFuture.completedFuture;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


/**
 * The FlightInfoServiceImpl unit tests
 *
 * @author Geoff Perks
 */
@ExtendWith(MockitoExtension.class)
public class FlightInfoServiceImplTest extends TestResources {

    @Mock
    private FlightInfoRepositoryImpl repository;

    @InjectMocks
    private FlightInfoServiceImpl flightInfoService;

    @Test
    public void findFlightByDateWhenNoFlightsFound() throws ExecutionException, InterruptedException {
        when(repository.findAllFlightsOnDay(any())).thenReturn(completedFuture(Optional.empty()));
        assertThat(flightInfoService.findFlightByDate(LocalDate.now()).toCompletableFuture().get(),
                equalTo((Optional.empty())));
    }

    @Test
    public void findFlightByDateWhenFlightsFound() throws ExecutionException, InterruptedException {
        when(repository.findAllFlightsOnDay(any())).thenReturn(completedFuture(Optional.of(testFlights)));
        assertThat(flightInfoService.findFlightByDate(LocalDate.now()).toCompletableFuture().get(),
                equalTo(Optional.of(testFlights)));
    }
}