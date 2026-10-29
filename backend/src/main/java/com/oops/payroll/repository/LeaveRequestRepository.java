package com.oops.payroll.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.oops.payroll.model.LeaveRequest;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Integer> {
    List<LeaveRequest> findAllByEmployeeId(int employeeId);
    void deleteAllByEmployeeId(int employeeId);
}