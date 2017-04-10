package conf;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import filters.CorsFilter;
import io.mangoo.configuration.Config;
import io.mangoo.interfaces.MangooLifecycle;
import io.mangoo.interfaces.MangooRequestFilter;
import org.h2.jdbcx.JdbcDataSource;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultConfiguration;

@Singleton
public class Module extends AbstractModule {

	@Override
    protected void configure() {
        bind(MangooLifecycle.class).to(Lifecycle.class);
        bind(MangooRequestFilter.class).to(CorsFilter.class);
    }

    @Provides @Singleton
    DSLContext provideJooqContext(Config config) {
        JdbcDataSource h2ds = new JdbcDataSource();
        h2ds.setURL(config.getString("application.db.url"));
        h2ds.setUser(config.getString("application.db.username"));
        h2ds.setPassword(config.getString("application.db.password"));

        DefaultConfiguration defConfig = new DefaultConfiguration();
        defConfig.setDataSource(h2ds);
        defConfig.setSQLDialect(SQLDialect.H2);

        return DSL.using(defConfig);
    }
}
