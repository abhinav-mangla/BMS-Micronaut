package com.bms.exceptionHandlers;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Error;
import io.micronaut.http.hateoas.JsonError;
import io.micronaut.http.hateoas.Link;

import java.sql.SQLException;

@Controller
public class GlobalExceptionHandler {
    @Error(global = true, exception = ArithmeticException.class)
    public HttpResponse<JsonError> ArithmeticException(HttpRequest<?> request, Throwable e)
    {
        JsonError error = new JsonError("Bad Things Happened: " + e.getMessage()) //
                .link(Link.SELF, Link.of(request.getUri()));

        return HttpResponse.<JsonError>serverError()
                .body(error);
    }

    @Error(global = true, exception = SQLException.class)
    public HttpResponse<JsonError> SQLException(HttpRequest<?> request, Throwable e)
    {
        JsonError error = new JsonError("Bad Things Happened: " + e.getMessage()) //
                .link(Link.SELF, Link.of(request.getUri()));

        return HttpResponse.<JsonError>serverError()
                .body(error);
    }
}
