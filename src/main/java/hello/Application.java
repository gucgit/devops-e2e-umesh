package hello;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {

        SpringApplicationBuilder app = new SpringApplicationBuilder(Application.class)
                .web(WebApplicationType.SERVLET);
        app.build().run(args);
    }
}
