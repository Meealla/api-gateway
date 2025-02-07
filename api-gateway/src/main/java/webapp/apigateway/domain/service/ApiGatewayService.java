package webapp.apigateway.domain.service;

import java.util.List;
import reactor.core.publisher.Mono;
import webapp.apigateway.domain.model.Department;
import webapp.apigateway.domain.model.ResumeWithTemplate;

/**
 * Интерфейс сервиса для отправки запросов в микросервисы.
 */
public interface ApiGatewayService {

    public <T> Mono<List<T>> makeRequest(String uri, Class<T> elementClass);

    public Mono<List<ResumeWithTemplate>> getListOfResumeWithTemplates(Department department);

    public Mono<List<ResumeWithTemplate>> getListOfResumeWithTemplates();
}

