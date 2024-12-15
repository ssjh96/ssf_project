package sg.edu.nus.iss.ssf_project_potluck_shamus.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@Configuration
public class LoggingConfig {

    @Bean
    public CommonsRequestLoggingFilter requestLoggingFilter(){
        CommonsRequestLoggingFilter crlf = new CommonsRequestLoggingFilter();

        // Includes information about the client making the request (e.g., IP address)
        crlf.setIncludeClientInfo(true);
        // Client Info: IP=127.0.0.1, Session ID=ABC123

        // Logs the query string from the request URL (e.g., ?key=value)
        crlf.setIncludeQueryString(true);
        // Query String: key=value

        // Whether to log HTTP headers (e.g., User-Agent, Authorization)
        crlf.setIncludeHeaders(false); // Change to true to enable
        // Headers: {User-Agent=PostmanRuntime/7.29.0, Authorization=Bearer token123}

        // Logs the body/payload of the request (e.g., JSON or form data)
        // Useful for debugging POST requests with JSON or form data.
        crlf.setIncludePayload(false); // Change to true to enable
        // Payload: {"name": "John Doe", "email": "john.doe@example.com"}

        return crlf;
    }
    
}
