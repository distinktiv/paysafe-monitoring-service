package com.paysafe.monitoring.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paysafe.monitoring.model.MonitoringParams;
import com.paysafe.monitoring.service.MonitoringService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(MonitoringController.class)
public class MonitoringControllerTest {
    private static final String HOST = "https://api.test.paysafe.com";
    private static final String MONITORING_START_ENDPOINT = "/monitoring/start";
    private static final String MONITORING_STOP_ENDPOINT = "/monitoring/stop";
    private static final String MONITORING_STATUS_ENDPOINT = "/monitoring/status";
    private MonitoringParams validPayload;
    private MonitoringParams invalidPayload;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MonitoringController monitoringController;

    @Mock
    private MonitoringService monitoringService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void setup() {
        validPayload = MonitoringParams.builder()
                .interval(15)
                .url(HOST)
                .build();

        invalidPayload = MonitoringParams.builder()
                .interval(0)
                .url("")
                .build();
    }

    @Test
    public void testStartMonitoringWithValidPayload() throws Exception {
        mockMvc.perform(post(MONITORING_START_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(validPayload)))
                .andExpect(status().isOk());
    }

    @Test
    public void testStartMonitoringWithInvalidPayload() throws Exception {
        mockMvc.perform(post(MONITORING_START_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(invalidPayload)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testStopMonitoringWithValidPayload() throws Exception {
        mockMvc.perform(post(MONITORING_STOP_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(validPayload)))
                .andExpect(status().isOk());
    }

    @Test
    public void testStopMonitoringWithInvalidPayload() throws Exception {
        mockMvc.perform(post(MONITORING_STOP_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(invalidPayload)))
                .andExpect(status().isBadRequest());
    }
}