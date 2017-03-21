package filters;

import io.mangoo.interfaces.MangooRequestFilter;
import io.mangoo.routing.Response;
import io.mangoo.routing.bindings.Request;
import io.undertow.util.HttpString;
import io.undertow.util.Methods;

/**
 * @author Ilya Ismagilov <ilya@singulator.net>
 */
public class CorsFilter implements MangooRequestFilter {
    public Response execute(Request request, Response response) {

        System.out.println("Global filter is executed");

        response.andHeader(HttpString.tryFromString("Access-Control-Allow-Origin"), "*");
        response.andHeader(HttpString.tryFromString("Access-Control-Allow-Methods"), "GET, POST, OPTIONS");

        if (Methods.OPTIONS.equals(request.getMethod())) {
            response.andHeader(HttpString.tryFromString("Access-Control-Request-Headers"),
                    "Keep-Alive ,User-Agent ,X-Requested-With, If-Modified-Since, Cache-Control, Content-Type, Content-Range, Range");
            response.andHeader(HttpString.tryFromString("Access-Control-Max-Age"), Integer.toString(1728000));
        }

        return response;
    }
}
