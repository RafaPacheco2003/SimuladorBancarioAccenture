package com.simulador.financiero.controllers;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/batch")
public class BatchController {

    private final JobLauncher jobLauncher;
    private final Job dailyQuotesJob;

    public BatchController(JobLauncher jobLauncher, Job dailyQuotesJob) {
        this.jobLauncher = jobLauncher;
        this.dailyQuotesJob = dailyQuotesJob;
    }

    @PostMapping("/quotes")
    public String runJob() throws Exception {

        JobParameters parameters = new JobParametersBuilder()
                .addLong("execution.timestamp", System.currentTimeMillis())
                .toJobParameters();

        jobLauncher.run(dailyQuotesJob, parameters);

        return "Job iniciado";
    }
}