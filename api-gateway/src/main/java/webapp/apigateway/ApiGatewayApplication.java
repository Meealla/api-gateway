package webapp.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class,
        RedisAutoConfiguration.class,
        MongoAutoConfiguration.class})
@ComponentScan(basePackageClasses = {webapp.resumeanalyzer.domain.model.Resume.class,
        webapp.resumegenerator.domain.model.Template.class})
@ComponentScan(basePackages = "webapp.apigateway")
public class ApiGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }
}