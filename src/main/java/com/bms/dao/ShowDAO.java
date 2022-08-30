package com.bms.dao;

import com.bms.congif.PropertiesConfig;
import jakarta.inject.Inject;

import java.sql.*;
import java.util.UUID;

public class ShowDAO {

    @Inject
    PropertiesConfig propertiesConfig;
    public Connection getPostgresConnection() throws SQLException
    {
        return DriverManager.getConnection(
                propertiesConfig.getDatabaseURL(), propertiesConfig.getDatabaseUsername(), propertiesConfig.getDatabasePassword());
    }

    public int getSeatsForAShow(String showId) throws SQLException {
        Connection con = getPostgresConnection();
        PreparedStatement fetchSeats = con.prepareStatement("select count(seat.id) from \"BMS\".\"Show\" show inner join \"BMS\".\"Screen\" screen on screen.id=show.\"screenId\" inner join \"BMS\".\"Seat\" seat on seat.\"screenId\"=screen.id where show.id = ?");
        fetchSeats.setObject(1, UUID.fromString(showId));
        ResultSet res = fetchSeats.executeQuery();
        res.next();
        int totalSeats = res.getInt(1);

        PreparedStatement fetchBookedSeats = con.prepareStatement("select count(bookedSeat.id) from \"BMS\".\"Show\" show inner join \"BMS\".\"Screen\" screen on screen.id=show.\"screenId\" inner join \"BMS\".\"Seat\" seat on seat.\"screenId\"=screen.id inner join \"BMS\".\"BookedSeat\" bookedSeat on bookedSeat.\"seatId\"=seat.id where show.id = ?");
        fetchBookedSeats.setObject(1, UUID.fromString(showId));
        res = fetchBookedSeats.executeQuery();
        res.next();
        int bookedSeats = res.getInt(1);
        return totalSeats-bookedSeats;
    }
}
