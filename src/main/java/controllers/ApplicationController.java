package controllers;

import com.google.inject.Inject;
import io.mangoo.routing.Response;
import io.mangoo.routing.bindings.Request;
import jooq.tables.records.TodoRecord;
import model.Todo;
import org.jooq.DSLContext;

import java.util.List;

import static jooq.tables.Todo.TODO;

public class ApplicationController {
    @Inject private DSLContext create;

    public Response get(String id) {
        System.out.println(id);

        return Response.withOk().andEmptyBody();
    }

    public Response getAll(Request r) {
        List<Todo> result = create.selectFrom(TODO).fetchInto(Todo.class);

        result.stream().forEach(todo -> todo.url = composeUrl(r, todo.id));

        return Response.withOk().andJsonBody(result);
    }

    public Response create(Request r, Todo t) {
        TodoRecord tr = create.insertInto(TODO).columns(TODO.TITLE).values(t.title).returning(TODO.ID).fetchOne();

        t.id = tr.get(TODO.ID);
        t.url = composeUrl(r, t.id);

        return Response.withOk().andJsonBody(t);
    }

    public Response deleteAll() {
        create.deleteFrom(TODO).execute();

        return Response.withOk().andEmptyBody();
    }

    public Response options() {
        return Response.withOk().andEmptyBody();
    }


    private String composeUrl(Request r, long id) {
        return (r.getURL().endsWith("/")) ? r.getURL() + id : r.getURL() + "/" + id;
    }

    //POST: {"title":"a todo"}
    //{"title":"a todo","completed":false,"url":"http://todobackend-spring.herokuapp.com//28","order":null}

    //DELETE: / - deletes all todos
    //

    //GET: / - returns all as an array
    //[{"id":1,"title":"walk the dog","completed":false,"order":0,"url":"https://todo-backend-spring4-java8.herokuapp.com/todos/1"}]

//    Request URL:http://todobackend-mangooio.herokuapp.com/5
//    Request Method:OPTIONS
//    Status Code:404 Not Found

    //GET: http://todobackend-spring.herokuapp.com//4
    //{"title":"my todo","completed":false,"url":"http://todobackend-spring.herokuapp.com//4","order":null}
}
