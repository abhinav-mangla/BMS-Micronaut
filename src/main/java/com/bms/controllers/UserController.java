package com.bms.controllers;

import com.bms.models.UserModel;
import com.bms.services.UserService;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import jakarta.inject.Inject;

@ExecuteOn(TaskExecutors.IO)
@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("user")
public class UserController {

    @Inject
    UserService userService;

    //User Sign Up
    @Post(value = "/signup")
    public String signUp(@Body UserModel userModel) throws Exception
    {
        return userService.signUp(userModel.getName(), userModel.getUsername(), userModel.getEmail(), userModel.getPassword(), userModel.getCityId(), userModel.getPhoneNumber());
    }
}
