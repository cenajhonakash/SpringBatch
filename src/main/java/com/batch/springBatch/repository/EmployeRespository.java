package com.batch.springBatch.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.batch.springBatch.entity.Employee;

public interface EmployeRespository extends JpaRepository<Employee, Long>{

}
