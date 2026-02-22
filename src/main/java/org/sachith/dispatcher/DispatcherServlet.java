package org.sachith.dispatcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.sachith.controller.AvailabilityController;
import org.sachith.controller.HealthController;
import org.sachith.controller.ReservationController;
import org.sachith.controller.SeatGridMapController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Handles routing of HTTP requests to the appropriate controller.
 */
public class DispatcherServlet extends HttpServlet {

        private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

        private final ObjectMapper mapper = new ObjectMapper();
        private final Map<RouteKey, Controller> routes = new ConcurrentHashMap<>();

        public DispatcherServlet() {
        }

        /**
         * Handles all HTTP requests, normalizes the path, matches the route, and
         * delegates to the controller.
         * Returns 404 if no route, 405 if method not allowed.
         */
        @Override
        protected void service(HttpServletRequest req,
                        HttpServletResponse resp)
                        throws IOException {

                String rawPath = req.getRequestURI();
                String path = rawPath.substring(req.getContextPath().length());

                if (path == null || path.isEmpty()) {
                        path = "/";
                }

                if (path.length() > 1 && path.endsWith("/")) {
                        path = path.substring(0, path.length() - 1);
                }

                final String normalizedPath = path;
                String method = req.getMethod().toUpperCase();

                log.info("Received {} request for path: {}", method, normalizedPath);

                RouteKey key = RouteKey.of(method, normalizedPath);
                Controller controller = routes.get(key);

                if (controller == null) {
                        boolean pathExists = routes.keySet()
                                        .stream()
                                        .anyMatch(k -> k.getPath()
                                                        .equals(normalizedPath));

                        log.warn("No controller found for {} {}. Path exists: {}", method, normalizedPath, pathExists);

                        resp.setStatus(pathExists ? 405 : 404);
                        resp.setContentType("application/json");
                        mapper.writeValue(
                                        resp.getOutputStream(),
                                        Map.of(
                                                        "errorCode",
                                                        pathExists ? "METHOD_NOT_ALLOWED"
                                                                        : "NOT_FOUND",
                                                        "message",
                                                        pathExists ? "Method not allowed for " + normalizedPath
                                                                        : "No route for " + method + " "
                                                                                        + normalizedPath));
                        return;
                }

                log.info("Dispatching to controller: {} for {} {}", controller.getClass().getSimpleName(), method,
                                normalizedPath);
                try {
                        controller.handle(req, resp);
                } catch (Exception ex) {
                        log.error("Exception while handling {} {}: {}", method, normalizedPath, ex.toString(), ex);
                        throw new RuntimeException(ex);
                }
        }

        /**
         * Registers all routes and their corresponding controllers.
         */
        @Override
        public void init() {
                routes.putAll(RouteRegistry.routes());
        }
}
