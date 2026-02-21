package org.sachith.server;

import jakarta.servlet.http.HttpServlet;
import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import org.apache.tomcat.util.descriptor.web.FilterMap;
import org.sachith.dispatcher.DispatcherServlet;
import org.sachith.dispatcher.RouteRegistry;
import org.sachith.filter.ExceptionHandlingFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;


public class TomcatServer {

    private static final Logger log =
            LoggerFactory.getLogger(TomcatServer.class);

    public static void main(String[] args) throws Exception {



        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);
        tomcat.getConnector();  // IMPORTANT

        String webAppDir = new File("src/main/webapp").getAbsolutePath();
        Context context = tomcat.addContext("",webAppDir);

        // Register ONLY ONE servlet: DispatcherServlet (Front Controller)
        registerServlet(context, "dispatcher", "/*",
                new DispatcherServlet(RouteRegistry.routes()));
        registerExceptionFilter(context);

        tomcat.start();
        log.info("Server started on port 8080");
        tomcat.getServer().await();

    }

    private static void registerServlet(Context context, String name, String path, HttpServlet servlet) {
        Tomcat.addServlet(context, name, servlet);
        context.addServletMappingDecoded(path, name);
        log.info("Registered servlet: {} -> {}", name, path);
    }

    private static void registerExceptionFilter(Context context) {
        FilterDef filterDef = new FilterDef();
        filterDef.setFilterName("exceptionFilter");
        filterDef.setFilterClass(ExceptionHandlingFilter.class.getName());
        context.addFilterDef(filterDef);

        FilterMap filterMap = new FilterMap();
        filterMap.setFilterName("exceptionFilter");
        filterMap.addURLPattern("/*");
        context.addFilterMap(filterMap);

        log.info("Registered filter: exceptionFilter -> /*");
    }


}
