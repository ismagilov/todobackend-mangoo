package controllers;

import filters.SimpleFilter;
import io.mangoo.annotations.FilterWith;
import io.mangoo.routing.Response;

@FilterWith(SimpleFilter.class)
public class ApplicationController {
    public Response index() {
        String hello = "Hello World!";
        return Response.withOk().andContent("hello", hello);
    }

    public Response todo() {
        return Response.withOk().andJsonBody("TODO");
    }

    public Response options() {
        return Response.withOk();
    }

}
