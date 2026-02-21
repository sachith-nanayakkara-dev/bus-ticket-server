package org.sachith.dispatcher;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface Controller {

    /**
     * Interface for handling HTTP requests in the dispatcher/controller
     * architecture.
     * Implementations should process requests and generate responses.
     */
    void handle(HttpServletRequest request, HttpServletResponse response) throws Exception;
}
