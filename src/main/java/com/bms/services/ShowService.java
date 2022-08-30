package com.bms.services;

import com.bms.dao.ShowDAO;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.sql.SQLException;

@Singleton
public class ShowService {

    @Inject
    ShowDAO showDAO;

    public int getSeatsForAShow(String showId) throws SQLException {
        return showDAO.getSeatsForAShow(showId);
    }
}
