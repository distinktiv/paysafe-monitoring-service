package com.paysafe.monitoring.service;

import com.paysafe.monitoring.job.MonitoringJob;
import com.paysafe.monitoring.model.MonitoringParams;
import com.paysafe.monitoring.model.ServerStatus;
import com.paysafe.monitoring.repository.MonitoringStatusRepository;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

@Slf4j
@Service
public class MonitoringService {


    private final Scheduler scheduler;
    private final MonitoringStatusRepository monitoringStatusRepository;

    public static final String JOB = "job_";
    public static final String GROUP = "job_group_";
    public static final String SERVER_URL = "url";
    public static final String TRIGGER = "trigger_";
    public static final String TRIGGER_GROUP = "trigger_group_";

    public MonitoringService(Scheduler scheduler, MonitoringStatusRepository monitoringStatusRepository) {
        this.scheduler = scheduler;
        this.monitoringStatusRepository = monitoringStatusRepository;
    }

    public void start(MonitoringParams monitoringParams){

        try {
            int params_hash_code = monitoringParams.hashCode();
            int intervalInSeconds = monitoringParams.getInterval();
            String jobName = JOB + params_hash_code;
            String jobGroup = GROUP + params_hash_code;
            String triggerName = TRIGGER + params_hash_code;
            String triggerGroup = TRIGGER_GROUP + params_hash_code;

            JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
            if(!scheduler.checkExists(jobKey)){
                createJob(monitoringParams, intervalInSeconds, triggerName, triggerGroup, jobKey);
            } else {
                updateInterval(intervalInSeconds, triggerName);
            }
        } catch (SchedulerException e) {
            log.error("Error while starting job ",  e);
        }
    }

    public void stop(MonitoringParams monitoringParams){
        try {
            JobKey jobKey = JobKey.jobKey(JOB + monitoringParams.hashCode(), GROUP + monitoringParams.hashCode());
            if(scheduler.checkExists(jobKey)){
                scheduler.deleteJob(jobKey);
            }
        } catch (SchedulerException e) {
            log.error("Error while stopping Job ", e);
        }
    }

    public List<ServerStatus> getStatus(String url){
         return monitoringStatusRepository.getMonitoringStatus(url);
    }

    private void createJob(MonitoringParams monitoringParams, int intervalInSeconds, String triggerName, String triggerGroup, JobKey jobKey) throws SchedulerException {
        JobDetail jobDetail = newJob(MonitoringJob.class)
                .withIdentity(jobKey)
                .build();

        jobDetail.getJobDataMap().put(SERVER_URL, monitoringParams.getUrl());

        Trigger trigger = newTrigger()
                .withIdentity(triggerName, triggerGroup)
                .startNow()
                .withSchedule(simpleSchedule()
                        .withIntervalInSeconds(intervalInSeconds)
                        .repeatForever())
                .build();
        scheduler.scheduleJob(jobDetail, trigger);
    }

    private void updateInterval(int intervalInSeconds, String triggerName) throws SchedulerException {
        Trigger oldTrigger = scheduler.getTrigger(TriggerKey.triggerKey(triggerName));
        TriggerBuilder tb = oldTrigger.getTriggerBuilder();
        Trigger newTrigger = tb
                .withSchedule(simpleSchedule()
                        .withIntervalInSeconds(intervalInSeconds)
                        .repeatForever())
                .build();
        scheduler.rescheduleJob(oldTrigger.getKey(), newTrigger);
    }
}
