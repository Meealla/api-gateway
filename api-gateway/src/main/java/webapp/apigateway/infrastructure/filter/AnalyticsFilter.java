package webapp.apigateway.infrastructure.filter;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;

import org.example.analyticsmessage.domain.model.MessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import webapp.apigateway.application.service.KafkaProducerService;

/**
 * Фильтр для сбора аналитических данных в API Gateway.
 * Реализует интерфейс GlobalFilter для обработки запросов и ответов.
 */
@Component
public class AnalyticsFilter implements GlobalFilter {

    /**
     * Сервис для отправки аналитических данных в Kafka.
     */
    private final KafkaProducerService kafkaProducerService;

    /**
     * Конструктор для внедрения зависимости KafkaProducerService.
     *
     * @param kafkaProducerService сервис для отправки данных в Kafka.
     */
    @Autowired
    public AnalyticsFilter(KafkaProducerService kafkaProducerService) {
        this.kafkaProducerService = kafkaProducerService;
    }

  /**
   * Метод фильтрации запросов и ответов.
   *
   * @param exchange текущий веб-обмен.
   * @param chain цепочка фильтров для продолжения обработки.
   * @return сигнал завершения обработки.
   */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        LocalDateTime start = LocalDateTime.now();
        return chain.filter(exchange)
            .doOnTerminate(() -> {
                LocalDateTime end = LocalDateTime.now();
                int executionTimeMs = (int) Duration.between(start, end).toMillis();
                String method = exchange.getRequest().getMethod().name();
                String url = exchange.getRequest().getURI().getPath();
                String clientIp = exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();
                int responseCode = exchange.getResponse().getStatusCode().value();
                MessageDTO messageDTO = new MessageDTO(
                        start.toString(),
                        method,
                        url,
                        responseCode,
                        clientIp,
                        executionTimeMs
                );
                kafkaProducerService.sendToKafka(messageDTO);
            });
    }
}