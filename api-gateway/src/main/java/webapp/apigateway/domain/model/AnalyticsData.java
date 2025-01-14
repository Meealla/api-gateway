package webapp.apigateway.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Класс, представляющий аналитические данные для сбора информации о запросах и ответах.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsData {

    /**
     * Временная метка события.
     */
    private String timestamp;

    /**
     * HTTP method.
     */
    private String method;

    /**
     * URL-запроса.
     */
    private String url;

    /**
     * Код HTTP-ответа.
     */
    private int responseCode;

    /**
     * IP-адрес клиента.
     */
    private String clientIp;

    /**
     * Время, затраченное на обработку запроса.
     */
    private long executionTimeMs;
}
