package controllers;

import com.google.inject.Inject;
import io.mangoo.routing.Response;
import io.mangoo.routing.bindings.Request;
import io.undertow.util.HttpString;
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
        String proto = r.getHeader(HttpString.tryFromString("X-Forwarded-Proto"));
        String url = r.getURL();
        if (null != proto)
            url = url.replaceFirst("^https?://", proto + "://");

        return (url.endsWith("/")) ? url + id : url + "/" + id;
    }

}
