package com.oops.payroll.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.oops.payroll.model.Department;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {
}