package org.sachith.dispatcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DispatcherServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final ObjectMapper mapper = new ObjectMapper();
    private final Map<RouteKey, Controller> routes = new ConcurrentHashMap<>();

    public DispatcherServlet(Map<RouteKey, Controller> initialRoutes) {
        this.routes.putAll(initialRoutes);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String path = req.getRequestURI();
        String method = req.getMethod().toUpperCase();
        RouteKey key = RouteKey.of(method, path);

        Controller controller = routes.get(key);

        if (controller == null) {
            // If path exists for other method -> 405; else 404
            boolean pathExists = routes.keySet().stream().anyMatch(k -> k.toString().endsWith(" " + path));
            resp.setStatus(pathExists ? 405 : 404);
            resp.setContentType("application/json");
            mapper.writeValue(resp.getOutputStream(),
                    Map.of("errorCode", pathExists ? "METHOD_NOT_ALLOWED" : "NOT_FOUND",
                            "message", pathExists ? "Method not allowed for " + path : "No route for " + method + " " + path));
            return;
        }

        try {
            log.info("Dispatching {} {}", method, path);
            controller.handle(req, resp);
        } catch (Exception ex) {
            // IMPORTANT:
            // Your ExceptionHandlingFilter should catch exceptions thrown downstream of chain.doFilter().
            // But since we're in the servlet, if you prefer filter-only handling, rethrow as RuntimeException.
            // This ensures your global filter handles it consistently.
            throw new RuntimeException(ex);
        }
    }
}
