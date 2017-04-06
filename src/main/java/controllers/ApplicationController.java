package controllers;

import com.google.inject.Inject;
import io.mangoo.routing.Response;
import model.Todo;
import org.jooq.DSLContext;

import java.util.List;

import static jooq.tables.Todo.TODO;

public class ApplicationController {
    @Inject private DSLContext create;

    public Response index() {
        create.insertInto(TODO).columns(TODO.TITLE).values("NewTitle").execute();

        List<Todo> result = create.selectFrom(TODO).fetchInto(Todo.class);

        return Response.withOk().andJsonBody(result);
    }

    public Response todo() {
        return Response.withOk().andJsonBody("TODO");
    }

    public Response options() {
        return Response.withOk().andEmptyBody();
    }
}
