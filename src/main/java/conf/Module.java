package conf;

import filters.CorsFilter;
import io.mangoo.interfaces.MangooLifecycle;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import io.mangoo.interfaces.MangooRequestFilter;

@Singleton
public class Module extends AbstractModule {
	@Override
    protected void configure() {
        bind(MangooLifecycle.class).to(Lifecycle.class);
        bind(MangooRequestFilter.class).to(CorsFilter.class);
    }
}
