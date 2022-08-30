package com.bms.controllers;

import com.bms.services.MovieService;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import jakarta.inject.Inject;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

@ExecuteOn(TaskExecutors.IO)
@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("movie")
public class MovieController {

    @Inject
    MovieService movieService;

    //API to view all the movies playing in city
    @Get("/{cityId}")
    public List<String> getMoviesInACity(String cityId) throws SQLException
    {
        return movieService.getMoviesInACity(cityId);
    }

    //API to check all cinemas in which a movie is playing along with all the showtime
    @Get("/{movieId}/shows")
    public List<HashMap<String, String>> getShowsForAMovie(String movieId) throws SQLException
    {
        return movieService.getShowsForAMovie(movieId);
    }
}
