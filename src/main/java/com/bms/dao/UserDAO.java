package com.bms.dao;

import com.bms.congif.PropertiesConfig;
import com.bms.utils.AuthenticationProviderUserPassword;
import io.micronaut.http.exceptions.HttpStatusException;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.sql.*;
import java.time.Instant;
import java.util.Base64;
import java.util.UUID;

import static io.micronaut.http.HttpStatus.BAD_REQUEST;

@Singleton
public class UserDAO {

    @Inject
    PropertiesConfig propertiesConfig;

    @Inject
    AuthenticationProviderUserPassword authenticationProviderUserPassword;


    public Connection getPostgresConnection() throws SQLException
    {
        return DriverManager.getConnection(
                propertiesConfig.getDatabaseURL(), propertiesConfig.getDatabaseUsername(), propertiesConfig.getDatabasePassword());
    }

    public String signUp(String name, String username, String email, String password, String cityId, Long phoneNumber) throws Exception{
        Connection con = getPostgresConnection();
        PreparedStatement fetchUserStatus = con.prepareStatement("select users.id from \"BMS\".\"User\" users inner join \"BMS\".\"Account\" account on account.\"userId\"=users.id where users.email=? OR users.\"phoneNumber\"=? OR account.username=?");
        fetchUserStatus.setString(1, email);
        fetchUserStatus.setObject(2, String.valueOf(phoneNumber));
        fetchUserStatus.setString(3, username);
        ResultSet fetchUserStatusRes = fetchUserStatus.executeQuery();
        if(fetchUserStatusRes.next())
        {
            throw new HttpStatusException(BAD_REQUEST, "CREDENTIALS ALREADY IN USE");
        }
        else{
            PreparedStatement addUser = con.prepareStatement("INSERT INTO \"BMS\".\"User\" (\"createdAt\", \"updatedAt\", \"isDeleted\", name, email, \"cityId\", \"phoneNumber\", \"recentLoggedInAtUTC\")" + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            addUser.setObject(1, Timestamp.from(Instant.now()).toLocalDateTime());
            addUser.setObject(2, Timestamp.from(Instant.now()).toLocalDateTime());
            addUser.setObject(3, false);
            addUser.setString(4, name);
            addUser.setString(5, email);
            addUser.setObject(6, UUID.fromString(cityId));
            addUser.setString(7, String.valueOf(phoneNumber));
            addUser.setObject(8, Timestamp.from(Instant.now()).toLocalDateTime());
            int addUserAffectedRows = addUser.executeUpdate();
            ResultSet addUserRes = addUser.getGeneratedKeys();
            addUserRes.next();
            String userId = addUserRes.getString(1);

            PreparedStatement addAccount = con.prepareStatement("INSERT INTO \"BMS\".\"Account\"(\"createdAt\", \"updatedAt\", \"isDeleted\", \"userId\", username, \"passwordHash\")VALUES (?, ?, ?, ?, ?, ?);", Statement.RETURN_GENERATED_KEYS);
            addAccount.setObject(1, Timestamp.from(Instant.now()).toLocalDateTime());
            addAccount.setObject(2, Timestamp.from(Instant.now()).toLocalDateTime());
            addAccount.setObject(3, false);
            addAccount.setObject(4, UUID.fromString(userId));
            addAccount.setString(5, username);
            addAccount.setString(6, encodePassword(password));
            int addAccountAffectedRows = addAccount.executeUpdate();
            ResultSet accountRes = addAccount.getGeneratedKeys();
            accountRes.next();
            String accountId = accountRes.getString(1);
            return accountId;
        }
    }

    public String encodePassword(String password)
    {
        return Base64.getEncoder().encodeToString(password.getBytes());
    }
}
