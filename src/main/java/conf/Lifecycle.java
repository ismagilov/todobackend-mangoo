package conf;

import com.google.inject.Singleton;
import io.mangoo.configuration.Config;
import io.mangoo.core.Application;
import io.mangoo.interfaces.MangooLifecycle;
import org.h2.jdbcx.JdbcDataSource;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultConfiguration;

@Singleton
public class Lifecycle implements MangooLifecycle {

	@Override
    public void applicationStarted() {
	    // Do nothing for now
    }

    @Override
    public void applicationInitialized() {
        Config CONFIG = Application.getConfig();

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

        System.out.println("Initialized: " + create.execute("RUNSCRIPT FROM 'classpath:/db/init.sql'"));
    }

    @Override
    public void applicationStopped() {
        // Do nothing for now
    }
}