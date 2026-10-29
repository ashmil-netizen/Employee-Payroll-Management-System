import { useEffect, useState } from 'react';
import { getAttendanceByEmployeeId, getLeaveRequests, submitLeaveRequest } from '../services/api';

function EmployeeAttendance() {
  const employee = JSON.parse(localStorage.getItem('payrollUser') || '{}');
  const [attendance, setAttendance] = useState({ present: 0, absent: 0, percentage: 0 });
  const [leaveRequests, setLeaveRequests] = useState([]);
  const [form, setForm] = useState({
    leaveType: 'Paid Leave',
    fromDate: '',
    toDate: '',
    reason: ''
  });
  const [message, setMessage] = useState('');
  const [error, setError] = useState('');

  useEffect(() => {
    if (!employee.employeeId) return;

    Promise.all([
      getAttendanceByEmployeeId(employee.employeeId),
      getLeaveRequests()
    ])
      .then(([summary, requests]) => {
        setAttendance(summary);
        setLeaveRequests(requests.filter((request) => request.employeeId === employee.employeeId));
      })
      .catch((requestError) => setError(requestError.message));
  }, [employee.employeeId]);

  const handleChange = (event) => {
    const { name, value } = event.target;
    setForm((previous) => ({ ...previous, [name]: value }));
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    setMessage('');
    setError('');

    try {
      const request = await submitLeaveRequest({
        employeeId: employee.employeeId,
        leaveType: form.leaveType,
        fromDate: form.fromDate,
        toDate: form.toDate,
        reason: form.reason
      });
      setLeaveRequests((previous) => [...previous, request]);
      setMessage('Leave request submitted.');
      setForm((previous) => ({ ...previous, fromDate: '', toDate: '', reason: '' }));
    } catch (requestError) {
      setError(requestError.message);
    }
  };

  return (
    <div className="min-h-screen bg-slate-100 p-6">
      <div className="mx-auto max-w-6xl space-y-6">
        <div className="rounded-2xl bg-white p-6 shadow-sm ring-1 ring-slate-200">
          <p className="text-sm font-semibold uppercase tracking-[0.2em] text-indigo-600">Attendance</p>
          <h1 className="mt-2 text-3xl font-bold text-slate-900">Attendance Percentage</h1>
        </div>

        <div className="grid gap-6 lg:grid-cols-[1.1fr_1.5fr]">
          <div className="rounded-2xl bg-white p-6 shadow-sm ring-1 ring-slate-200">
            <div className="text-5xl font-bold text-slate-900">{Number(attendance.percentage).toFixed(1)}%</div>
            <div className="mt-6 space-y-3 text-slate-700">
              <p><span className="font-semibold">Present:</span> {attendance.present}</p>
              <p><span className="font-semibold">Absent:</span> {attendance.absent}</p>
            </div>
            <h2 className="mt-6 font-semibold text-slate-900">My leave requests</h2>
            {leaveRequests.length === 0 ? (
              <p className="mt-2 text-sm text-slate-500">No leave requests yet.</p>
            ) : (
              <ul className="mt-2 space-y-2 text-sm text-slate-700">
                {leaveRequests.map((request) => (
                  <li key={request.leaveId}>
                    {request.leaveType}: {request.fromDate} to {request.toDate} ({request.status})
                  </li>
                ))}
              </ul>
            )}
          </div>

          <div className="rounded-2xl bg-white p-6 shadow-sm ring-1 ring-slate-200">
            <h2 className="mb-4 text-2xl font-bold text-slate-900">Leave Application</h2>
            <form onSubmit={handleSubmit} className="space-y-4">
              <div>
                <label className="mb-2 block text-sm font-medium text-slate-700">Leave Type</label>
                <select
                  name="leaveType"
                  value={form.leaveType}
                  onChange={handleChange}
                  className="w-full rounded-xl border border-slate-300 bg-slate-50 px-3 py-2.5"
                >
                  <option>Paid Leave</option>
                  <option>Medical Leave</option>
                  <option>Casual Leave</option>
                  <option>Other</option>
                </select>
              </div>

              <div className="grid gap-4 md:grid-cols-2">
                <div>
                  <label className="mb-2 block text-sm font-medium text-slate-700">From Date</label>
                  <input
                    type="date"
                    name="fromDate"
                    value={form.fromDate}
                    onChange={handleChange}
                    required
                    className="w-full rounded-xl border border-slate-300 bg-slate-50 px-3 py-2.5"
                  />
                </div>
                <div>
                  <label className="mb-2 block text-sm font-medium text-slate-700">To Date</label>
                  <input
                    type="date"
                    name="toDate"
                    value={form.toDate}
                    onChange={handleChange}
                    min={form.fromDate}
                    required
                    className="w-full rounded-xl border border-slate-300 bg-slate-50 px-3 py-2.5"
                  />
                </div>
              </div>

              <div>
                <label className="mb-2 block text-sm font-medium text-slate-700">Reason</label>
                <textarea
                  name="reason"
                  value={form.reason}
                  onChange={handleChange}
                  rows="4"
                  required
                  className="w-full rounded-xl border border-slate-300 bg-slate-50 px-3 py-2.5"
                  placeholder="Explain your reason"
                />
              </div>

              <button
                type="submit"
                disabled={!employee.employeeId}
                className="rounded-xl bg-indigo-600 px-4 py-3 font-semibold text-white hover:bg-indigo-700"
              >
                Submit
              </button>
              {message && <p className="text-sm text-emerald-700">{message}</p>}
              {error && <p role="alert" className="text-sm text-red-700">{error}</p>}
            </form>
          </div>
        </div>
      </div>
    </div>
  );
}

export default EmployeeAttendance;
