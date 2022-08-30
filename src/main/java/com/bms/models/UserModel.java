package com.bms.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserModel {

    private String name;

    private String username;

    private String email;

    private String password;

    private String cityId;

    private Long phoneNumber;
}
