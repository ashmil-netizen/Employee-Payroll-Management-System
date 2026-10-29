const API_BASE_URL = 'http://localhost:8080/api';

async function request(path, options) {
  const response = await fetch(`${API_BASE_URL}${path}`, options);
  if (!response.ok) {
    const body = await response.json().catch(() => ({}));
    throw new Error(body.message || 'Request failed');
  }
  if (response.status === 204) return null;
  return response.json();
}

export async function loginUser(name, password) {
  const params = new URLSearchParams({ name, password });

  return request('/login', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded'
    },
    body: params
  });
}

export async function getEmployees() {
  return request('/employees');
}

export async function getDepartments() {
  return request('/departments');
}

export async function addEmployee(employee) {
  return request('/employees', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(employee)
  });
}

export async function deleteEmployee(employeeId) {
  return request(`/employees/${employeeId}`, { method: 'DELETE' });
}

export async function getPayrollByEmployeeId(employeeId) {
  return request(`/payroll/${employeeId}`);
}

export async function getAttendanceByEmployeeId(employeeId) {
  return request(`/attendance/${employeeId}`);
}

export async function getAllAttendance() {
  return request('/attendance');
}

export async function markAttendance(employeeId, status) {
  const params = new URLSearchParams({ employeeId, status });
  return request('/attendance', {
    method: 'POST',
    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    body: params
  });
}

export async function submitLeaveRequest(leaveRequest) {
  return request('/leave', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(leaveRequest)
  });
}

export async function getLeaveRequests() {
  return request('/leave');
}

export async function updateLeaveRequest(leaveId, decision) {
  return request(`/leave/${decision}/${leaveId}`, { method: 'POST' });
}
