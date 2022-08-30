package com.bms.congif;

import jakarta.inject.Singleton;

@Singleton
public class PropertiesConfig {

    public String getDatabaseURL()
    {
        return "jdbc:postgresql://localhost:5432/BMS_db?currentSchema=\"BMS\"";
    }

    public String getDatabaseUsername()
    {
        return "abhinav";
    }

    public String getDatabasePassword()
    {
        return "root";
    }
}
