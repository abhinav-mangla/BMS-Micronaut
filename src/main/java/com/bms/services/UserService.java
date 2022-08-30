package com.bms.services;

import com.bms.dao.UserDAO;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class UserService {

    @Inject
    UserDAO userDAO;
    public String signUp(String name, String username, String email, String password, String cityId, Long phoneNumber) throws Exception{
        return userDAO.signUp(name, username, email, password, cityId, phoneNumber);
    }

}
