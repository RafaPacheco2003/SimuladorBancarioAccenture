package com.simulador.financiero.batch;

import java.time.Instant;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@ConditionalOnProperty(
        name = "app.batch.scheduler.enabled",
        havingValue = "true")
public class DailyQuotesScheduler implements ApplicationRunner {

    private final JobLauncher jobLauncher;
    private final Job dailyQuotesJob;

    private long lastExecutionTimestamp;

    public DailyQuotesScheduler(
            JobLauncher jobLauncher,
            Job dailyQuotesJob) {

        this.jobLauncher = jobLauncher;
        this.dailyQuotesJob = dailyQuotesJob;
    }

    @Scheduled(
            cron = "${app.batch.scheduler.cron:0 0 2 * * *}",
            zone = "${app.batch.scheduler.zone:UTC}")
    public void runDailyQuotesJob() {

        launchDailyQuotesJob("programado");
    }

    @Override
    public void run(ApplicationArguments args) {

        launchDailyQuotesJob("inicial");
    }

    private void launchDailyQuotesJob(String executionType) {

        JobParameters parameters = new JobParametersBuilder()
                .addLong(
                        "execution.timestamp",
                        nextExecutionTimestamp())
                .toJobParameters();

        try {
            log.info("Iniciando Job {} de cotizaciones", executionType);

            jobLauncher.run(dailyQuotesJob, parameters);

        } catch (Exception exception) {
            log.error(
                    "No se pudo iniciar el Job {} de cotizaciones",
                    executionType,
                    exception);
        }
    }

    private synchronized long nextExecutionTimestamp() {

        long currentTimestamp = Instant.now().toEpochMilli();

        lastExecutionTimestamp = Math.max(
                currentTimestamp,
                lastExecutionTimestamp + 1);

        return lastExecutionTimestamp;
    }
}
