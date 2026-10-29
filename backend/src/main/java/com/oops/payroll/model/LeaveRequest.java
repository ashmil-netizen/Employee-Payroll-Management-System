package com.oops.payroll.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Represents a leave request submitted by an employee.
 * The request stores the leave type, dates, reason, and approval status.
 */
@Entity
@Table(name = "leave_requests")
public class LeaveRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int leaveId;
    private int employeeId;
    private String leaveType;
    private LocalDate fromDate;
    private LocalDate toDate;
    private String reason;
    private String status;

    public LeaveRequest() {
        this.status = "PENDING";
    }

    public LeaveRequest(int leaveId, int employeeId, String leaveType,
                        LocalDate fromDate, LocalDate toDate, String reason, String status) {
        setLeaveId(leaveId);
        setEmployeeId(employeeId);
        setLeaveType(leaveType);
        setFromDate(fromDate);
        setToDate(toDate);
        setReason(reason);
        setStatus(status);
    }

    public int getLeaveId() {
        return leaveId;
    }

    public void setLeaveId(int leaveId) {
        if (leaveId <= 0) {
            throw new IllegalArgumentException("Leave ID must be positive.");
        }
        this.leaveId = leaveId;
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

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        if (leaveType == null || leaveType.trim().isEmpty()) {
            throw new IllegalArgumentException("Leave type cannot be empty.");
        }
        this.leaveType = leaveType.trim();
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        if (fromDate == null) {
            throw new IllegalArgumentException("From date cannot be null.");
        }
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        if (toDate == null) {
            throw new IllegalArgumentException("To date cannot be null.");
        }
        if (toDate.isBefore(fromDate)) {
            throw new IllegalArgumentException("To date cannot be before from date.");
        }
        this.toDate = toDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        if (reason == null || reason.trim().isEmpty()) {
            throw new IllegalArgumentException("Reason cannot be empty.");
        }
        this.reason = reason.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            this.status = "PENDING";
            return;
        }

        String normalized = status.trim().toUpperCase();
        if (!"PENDING".equals(normalized) && !"APPROVED".equals(normalized) && !"REJECTED".equals(normalized)) {
            throw new IllegalArgumentException("Status must be PENDING, APPROVED, or REJECTED.");
        }
        this.status = normalized;
    }
}
