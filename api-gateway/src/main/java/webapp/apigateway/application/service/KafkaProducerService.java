package webapp.apigateway.application.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import webapp.apigateway.domain.model.AnalyticsData;

@Service
public class KafkaProducerService {
    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerService.class);

    @Value("${kafka.topic.analytics}")
    private String analyticsTopic;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendToKafka(AnalyticsData data) {
        try {
            String jsonData = objectMapper.writeValueAsString(data);
            kafkaTemplate.send(analyticsTopic, jsonData);  // Отправка в Kafka
            logger.info("Data sent to Kafka: {}", jsonData); // Логирование отправленных данных
        } catch (Exception e) {
            logger.error("Error serializing data to JSON or sending to Kafka", e);  // Логирование ошибки
        }
    }
}