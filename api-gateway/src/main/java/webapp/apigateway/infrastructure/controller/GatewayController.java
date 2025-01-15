package webapp.apigateway.infrastructure.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер для управления запросами в API Gateway.
 */
@RestController
public class GatewayController {
    /**
     * Обработчик GET-запроса.
     * @return строка "OK", указывающая, что шлюз работает корректно.
     */
    @GetMapping("/status")
    public String status() {
        return "OK";
    }
}
