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
    private LinkedList<ServerStatus> serverStat = new LinkedList<>();

    public Map<String, LinkedList<ServerStatus>> saveServerStatus(String url, StatusResponse response){
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

    public void deleteStatus(String url) {
        serverStatusMap.get(url).clear();
    }
}
