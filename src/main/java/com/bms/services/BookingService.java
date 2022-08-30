package com.bms.services;

import com.bms.dao.BookingDAO;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class BookingService {

    @Inject
    BookingDAO bookingDAO;

    public String bookSeat(String showId, String seatId) throws Exception {
        return bookingDAO.bookSeat(showId, seatId);
    }
}
