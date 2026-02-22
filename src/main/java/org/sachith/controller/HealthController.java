package org.sachith.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.sachith.dispatcher.Controller;
import org.sachith.dispatcher.RequestContext;

import java.io.IOException;
import java.util.Map;

/**
 * Health check controller to verify server is running.
 */
public class HealthController implements Controller {

        private static final ObjectMapper mapper = new ObjectMapper();

        @Override
        public void handle(
                        HttpServletRequest request,
                        HttpServletResponse response) throws IOException {
                RequestContext ctx = new RequestContext(mapper, request, response);
                ctx.writeJson(HttpServletResponse.SC_OK, Map.of("status", "UP"));
        }
}