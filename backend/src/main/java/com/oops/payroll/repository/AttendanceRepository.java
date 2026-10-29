package com.oops.payroll.repository;

import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;

import com.oops.payroll.model.Attendance;

public interface AttendanceRepository extends JpaRepository<Attendance, Integer> {
    List<Attendance> findAllByEmployeeId(int employeeId);
    Optional<Attendance> findFirstByEmployeeIdAndDateOrderByAttendanceIdDesc(int employeeId, LocalDate date);
    void deleteAllByEmployeeId(int employeeId);
}