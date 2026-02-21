package org.sachith.dispatcher;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface Controller {

    void handle(HttpServletRequest request, HttpServletResponse response) throws Exception;
}
