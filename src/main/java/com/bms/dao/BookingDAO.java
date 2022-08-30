package com.bms.dao;

import com.bms.congif.PropertiesConfig;
import io.micronaut.http.exceptions.HttpStatusException;
import io.micronaut.security.utils.SecurityService;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.UUID;

import static io.micronaut.http.HttpStatus.BAD_REQUEST;

@Singleton
public class BookingDAO {

    @Inject
    PropertiesConfig propertiesConfig;

    @Inject
    SecurityService securityService;

    public Connection getPostgresConnection() throws SQLException
    {
        return DriverManager.getConnection(
                propertiesConfig.getDatabaseURL(), propertiesConfig.getDatabaseUsername(), propertiesConfig.getDatabasePassword());
    }

    public String bookSeat(String showId, String seatId) throws Exception {
        Connection con = getPostgresConnection();
        PreparedStatement fetchSeatStatus = con.prepareStatement("select bookedSeat.id from \"BMS\".\"BookedSeat\" bookedSeat where bookedSeat.\"seatId\"= ?");
        fetchSeatStatus.setObject(1, UUID.fromString(seatId));
        ResultSet fetchSeatStatusRes = fetchSeatStatus.executeQuery();
        if(fetchSeatStatusRes.next())
        {
            throw new HttpStatusException(BAD_REQUEST, "SEAT ALREADY BOOKED");
        }
        else
        {
            PreparedStatement getUserId = con.prepareStatement("select account.\"userId\" from \"BMS\".\"Account\" account where account.username=?");
            getUserId.setObject(1, securityService.username().get());
            ResultSet getUserIdRes = getUserId.executeQuery();
            getUserIdRes.next();
            String userId = getUserIdRes.getString("userId");
            PreparedStatement addBooking = con.prepareStatement("INSERT INTO \"BMS\".\"Booking\"(\"createdAt\", \"updatedAt\", \"isDeleted\", \"userId\", \"showId\", \"numberOfSeatsBooked\", \"showDate\")" + "VALUES (?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            addBooking.setObject(1, Timestamp.from(Instant.now()).toLocalDateTime());
            addBooking.setObject(2, Timestamp.from(Instant.now()).toLocalDateTime());
            addBooking.setObject(3, false);
            addBooking.setObject(4, UUID.fromString(userId));
            addBooking.setObject(5, UUID.fromString(showId));
            addBooking.setObject(6, 1);
            addBooking.setObject(7, new SimpleDateFormat("yyyy-MM-dd").parse("2022-08-04"), Types.DATE);
            int addBookingAffectedRows = addBooking.executeUpdate();
            ResultSet addBookingRes = addBooking.getGeneratedKeys();
            addBookingRes.next();
            String bookingId = addBookingRes.getString(1);

            PreparedStatement addBookedSeat = con.prepareStatement("INSERT INTO \"BMS\".\"BookedSeat\"(\"createdAt\", \"updatedAt\", \"isDeleted\", \"seatId\", \"showId\", \"bookingId\")" + "VALUES (?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            addBookedSeat.setObject(1, Timestamp.from(Instant.now()).toLocalDateTime());
            addBookedSeat.setObject(2, Timestamp.from(Instant.now()).toLocalDateTime());
            addBookedSeat.setObject(3, false);
            addBookedSeat.setObject(4, UUID.fromString(seatId));
            addBookedSeat.setObject(5, UUID.fromString(showId));
            addBookedSeat.setObject(6, UUID.fromString(bookingId));
            int addBookedSeatAffectedRows = addBookedSeat.executeUpdate();
            ResultSet bookedSeatRes = addBookedSeat.getGeneratedKeys();
            bookedSeatRes.next();
            String seatBookingId = addBookingRes.getString(1);
            if(seatBookingId != null)
            {
                return bookingId;
            }
        }
        return null;
    }
}
