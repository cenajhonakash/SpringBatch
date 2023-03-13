package com.batch.springBatch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.batch.springBatch.entity.Employee;

@Configuration
public class JobConfig {

	@Value("${app.batch.step.name}")
	private String batchStepName;

	@Value("${app.batch.chunk.size}")
	private int chunkSize;

	@Value("${app.batch.job.name}")
	private String jobName;

	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	@Autowired
	private ItemReader<Employee> reader;
	@Autowired
	private ItemProcessor<Employee, Employee> processor;
	@Autowired
	ItemWriter<Employee> writer;

	@Bean
	public Step step1() {
		return this.stepBuilderFactory.get(batchStepName).<Employee, Employee>chunk(chunkSize).reader(reader).processor(processor).writer(writer).build();
	}

	@Bean
	public Job runJob() {
		return this.jobBuilderFactory.get(jobName).flow(step1()).end().build();
	}

}
