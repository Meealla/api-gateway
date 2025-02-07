package webapp.apigateway.infrastructure.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import webapp.apigateway.application.service.ApiGatewayServiceImpl;
import webapp.apigateway.domain.model.Department;
import webapp.apigateway.domain.model.ResumeWithTemplate;

import java.util.List;

/**
 * Контроллер для управления запросами в API Gateway.
 */
@RestController
public class GatewayController {

    private ApiGatewayServiceImpl apiGatewayServiceImpl;

    @Autowired
    public GatewayController(ApiGatewayServiceImpl apiGatewayServiceImpl) {
        this.apiGatewayServiceImpl = apiGatewayServiceImpl;
    }

    /**
     * Обработчик GET-запроса.
     * @return строка "OK", указывающая, что шлюз работает корректно.
     */
    @GetMapping("/status")
    public String status() {
        return "OK";
    }

    /**
     * Обработчик GET-запроса.
     * @return Список всех резюме с соответствующим шаблоном.
     */
    @GetMapping("/resumes-with-templates")
    public ResponseEntity<Mono<List<ResumeWithTemplate>>> getAllResume(
            @RequestParam(value = "department", required = false) Department department) {
        if (department == null) {
            return ResponseEntity.ok(apiGatewayServiceImpl.getListOfResumeWithTemplates());
        } else {
            return ResponseEntity.ok(apiGatewayServiceImpl.getListOfResumeWithTemplates(department));
        }
    }
}
