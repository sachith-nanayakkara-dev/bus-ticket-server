package org.sachith.dispatcher;

import java.util.Objects;

/**
 * Composite key for routing based on HTTP method and path.
 * Used to uniquely identify routes in the dispatcher.
 */
public class RouteKey {

    private final String method;
    private final String path;

    public RouteKey(String method, String path) {
        this.method = method;
        this.path = path;
    }

    public static RouteKey of(String method, String path) {
        return new RouteKey(method.toUpperCase(), path);
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof RouteKey))
            return false;
        RouteKey routeKey = (RouteKey) o;
        return Objects.equals(method, routeKey.method) && Objects.equals(path, routeKey.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(method, path);
    }

    @Override
    public String toString() {
        return method + " " + path;
    }
}
