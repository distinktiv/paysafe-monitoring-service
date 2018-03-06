package com.paysafe.monitoring.controller;

import com.paysafe.monitoring.model.MonitoringParams;
import com.paysafe.monitoring.model.ServerStatus;
import com.paysafe.monitoring.service.MonitoringService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/monitoring")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MonitoringController {

    private final MonitoringService monitoringService;

    @RequestMapping(value = "/start", method = RequestMethod.POST)
    public ResponseEntity<String> startMonitoring(@Valid @RequestBody MonitoringParams monitoringParams){
        monitoringService.start(monitoringParams);
        return new ResponseEntity<>("Monitoring Started", HttpStatus.OK);
    }

    @RequestMapping(value = "/stop", method = RequestMethod.POST)
    public ResponseEntity stopMonitoring(@Valid @RequestBody MonitoringParams monitoringParams){
        monitoringService.stop(monitoringParams);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/status", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ServerStatus>> getMonitoringStatus(@RequestParam("url") String url){
        List<ServerStatus> statusList = monitoringService.getStatus(url);
        return new ResponseEntity<List<ServerStatus>>(statusList, HttpStatus.OK);
    }
}
