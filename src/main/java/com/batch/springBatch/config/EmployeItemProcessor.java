package com.batch.springBatch.config;

import org.springframework.batch.item.ItemProcessor;

import com.batch.springBatch.entity.Employee;

public class EmployeItemProcessor implements ItemProcessor<Employee, Employee>{

	@Override
	public Employee process(Employee item) throws Exception {
		return item;
	}

}
