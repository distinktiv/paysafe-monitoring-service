package com.paysafe.monitoring.job;

import com.paysafe.monitoring.client.PaysafeClient;
import com.paysafe.monitoring.model.StatusResponse;
import com.paysafe.monitoring.repository.MonitoringStatusRepository;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MonitoringJob implements Job {

    @Autowired
    private MonitoringStatusRepository monitoringStatusRepository;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        String monitoring_url = (String) context.getJobDetail().getJobDataMap().get("url");
        PaysafeClient paysafeClient = Feign.builder().encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder()).target(PaysafeClient.class, monitoring_url);

        StatusResponse response = paysafeClient.getServiceStatus();

        monitoringStatusRepository.saveServerStatus(monitoring_url, response);
    }
}
