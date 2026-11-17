package com.simulador.financiero.controllers;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/batch")
@Tag(name = "Batch-controller", description = "Ejecución de procesos batch")
public class BatchController {

    private final JobLauncher jobLauncher;
    private final Job dailyQuotesJob;

    public BatchController(JobLauncher jobLauncher, Job dailyQuotesJob) {
        this.jobLauncher = jobLauncher;
        this.dailyQuotesJob = dailyQuotesJob;
    }

    @PostMapping("/quotes")
    @Operation(summary = "Ejecutar job de cotizaciones", description = "Lanza el job batch que actualiza las cotizaciones diarias de las acciones.")
    @ApiResponse(responseCode = "200", description = "Job iniciado", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string", example = "Job iniciado")))
    public String runJob() throws Exception {

        JobParameters parameters = new JobParametersBuilder()
                .addLong("execution.timestamp", System.currentTimeMillis())
                .toJobParameters();

        jobLauncher.run(dailyQuotesJob, parameters);

        return "Job iniciado";
    }
}