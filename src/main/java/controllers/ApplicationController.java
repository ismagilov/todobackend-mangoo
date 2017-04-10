package controllers;

import com.google.inject.Inject;
import io.mangoo.routing.Response;
import io.mangoo.routing.bindings.Session;
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

    public Response todo(Session session) {
        session.put("1", "2");

        return Response.withOk().andJsonBody("TODO");
    }

    public Response options() {
        return Response.withOk().andEmptyBody();
    }

    //POST: {"title":"a todo"}
    //{"title":"a todo","completed":false,"url":"http://todobackend-spring.herokuapp.com//28","order":null}

    //DELETE: / - deletes all todos
    //

    //GET: / - returns all as an array
    //[{"id":1,"title":"walk the dog","completed":false,"order":0,"url":"https://todo-backend-spring4-java8.herokuapp.com/todos/1"}]
}
