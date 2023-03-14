package com.batch.springBatch.config;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.batch.springBatch.entity.Employee;
import com.batch.springBatch.repository.EmployeRespository;

@Component
public class CustomEmployeWriter implements ItemWriter<Employee> {

	@Autowired
	private EmployeRespository employeRespository;

	@Override
	public void write(List<? extends Employee> items) throws Exception {
		System.out.println("Thread: " + Thread.currentThread().getName() + " saving total items: " + items.size());
		employeRespository.saveAll(items);
	}

}
