# Bus Ticket Server

A simple Java web application for bus seat reservation and availability management.

## Features
- Seat reservation with input validation
- Seat availability queries

## Build & Deployment

1. **Build the WAR file**

   Run the following command in the project root:

   ```
   ./gradlew clean build
   ```

   The WAR file will be generated at:
   ```
   build/libs/bus-ticket-server.war
   ```

2. **Deploy to Tomcat**

   - Copy the generated `bus-ticket-server.war` file to the `webapps` directory of your external Tomcat server.
   - Start or restart Tomcat.
   - The application will be available at:  
     `http://<your-server>:<tomcat-port>/bus-ticket-server/`

## Configuration

- Servlet configuration is in `src/main/webapp/WEB-INF/web.xml`

## Requirements

- Java 17+
- Apache Tomcat 10+ (Jakarta EE 10/Servlet 6.0 compatible)