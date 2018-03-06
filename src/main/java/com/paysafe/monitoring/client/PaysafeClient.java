package com.paysafe.monitoring.client;

import com.paysafe.monitoring.model.StatusResponse;
import feign.RequestLine;

public interface PaysafeClient {
    @RequestLine("GET /accountmanagement/monitor")
    StatusResponse getServiceStatus();
}
