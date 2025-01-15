package webapp.apigateway.infrastructure.filter;

import java.time.Duration;
import java.time.Instant;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import webapp.apigateway.application.service.KafkaProducerService;
import webapp.apigateway.domain.model.AnalyticsData;

/**
 * Фильтр для сбора аналитических данных в API Gateway.
 * Реализует интерфейс GatewayFilter для обработки запросов и ответов.
 */
@Component
public class AnalyticsFilter implements GatewayFilter {

    /**
     * Сервис для отправки аналитических данных в Kafka.
     */
    private final KafkaProducerService kafkaProducerService;

    /**
     * Конструктор для внедрения зависимости KafkaProducerService.
     *
     * @param kafkaProducerService сервис для отправки данных в Kafka.
     */
    public AnalyticsFilter(KafkaProducerService kafkaProducerService) {
        this.kafkaProducerService = kafkaProducerService;
    }

  /**
   * Метод фильтрации запросов и ответов.
   *
   * @param exchange текущий веб-обмен.
   * @param chain цупочка фильтров для продолжения обработки.
   * @return сигнал завершения обработки.
   */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        Instant start = Instant.now();
        return chain.filter(exchange)
            .doOnTerminate(() -> {
                Instant end = Instant.now();
                long executionTimeMs = Duration.between(start, end).toMillis();
                String method = exchange.getRequest().getMethod().name();
                String url = exchange.getRequest().getURI().getPath();
                String clientIp = exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();
                int responseCode = exchange.getResponse().getStatusCode().value();
                AnalyticsData analyticsData = new AnalyticsData(
                        Instant.now().toString(),
                        method,
                        url,
                        responseCode,
                        clientIp,
                        executionTimeMs
                );
                kafkaProducerService.sendToKafka(analyticsData);
            });
    }
}