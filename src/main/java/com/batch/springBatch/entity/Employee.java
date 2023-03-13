package com.batch.springBatch.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "employee")
@AllArgsConstructor
@NoArgsConstructor
public class Employee {

	@Id
	private Long empId;
	private String first_name;
	private String last_name;
	private String dept;
	private Long salary;
	
}
