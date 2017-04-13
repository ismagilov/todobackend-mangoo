package controllers;

import com.google.inject.Inject;
import io.mangoo.routing.Response;
import io.mangoo.routing.bindings.Request;
import jooq.tables.records.TodoRecord;
import model.Todo;
import model.TodoPatch;
import org.jooq.DSLContext;
import org.jooq.Field;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static jooq.tables.Todo.TODO;

public class ApplicationController {
    @Inject private DSLContext create;

    public Response get(Request r, long id) {
        Todo t = create.selectFrom(TODO).where(TODO.ID.eq(id)).fetchAnyInto(Todo.class);

        if (null != t) {
            t.url = r.getURL();
            return Response.withOk().andJsonBody(t);
        } else return Response.withOk().andEmptyBody();

    }

    public Response getAll(Request r) {
        List<Todo> result = create.selectFrom(TODO).fetchInto(Todo.class);

        result.stream().forEach(todo -> todo.url = appendIdToUrl(r, todo.id));

        return Response.withOk().andJsonBody(result);
    }

    public Response create(Request r, Todo t) {
        TodoRecord tr = create.insertInto(TODO).columns(TODO.TITLE).values(t.title).returning(TODO.ID).fetchOne();

        t.id = tr.get(TODO.ID);
        t.url = appendIdToUrl(r, t.id);

        return Response.withOk().andJsonBody(t);
    }

    public Response delete(long id) {
        create.deleteFrom(TODO).where(TODO.ID.eq(id)).execute();

        return Response.withOk().andEmptyBody();
    }

    public Response modify(Request r, long id, TodoPatch tp) {
        Map<Field<?>, Object> values = new HashMap<>();

        if (null != tp.completed)
            values.put(TODO.COMPLETED, tp.completed);
        if (null != tp.title)
            values.put(TODO.TITLE, tp.title);
        if (null != tp.order)
            values.put(TODO.ORDER, tp.order);

        int result = create.update(TODO).set(values).where(TODO.ID.eq(id)).execute();

        if (1 == result) {
            Todo t = create.selectFrom(TODO).where(TODO.ID.eq(id)).fetchAnyInto(Todo.class);
            t.url = r.getURL();
            return Response.withOk().andJsonBody(t);
        } else
            return Response.withOk().andEmptyBody();
    }

    public Response deleteAll() {
        create.deleteFrom(TODO).execute();

        return Response.withOk().andEmptyBody();
    }

    public Response options() {
        return Response.withOk().andEmptyBody();
    }


    private String appendIdToUrl(Request r, long id) {
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

//    Request URL:http://todobackend-spring.herokuapp.com//7
//    Request Method:PATCH
//
//    {completed: true}
//    {title: "bathe the cat"}
//    {title: "changed title", completed: true}
//    {order: 95}
}
