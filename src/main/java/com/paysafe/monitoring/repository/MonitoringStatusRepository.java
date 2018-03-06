package com.paysafe.monitoring.repository;

import com.paysafe.monitoring.model.ServerStatus;
import com.paysafe.monitoring.model.StatusResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class MonitoringStatusRepository {

    private Map<String, LinkedList<ServerStatus>> serverStatusMap = new HashMap<>();

    public Map<String, LinkedList<ServerStatus>> saveServerStatus(String url, StatusResponse response){

        LinkedList<ServerStatus> serverStat = new LinkedList<>();
        serverStat.add(ServerStatus.builder().date(LocalDateTime.now()).status(response.getStatus()).build());
        serverStatusMap.put(url, serverStat);
        return serverStatusMap;
    }

    public List<ServerStatus> getMonitoringStatus(String url) {
        List<ServerStatus> serverStatusList =  serverStatusMap.entrySet()
                .stream()
                .filter(p -> p.getKey().equals(url))
                .flatMap(as -> as.getValue().stream())
                .collect(Collectors.toList());
        return serverStatusList;
    }
}
