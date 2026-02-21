package org.sachith.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import org.sachith.dto.ErrorResponse;
import org.sachith.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ExceptionHandlingFilter implements Filter {

        private static final Logger log = LoggerFactory.getLogger(ExceptionHandlingFilter.class);

        private final ObjectMapper mapper = new ObjectMapper();

        @Override
        public void doFilter(
                        ServletRequest request,
                        ServletResponse response,
                        FilterChain chain)
                        throws IOException, ServletException {

                HttpServletResponse httpResponse = (HttpServletResponse) response;

                try {
                        chain.doFilter(request, response);
                } catch (Exception ex) {
                        // Unwrap cause chain to check for BusinessException (including InvalidPaymentException)
                        Throwable cause = ex;
                        while (cause != null) {
                                if (cause instanceof BusinessException bex) {
                                        log.warn("Unwrapped BusinessException: {} | Type: {}", bex.getMessage(), bex.getClass().getName(), bex);
                                        httpResponse.setStatus(400);
                                        httpResponse.setContentType("application/json");
                                        ErrorResponse error = new ErrorResponse(bex.getErrorCode(), bex.getMessage());
                                        mapper.writeValue(httpResponse.getOutputStream(), error);
                                        return;
                                }
                                cause = cause.getCause();
                        }
                        log.error("Unexpected exception occurred | Type: {}", ex.getClass().getName(), ex);
                        httpResponse.setStatus(500);
                        httpResponse.setContentType("application/json");
                        ErrorResponse error = new ErrorResponse("INTERNAL_ERROR", "Something went wrong");
                        mapper.writeValue(httpResponse.getOutputStream(), error);
                }
        }
}
