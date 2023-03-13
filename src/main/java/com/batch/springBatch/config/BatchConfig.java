package com.batch.springBatch.config;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import com.batch.springBatch.entity.Employee;
import com.batch.springBatch.repository.EmployeRespository;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

	@Value("${app.batch.file.name}")
	private String fileName;

	@Value("${app.batch.file.isHeader}")
	private String isHeaderPresent;

	@Value("${app.batch.file.headers.include}")
	private String[] fieldNames;

	@Value("${app.batch.file.headers.index}")
	private int[] fieldIndex;

	@Autowired
	private EmployeRespository employeRespository;

	@Bean
	FlatFileItemReader<Employee> reader() {

		FlatFileItemReader<Employee> dataReader = new FlatFileItemReader<>();
		dataReader.setResource(new FileSystemResource(fileName));
		dataReader.setLineMapper(getLineMapper());
		if (isHeaderPresent.equalsIgnoreCase("Y")) {
			dataReader.setLinesToSkip(1);// skipping Header
		}
		return dataReader;
	}

	@Bean
	LineMapper<Employee> getLineMapper() {

		DefaultLineMapper<Employee> dataLineMapper = new DefaultLineMapper<>();

		DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
		lineTokenizer.setDelimiter(",");
		// lineTokenizer.setStrict(true);//by default it is true
		lineTokenizer.setNames(fieldNames);
		lineTokenizer.setIncludedFields(fieldIndex);

		BeanWrapperFieldSetMapper<Employee> fieldSetter = new BeanWrapperFieldSetMapper<>();
		fieldSetter.setTargetType(Employee.class);

		dataLineMapper.setLineTokenizer(lineTokenizer);
		dataLineMapper.setFieldSetMapper(fieldSetter);

		return dataLineMapper;
	}

	@Bean
	EmployeItemProcessor processor() {
		return new EmployeItemProcessor();
	}

	@Bean
	RepositoryItemWriter<Employee> writer() {

		RepositoryItemWriter<Employee> dataWriter = new RepositoryItemWriter<>();
		dataWriter.setRepository(employeRespository);
		dataWriter.setMethodName("save");

		return dataWriter;
	}
}
