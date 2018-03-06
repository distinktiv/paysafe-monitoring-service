package com.paysafe.monitoring.repository;

import com.paysafe.monitoring.model.ServerStatus;
import com.paysafe.monitoring.model.StatusResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class MonitoringStatusRepository {

    private Map<String, ServerStatus> serverStatusMap = new HashMap<>();

    public Map<String, ServerStatus> saveServerStatus(String url, StatusResponse response){
        serverStatusMap.put(url, ServerStatus.builder().date(LocalDateTime.now()).status(response.getStatus()).build());
        return serverStatusMap;
    }

    public List<ServerStatus> getMonitoringStatus(String url) {
        List<ServerStatus> serverStatusList =  serverStatusMap.entrySet()
                .stream()
                .filter(p -> p.getKey().equals(url))
                .map(map -> map.getValue())
                .collect(Collectors.toList());

        return serverStatusList;
    }
}
