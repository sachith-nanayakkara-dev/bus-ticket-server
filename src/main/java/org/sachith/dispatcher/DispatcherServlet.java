package org.sachith.dispatcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.sachith.controller.AvailabilityController;
import org.sachith.controller.ReservationController;
import org.sachith.controller.SeatGridMapController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DispatcherServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final ObjectMapper mapper = new ObjectMapper();
    private final Map<RouteKey, Controller> routes = new ConcurrentHashMap<>();

    public DispatcherServlet() {
    }

    @Override
    protected void service(HttpServletRequest req,
                           HttpServletResponse resp)
            throws IOException {

        String rawPath = req.getRequestURI();

        String path =
                rawPath.substring(req.getContextPath().length());

        if (path == null || path.isEmpty()) {
            path = "/";
        }

        if (path.length() > 1 && path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }

        final String normalizedPath = path;

        String method =
                req.getMethod().toUpperCase();

        RouteKey key =
                RouteKey.of(method, normalizedPath);

        Controller controller =
                routes.get(key);

        if (controller == null) {

            boolean pathExists =
                    routes.keySet()
                            .stream()
                            .anyMatch(k ->
                                    k.getPath()
                                            .equals(normalizedPath)
                            );

            resp.setStatus(pathExists ? 405 : 404);

            resp.setContentType("application/json");

            mapper.writeValue(
                    resp.getOutputStream(),
                    Map.of(
                            "errorCode",
                            pathExists ?
                                    "METHOD_NOT_ALLOWED"
                                    : "NOT_FOUND",
                            "message",
                            pathExists ?
                                    "Method not allowed for " + normalizedPath
                                    : "No route for " + method + " " + normalizedPath
                    )
            );

            return;
        }

        try {
            controller.handle(req, resp);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void init() {
        routes.put(RouteKey.of("POST", "/availability"), new AvailabilityController());
        routes.put(RouteKey.of("POST", "/reserve"), new ReservationController());
        routes.put(RouteKey.of("GET", "/seat-map/grid"), new SeatGridMapController());
    }
}
