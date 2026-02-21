package org.sachith.dispatcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Wrapper for HTTP request and response handling.
 * Provides utility methods for reading request body,
 * writing JSON responses, and accessing request parameters.
 */
public class RequestContext {

    private final ObjectMapper mapper;
    private final HttpServletRequest request;
    private final HttpServletResponse response;

    public RequestContext(ObjectMapper mapper, HttpServletRequest request, HttpServletResponse response) {
        this.mapper = mapper;
        this.request = request;
        this.response = response;
    }

    public <T> T readBody(Class<T> clazz) throws IOException {
        return mapper.readValue(request.getInputStream(), clazz);
    }

    public void writeJson(int status, Object body) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        mapper.writeValue(response.getOutputStream(), body);
    }

    public String queryParam(String name) {
        return request.getParameter(name);
    }

    public String method() {
        return request.getMethod();
    }

    public String path() {
        return request.getRequestURI();
    }
}
