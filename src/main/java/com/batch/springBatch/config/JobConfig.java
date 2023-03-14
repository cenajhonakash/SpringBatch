package com.batch.springBatch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.partition.PartitionHandler;
import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.batch.springBatch.entity.Employee;

@Configuration
public class JobConfig {

	@Value("${app.batch.step.name}")
	private String batchStepName;

	@Value("${app.batch.chunk.size}")
	private int chunkSize;

	@Value("${app.batch.job.name}")
	private String jobName;

	@Value("${app.batch.gridsize}")
	private int gridSize;

	@Value("${app.thread.pool.size.max}")
	private int maxPoolSize;

	@Value("${app.thread.core.size}")
	private int corePoolSize;

	@Value("${app.thread.queue.capacity}")
	private int queueCapacity;

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
	@Autowired
	CustomEmployeWriter customEmployeWriter;

	/*
	 * Will perform read, process & write
	 */
	@Bean
	Step slaveStep() {
		return this.stepBuilderFactory.get(batchStepName + "SLAVE").<Employee, Employee>chunk(chunkSize).reader(reader).processor(processor).writer(customEmployeWriter).build();
	}

	/*
	 * Will take care partition related configuration
	 */
	@Bean
	Step masterStep() {
		return this.stepBuilderFactory.get(batchStepName + "MASTER").partitioner(slaveStep().getName(), partitioner()).partitionHandler(partitionHandler()).build();
	}

	@Bean
	Job runJob() {
		return this.jobBuilderFactory.get(jobName).flow(masterStep()).end().build();
	}

	@Bean
	PartitionHandler partitionHandler() {

		TaskExecutorPartitionHandler taskExecutorPartitionHandler = new TaskExecutorPartitionHandler();
		taskExecutorPartitionHandler.setGridSize(gridSize);
		taskExecutorPartitionHandler.setTaskExecutor(taskExecutor());
		taskExecutorPartitionHandler.setStep(slaveStep());

		return taskExecutorPartitionHandler;
	}

	@Bean
	TaskExecutor taskExecutor() {
		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
		taskExecutor.setMaxPoolSize(maxPoolSize);
		taskExecutor.setCorePoolSize(corePoolSize);
		taskExecutor.setQueueCapacity(queueCapacity);

		return taskExecutor;
	}

	@Bean
	DataRangePartitioner partitioner() {
		return new DataRangePartitioner();
	}
}
