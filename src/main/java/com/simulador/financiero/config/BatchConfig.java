package com.simulador.financiero.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.simulador.financiero.services.IConsultarApiExternaService;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class BatchConfig {

    private final IConsultarApiExternaService consultarApiExternaService;
    private final List<String> symbols;

    public BatchConfig(
            IConsultarApiExternaService consultarApiExternaService,
            @Value("${app.batch.symbols:}") String configuredSymbols) {

        this.consultarApiExternaService = consultarApiExternaService;

        this.symbols = Arrays.stream(configuredSymbols.split(","))
                .map(String::trim)
                .filter(symbol -> !symbol.isBlank())
                .toList();
    }

    @Bean
    public Job dailyQuotesJob(JobRepository jobRepository, Step dailyQuotesStep) {
        return new JobBuilder("dailyQuotesJob", jobRepository)
                .start(dailyQuotesStep)
                .build();
    }

    @Bean
    public Step dailyQuotesStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager) {

        return new StepBuilder("dailyQuotesStep", jobRepository)
                .<String, String>chunk(1, transactionManager)
                .reader(symbolReader())
                .processor(symbolProcessor())
                .writer(items -> {
                })
                .faultTolerant()
                .skip(RuntimeException.class)
                .skipLimit(symbols.size())
                .build();
    }

    @Bean
    public ItemReader<String> symbolReader() {
        return new ListItemReader<>(symbols);
    }

    @Bean
    public ItemProcessor<String, String> symbolProcessor() {
        return symbol -> {
            try {
                consultarApiExternaService.consultarYGuardarCotizacion(symbol);
                log.info("Cotización actualizada correctamente para {}", symbol);

                return symbol;

            } catch (RuntimeException exception) {
                log.error(
                        "No se pudo actualizar la cotización del símbolo {}",
                        symbol,
                        exception);

                throw exception;
            }
        };
    }
}