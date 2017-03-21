package filters;

import io.mangoo.interfaces.MangooFilter;
import io.mangoo.routing.Response;
import io.mangoo.routing.bindings.Request;

/**
 * @author Ilya Ismagilov <ilya@singulator.net>
 */
public class SimpleFilter implements MangooFilter {
    private final CorsFilter corsFilter = new CorsFilter();

    @Override
    public Response execute(Request request, Response response) {
        System.out.println("Simple filter is executed");

        return response;
    }
}
