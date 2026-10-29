package com.oops.payroll.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Stores one attendance record for an employee.
 * This class demonstrates encapsulation and simple daily attendance tracking.
 */
@Entity
@Table(name = "attendance_records")
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int attendanceId;
    private int employeeId;
    private LocalDate date;
    private String status;

    public Attendance() {
    }

    public Attendance(int attendanceId, int employeeId, LocalDate date, String status) {
        setAttendanceId(attendanceId);
        setEmployeeId(employeeId);
        setDate(date);
        setStatus(status);
    }

    public int getAttendanceId() {
        return attendanceId;
    }

    public void setAttendanceId(int attendanceId) {
        if (attendanceId <= 0) {
            throw new IllegalArgumentException("Attendance ID must be positive.");
        }
        this.attendanceId = attendanceId;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        if (employeeId <= 0) {
            throw new IllegalArgumentException("Employee ID must be positive.");
        }
        this.employeeId = employeeId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null.");
        }
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("Attendance status cannot be empty.");
        }
        String normalized = status.trim().toUpperCase();
        if (!"AVAILABLE".equals(normalized) && !"UNAVAILABLE".equals(normalized)) {
            throw new IllegalArgumentException("Status must be AVAILABLE or UNAVAILABLE.");
        }
        this.status = normalized;
    }
}
