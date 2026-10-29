import { Link, useLocation } from 'react-router-dom';
import { useEffect, useState } from 'react';
import { getAttendanceByEmployeeId, markAttendance } from '../services/api';

function EmployeeDashboard() {
  const location = useLocation();
  const employee = location.state?.employee || JSON.parse(localStorage.getItem('payrollUser') || 'null') || {
    name: 'Employee',
    employeeId: 'N/A'
  };
  const [attendance, setAttendance] = useState({ present: 0, absent: 0, percentage: 0 });
  const [error, setError] = useState('');

  useEffect(() => {
    if (!employee.employeeId || employee.employeeId === 'N/A') return;
    getAttendanceByEmployeeId(employee.employeeId)
      .then(setAttendance)
      .catch((requestError) => setError(requestError.message));
  }, [employee.employeeId]);

  const recordAttendance = async (status) => {
    try {
      await markAttendance(employee.employeeId, status);
      setAttendance(await getAttendanceByEmployeeId(employee.employeeId));
      setError('');
    } catch (requestError) {
      setError(requestError.message);
    }
  };

  return (
    <div className="min-h-screen bg-slate-100 p-6">
      <div className="mx-auto max-w-6xl">
        <div className="mb-6 rounded-2xl bg-white p-6 shadow-sm ring-1 ring-slate-200">
          <p className="text-sm font-semibold uppercase tracking-[0.2em] text-indigo-600">Dashboard</p>
          <h1 className="mt-2 text-3xl font-bold text-slate-900">Good Morning, {employee.name}</h1>
        </div>

        <div className="grid gap-6 md:grid-cols-3">
          <div className="rounded-2xl bg-white p-5 shadow-sm ring-1 ring-slate-200">
            <p className="text-sm text-slate-500">Employee ID</p>
            <p className="mt-2 text-2xl font-bold text-slate-900">{employee.employeeId || 'N/A'}</p>
          </div>
          <div className="rounded-2xl bg-white p-5 shadow-sm ring-1 ring-slate-200">
            <p className="text-sm text-slate-500">Employee Name</p>
            <p className="mt-2 text-2xl font-bold text-slate-900">{employee.name}</p>
          </div>
          <div className="rounded-2xl bg-white p-5 shadow-sm ring-1 ring-slate-200">
            <p className="text-sm text-slate-500">Current Attendance</p>
            <div className="mt-3 flex gap-3">
              <button onClick={() => recordAttendance('PRESENT')} className="rounded-lg bg-emerald-600 px-3 py-2 text-sm font-semibold text-white">Present</button>
              <button onClick={() => recordAttendance('ABSENT')} className="rounded-lg bg-red-500 px-3 py-2 text-sm font-semibold text-white">Absent</button>
            </div>
            <p className="mt-3 text-sm text-slate-600">{Number(attendance.percentage).toFixed(1)}% attendance</p>
            {error && <p role="alert" className="mt-2 text-sm text-red-700">{error}</p>}
          </div>
        </div>

        <div className="mt-6 grid gap-6 md:grid-cols-2">
          <Link to="/attendance" className="rounded-2xl bg-indigo-600 p-6 text-white shadow-sm transition hover:bg-indigo-700">
            <p className="text-sm uppercase tracking-[0.2em] text-indigo-100">Employee</p>
            <h2 className="mt-2 text-2xl font-bold">Attendance & Leave</h2>
          </Link>

          <Link to="/salary" className="rounded-2xl bg-emerald-600 p-6 text-white shadow-sm transition hover:bg-emerald-700">
            <p className="text-sm uppercase tracking-[0.2em] text-emerald-100">Payroll</p>
            <h2 className="mt-2 text-2xl font-bold">Salary Slip</h2>
          </Link>

        </div>
      </div>
    </div>
  );
}

export default EmployeeDashboard;
