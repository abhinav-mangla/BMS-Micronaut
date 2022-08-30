package com.bms.controllers;

import com.bms.services.ShowService;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import jakarta.inject.Inject;

import java.sql.SQLException;

@ExecuteOn(TaskExecutors.IO)
@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("show")
public class ShowController {

    @Inject
    ShowService showService;

    //For each showtime, check the availability of seats
    @Get("/{showId}")
    public int getSeatsForAShow(String showId) throws SQLException
    {
        return showService.getSeatsForAShow(showId);
    }
}