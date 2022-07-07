package com.virginholidays.backend.test.resource;

import com.virginholidays.backend.test.TestResources;
import com.virginholidays.backend.test.api.Flight;
import com.virginholidays.backend.test.service.FlightInfoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import static java.util.concurrent.CompletableFuture.completedFuture;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * The FlightInfoResource unit tests
 *
 * @author Geoff Perks
 */
@ExtendWith(MockitoExtension.class)
public class FlightInfoResourceTest extends TestResources {

    @Mock
    private FlightInfoService flightInfoService;

    @InjectMocks
    private FlightInfoResource flightInfoResource;

    @Test
    public void getResultsWhenNoFlightsOnGivenDate() throws ExecutionException, InterruptedException {
        when(flightInfoService.findFlightByDate(any())).thenReturn(completedFuture(Optional.empty()));
        assertThat(
                flightInfoResource.getResults("2022-07-09").toCompletableFuture().get().getStatusCode(),
                equalTo(HttpStatus.NO_CONTENT));
    }

    @Test
    public void getResultsWhenFlightsOnGivenDate() throws ExecutionException, InterruptedException {
        when(flightInfoService.findFlightByDate(any()))
                .thenReturn(completedFuture(Optional.of(testFlights)));

        var response = flightInfoResource.getResults("2022-07-08").toCompletableFuture().get();
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(response.hasBody(), equalTo(true));
        List<Flight> body = (List<Flight>) response.getBody();
        assertThat(body, equalTo(testFlights));
    }
}

