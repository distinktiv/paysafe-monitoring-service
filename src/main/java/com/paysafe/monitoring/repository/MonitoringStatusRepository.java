package com.paysafe.monitoring.repository;

import com.paysafe.monitoring.model.ServerStatus;
import com.paysafe.monitoring.model.StatusResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class MonitoringStatusRepository {

    private Map<String, LinkedList<ServerStatus>> serverStatusMap = new HashMap<>();

    public void saveServerStatus(String url, StatusResponse response){
        LinkedList<ServerStatus> linkedServerStatus;

        if(serverStatusMap.get(url) != null){
            linkedServerStatus  = serverStatusMap.get(url);
        } else{
            linkedServerStatus = new LinkedList<>();
        }

        linkedServerStatus.add(ServerStatus.builder().date(LocalDateTime.now()).status(response.getStatus()).build());

        serverStatusMap.put(url, linkedServerStatus);
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
