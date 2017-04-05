package controllers;

import io.mangoo.configuration.Config;
import io.mangoo.core.Application;
import io.mangoo.routing.Response;
import jooq.tables.records.TodoRecord;
import org.h2.jdbcx.JdbcDataSource;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultConfiguration;

import java.util.Iterator;

import static jooq.tables.Todo.TODO;

public class ApplicationController {
    private static final Config CONFIG = Application.getConfig();

    public Response index() {

        JdbcDataSource h2ds = new JdbcDataSource();
        h2ds.setURL(CONFIG.getString("application.db.url"));
        h2ds.setUser(CONFIG.getString("application.db.username"));
        h2ds.setPassword(CONFIG.getString("application.db.password"));

        //TODO Move to Guice module, make injectible
        DefaultConfiguration config = new DefaultConfiguration();
        config.setDataSource(h2ds);
        config.setSQLDialect(SQLDialect.H2);

        //TODO Move to Guice module, make injectible
        DSLContext create = DSL.using(config);

        System.out.println("Executed = " + create.insertInto(TODO).columns(TODO.TITLE).values("NewTitle").execute());

        Result<TodoRecord> result = create.selectFrom(TODO).fetch();
        Iterator<TodoRecord> it = result.iterator();
        while(it.hasNext()) {
            TodoRecord r = it.next();

            System.out.println(r.get(TODO.TITLE));
        }


        String hello = "Hello World!";

        return Response.withOk().andContent("hello", hello);
    }

    public Response todo() {


        return Response.withOk().andJsonBody("TODO");
    }

    public Response options() {
        return Response.withOk().andEmptyBody();
    }
}
