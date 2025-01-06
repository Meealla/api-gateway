package webapp.apigateway.infrastructure.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GatewayController {
    @GetMapping("/status")
    public String status() {
        return "OK";
    }
}
