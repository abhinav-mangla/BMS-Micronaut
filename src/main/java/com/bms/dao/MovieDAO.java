package com.bms.dao;

import com.bms.congif.PropertiesConfig;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Singleton
public class MovieDAO {

    @Inject
    PropertiesConfig propertiesConfig;
    public Connection getPostgresConnection() throws SQLException
    {
        return DriverManager.getConnection(
                propertiesConfig.getDatabaseURL(), propertiesConfig.getDatabaseUsername(), propertiesConfig.getDatabasePassword());
    }

    public List<String> getMoviesInACity(String id) throws SQLException {
        Connection con = getPostgresConnection();
        PreparedStatement stmt = con.prepareStatement("select movie.name from \"BMS\".\"Movie\" movie inner join \"BMS\".\"Show\" show on movie.id=show.\"movieId\" inner join \"BMS\".\"Screen\" screen on show.\"screenId\"=screen.id inner join \"BMS\".\"Theatre\" theatre on screen.\"theatreId\" = theatre.id where theatre.\"cityId\" = ?");
        stmt.setObject(1, UUID.fromString(id));

        ResultSet res = stmt.executeQuery();
        List<String> movieList = new ArrayList<String>();
        while(res.next())
        {
            movieList.add(res.getString("name"));
        }
        return movieList;
    }

    public List<HashMap<String, String>> getShowsForAMovie(String id) throws SQLException {
        Connection con = getPostgresConnection();
        PreparedStatement stmt = con.prepareStatement("select theatre.name, show.\"startTimeInUTC\" from \"BMS\".\"Movie\" movie inner join \"BMS\".\"Show\" show on movie.id=show.\"movieId\" inner join \"BMS\".\"Screen\" screen on show.\"screenId\"=screen.id inner join \"BMS\".\"Theatre\" theatre on screen.\"theatreId\" = theatre.id where movie.id = ?");
        stmt.setObject(1, UUID.fromString(id));

        ResultSet res = stmt.executeQuery();
        List<HashMap<String, String>> movieList = new ArrayList<HashMap<String, String>>();
        while(res.next())
        {
            HashMap<String, String> movieMap = new HashMap<>();
            movieMap.put("Theatre", res.getString("name"));
            movieMap.put("Show", res.getString("startTimeInUTC"));
            movieList.add(movieMap);
        }
        return movieList;
    }
}
