package webapp.apigateway.application.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.analyticsmessage.domain.model.MessageDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * Сервис для отправки данных в Kafka.
 */
@Service
public class KafkaProducerService {
    /**
     * Логгер для записи информации.
     */
    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerService.class);

    /**
     * Название топика Kafka для аналитических данных.
     */
    @Value("${kafka.topic.analytics}")
    private String analyticsTopic;

    /**
     * KafkaTemplate для отправки сообщений в Kafka.
     */
    private final KafkaTemplate<String, String> kafkaTemplate;

    /**
     * ObjectMapper для преобразования объектов в формат JSON.
     */
    private final ObjectMapper objectMapper;

    /**
     * Конструктор для внедрения зависимостей.
     *
     * @param kafkaTemplate для отправки сообщений.
     * @param objectMapper для сериализации данных.
     */
    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    /**
     * Отправляет данные в Kafka.
     *
     * @param data объект типа MessageDTO.
     */
    public void sendToKafka(MessageDTO data) {
        try {
            String jsonData = objectMapper.writeValueAsString(data);
            kafkaTemplate.send(analyticsTopic, jsonData);  // Отправка в Kafka
            logger.info("Data sent to Kafka: {}", jsonData); // Логирование отправленных данных
        } catch (Exception e) {
            logger.error("Error serializing data to JSON or sending to Kafka", e);  // Логирование ошибки
        }
    }
}