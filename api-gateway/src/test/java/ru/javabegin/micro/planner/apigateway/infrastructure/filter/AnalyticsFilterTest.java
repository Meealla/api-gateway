package ru.javabegin.micro.planner.apigateway.infrastructure.filter;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import webapp.apigateway.ApiGatewayApplication;
import webapp.apigateway.application.service.KafkaProducerService;
import webapp.apigateway.domain.model.AnalyticsData;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = ApiGatewayApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.yml")
public class AnalyticsFilterTest {
    @Autowired
    private MockMvc mockMvc;

    @Mock
    private KafkaProducerService kafkaProducerService;

    @Test
    public void testAnalyticsFilter() throws Exception {
        doNothing().when(kafkaProducerService).sendToKafka(ArgumentMatchers.any(AnalyticsData.class));
        mockMvc.perform(get("/status"))
                .andExpect(status().isOk());
        verify(kafkaProducerService, times(1)).sendToKafka(any(AnalyticsData.class));
    }
}
