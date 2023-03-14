package com.batch.springBatch.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/jobs")
public class JobController {

	@Autowired
	private JobLauncher jobLauncher;
	@Autowired
	private Job job;

	@PostMapping("/dump-employee-data")
	public ResponseEntity<String> runJob() {
		JobParameters params = new JobParametersBuilder().addLong("startAt", System.currentTimeMillis()).toJobParameters();
		String message = null;
		try {
			jobLauncher.run(job, params);
			message = "Data successfully dumped!!!";
		} catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
			message = "Error in dumping data!!!";
			e.printStackTrace();
		}
		return new ResponseEntity<String>(message, HttpStatus.OK);
	}

	@PostMapping("/dump-employee-data/scaled")
	public ResponseEntity<String> runJobParallely() {
		JobParameters params = new JobParametersBuilder().addLong("startAt", System.currentTimeMillis()).toJobParameters();
		String message = null;
		try {
			jobLauncher.run(job, params);
			message = "Data successfully dumped!!!";
		} catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
			message = "Error in dumping data!!!";
			e.printStackTrace();
		}
		return new ResponseEntity<String>(message, HttpStatus.OK);
	}
}
