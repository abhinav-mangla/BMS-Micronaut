package com.bms.controllers;

import com.bms.services.BookingService;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import jakarta.inject.Inject;

@ExecuteOn(TaskExecutors.IO)
@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("book")
public class BookingController {

    @Inject
    BookingService bookingService;

    //Book a Seat
    @Post("/{showId}/{seatId}")
    public String bookSeat(String showId, String seatId) throws Exception
    {
        return bookingService.bookSeat(showId, seatId);
    }
}
