package conf;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.mangoo.interfaces.MangooLifecycle;
import org.jooq.DSLContext;

@Singleton
public class Lifecycle implements MangooLifecycle {
    @Inject
    private DSLContext create;

	@Override
    public void applicationStarted() {
	    // Do nothing for now
    }

    @Override
    public void applicationInitialized() {
        create.execute("RUNSCRIPT FROM 'classpath:/db/init.sql'");
    }

    @Override
    public void applicationStopped() {
        // Do nothing for now
    }
}