package com.bms.services;

import com.bms.dao.MovieDAO;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

@Singleton
public class MovieService {

    @Inject
    MovieDAO movieDAO;

    public List<String> getMoviesInACity(String id) throws SQLException {
        return movieDAO.getMoviesInACity(id);
    }

    public List<HashMap<String, String>> getShowsForAMovie(String movieId) throws SQLException {
        return movieDAO.getShowsForAMovie(movieId);

    }
}
