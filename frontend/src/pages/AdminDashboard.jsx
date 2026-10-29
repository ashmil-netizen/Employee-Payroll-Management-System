import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { addEmployee, deleteEmployee, getAllAttendance, getDepartments, getEmployees } from '../services/api';

const emptyForm = {
  employeeId: '',
  name: '',
  password: '',
  designation: '',
  departmentId: '',
  employeeType: 'FULL_TIME',
  basicSalary: '',
  allowances: '0',
  overtimeHours: '0',
  bonus: '0',
  deductions: '0',
  hourlyRate: '',
  hoursWorked: '',
  contractAmount: ''
};

const money = (value) => `₹${Number(value || 0).toLocaleString('en-IN')}`;

function AdminDashboard() {
  const [employees, setEmployees] = useState([]);
  const [departments, setDepartments] = useState([]);
  const [todayAttendance, setTodayAttendance] = useState([]);
  const [form, setForm] = useState(emptyForm);
  const [error, setError] = useState('');
  const [message, setMessage] = useState('');
  const [saving, setSaving] = useState(false);

  useEffect(() => {
    Promise.all([getEmployees(), getAllAttendance(), getDepartments()])
      .then(([employeeData, attendanceData, departmentData]) => {
        const today = new Date().toISOString().slice(0, 10);
        setEmployees(employeeData);
        setTodayAttendance(attendanceData.filter((record) => record.date === today));
        setDepartments(departmentData);
        setForm((previous) => ({
          ...previous,
          departmentId: previous.departmentId || String(departmentData[0]?.departmentId || '')
        }));
      })
      .catch((requestError) => setError(requestError.message));
  }, []);

  const availabilityByEmployee = todayAttendance.reduce((availability, record) => {
    const currentRecord = availability[record.employeeId];
    if (!currentRecord || record.attendanceId > currentRecord.attendanceId) {
      availability[record.employeeId] = record;
    }
    return availability;
  }, {});
  const todaysStatuses = Object.values(availabilityByEmployee);
  const availableToday = todaysStatuses.filter((record) => record.status === 'AVAILABLE').length;
  const unavailableToday = todaysStatuses.filter((record) => record.status === 'UNAVAILABLE').length;

  const handleChange = (event) => {
    const { name, value } = event.target;
    setForm((previous) => ({ ...previous, [name]: value }));
  };

  const handleAddEmployee = async (event) => {
    event.preventDefault();
    setError('');
    setMessage('');
    setSaving(true);

    const employee = { ...form, employeeId: Number(form.employeeId), departmentId: Number(form.departmentId) };
    ['basicSalary', 'allowances', 'overtimeHours', 'bonus', 'deductions', 'hourlyRate', 'hoursWorked', 'contractAmount']
      .forEach((field) => { employee[field] = Number(form[field] || 0); });

    try {
      const createdEmployee = await addEmployee(employee);
      setEmployees((previous) => [...previous, createdEmployee].sort((a, b) => a.employeeId - b.employeeId));
      setForm({ ...emptyForm, departmentId: String(departments[0]?.departmentId || '') });
      setMessage(`${createdEmployee.name} was added.`);
    } catch (requestError) {
      setError(requestError.message);
    } finally {
      setSaving(false);
    }
  };

  const handleRemoveEmployee = async (employee) => {
    if (!window.confirm(`Remove ${employee.name} and their related payroll, attendance, and leave records?`)) return;

    setError('');
    setMessage('');
    try {
      await deleteEmployee(employee.employeeId);
      setEmployees((previous) => previous.filter((item) => item.employeeId !== employee.employeeId));
      setMessage(`${employee.name} was removed.`);
    } catch (requestError) {
      setError(requestError.message);
    }
  };

  return (
    <div className="min-h-screen bg-slate-100 p-6">
      <div className="mx-auto max-w-6xl">
        <div className="mb-6 rounded-2xl bg-white p-6 shadow-sm ring-1 ring-slate-200">
          <p className="text-sm font-semibold uppercase tracking-[0.2em] text-indigo-600">Admin</p>
          <h1 className="mt-2 text-3xl font-bold text-slate-900">Good Morning, Admin</h1>
          <p className="mt-2 text-sm text-slate-500">People operations <span className="mx-2 text-indigo-400">/</span> Today at a glance</p>
          {error && <p role="alert" className="mt-3 text-sm text-red-700">{error}</p>}
          <Link to="/leave-management" className="mt-4 inline-flex rounded-lg bg-slate-900 px-4 py-2 text-sm font-semibold text-white hover:bg-slate-700">
            Review leave requests
          </Link>
        </div>

        <div className="grid gap-6 md:grid-cols-3">
          <div className="rounded-2xl bg-white p-5 shadow-sm ring-1 ring-slate-200">
            <p className="text-sm text-slate-500">Total Employees</p>
            <p className="mt-2 text-3xl font-bold text-slate-900">{employees.length}</p>
          </div>
          <div className="rounded-2xl bg-white p-5 shadow-sm ring-1 ring-slate-200">
            <p className="text-sm text-slate-500">Available Today</p>
            <p className="mt-2 text-3xl font-bold text-emerald-600">{availableToday}</p>
          </div>
          <div className="rounded-2xl bg-white p-5 shadow-sm ring-1 ring-slate-200">
            <p className="text-sm text-slate-500">Unavailable Today</p>
            <p className="mt-2 text-3xl font-bold text-red-500">{unavailableToday}</p>
          </div>
        </div>

        <section className="mt-8 rounded-2xl bg-white p-6 shadow-sm ring-1 ring-slate-200">
          <h2 className="text-xl font-bold text-slate-900">Add employee</h2>
          <form onSubmit={handleAddEmployee} className="mt-5 grid gap-4 md:grid-cols-3">
            <label className="text-sm font-medium text-slate-700">
              Employee ID
              <input name="employeeId" type="number" min="1" value={form.employeeId} onChange={handleChange} required className="mt-1 w-full rounded-lg border border-slate-300 px-3 py-2" />
            </label>
            <label className="text-sm font-medium text-slate-700">
              Name
              <input name="name" value={form.name} onChange={handleChange} required className="mt-1 w-full rounded-lg border border-slate-300 px-3 py-2" />
            </label>
            <label className="text-sm font-medium text-slate-700">
              Temporary password
              <input name="password" type="password" value={form.password} onChange={handleChange} required className="mt-1 w-full rounded-lg border border-slate-300 px-3 py-2" />
            </label>
            <label className="text-sm font-medium text-slate-700">
              Designation
              <input name="designation" value={form.designation} onChange={handleChange} required className="mt-1 w-full rounded-lg border border-slate-300 px-3 py-2" />
            </label>
            <label className="text-sm font-medium text-slate-700">
              Department
              <select name="departmentId" value={form.departmentId} onChange={handleChange} required className="mt-1 w-full rounded-lg border border-slate-300 bg-white px-3 py-2">
                <option value="">Select department</option>
                {departments.map((department) => (
                  <option key={department.departmentId} value={department.departmentId}>{department.departmentName}</option>
                ))}
              </select>
            </label>
            <label className="text-sm font-medium text-slate-700">
              Employee type
              <select name="employeeType" value={form.employeeType} onChange={handleChange} className="mt-1 w-full rounded-lg border border-slate-300 bg-white px-3 py-2">
                <option value="FULL_TIME">Full-time</option>
                <option value="PART_TIME">Part-time</option>
                <option value="CONTRACT">Contract</option>
              </select>
            </label>

            {form.employeeType === 'FULL_TIME' && (
              <>
                <label className="text-sm font-medium text-slate-700">Basic salary
                  <input name="basicSalary" type="number" min="0" step="0.01" value={form.basicSalary} onChange={handleChange} required className="mt-1 w-full rounded-lg border border-slate-300 px-3 py-2" />
                </label>
                <label className="text-sm font-medium text-slate-700">Overtime hours
                  <input name="overtimeHours" type="number" min="0" step="0.1" value={form.overtimeHours} onChange={handleChange} className="mt-1 w-full rounded-lg border border-slate-300 px-3 py-2" />
                </label>
              </>
            )}
            {form.employeeType === 'PART_TIME' && (
              <>
                <label className="text-sm font-medium text-slate-700">Hourly rate
                  <input name="hourlyRate" type="number" min="0" step="0.01" value={form.hourlyRate} onChange={handleChange} required className="mt-1 w-full rounded-lg border border-slate-300 px-3 py-2" />
                </label>
                <label className="text-sm font-medium text-slate-700">Hours worked
                  <input name="hoursWorked" type="number" min="0" step="0.1" value={form.hoursWorked} onChange={handleChange} required className="mt-1 w-full rounded-lg border border-slate-300 px-3 py-2" />
                </label>
              </>
            )}
            {form.employeeType === 'CONTRACT' && (
              <label className="text-sm font-medium text-slate-700">Contract amount
                <input name="contractAmount" type="number" min="0" step="0.01" value={form.contractAmount} onChange={handleChange} required className="mt-1 w-full rounded-lg border border-slate-300 px-3 py-2" />
              </label>
            )}
            <label className="text-sm font-medium text-slate-700">Allowances
              <input name="allowances" type="number" min="0" step="0.01" value={form.allowances} onChange={handleChange} className="mt-1 w-full rounded-lg border border-slate-300 px-3 py-2" />
            </label>
            <label className="text-sm font-medium text-slate-700">Bonus
              <input name="bonus" type="number" min="0" step="0.01" value={form.bonus} onChange={handleChange} className="mt-1 w-full rounded-lg border border-slate-300 px-3 py-2" />
            </label>
            <label className="text-sm font-medium text-slate-700">Deductions
              <input name="deductions" type="number" min="0" step="0.01" value={form.deductions} onChange={handleChange} className="mt-1 w-full rounded-lg border border-slate-300 px-3 py-2" />
            </label>
            <div className="flex items-end">
              <button type="submit" disabled={saving || departments.length === 0} className="rounded-lg bg-indigo-600 px-4 py-2.5 font-semibold text-white hover:bg-indigo-700 disabled:cursor-not-allowed disabled:opacity-60">
                {saving ? 'Adding...' : 'Add employee'}
              </button>
            </div>
          </form>
          {message && <p role="status" className="mt-4 text-sm text-emerald-700">{message}</p>}
          {error && <p role="alert" className="mt-4 text-sm text-red-700">{error}</p>}
        </section>

        <section className="mt-8 rounded-2xl bg-white p-6 shadow-sm ring-1 ring-slate-200">
          <h2 className="text-xl font-bold text-slate-900">Employees</h2>
          <div className="mt-4 overflow-x-auto">
            <table className="min-w-[1150px] w-full text-left text-sm">
              <thead className="border-b border-slate-200 text-slate-500">
                <tr>
                  <th className="px-3 py-3">ID</th>
                  <th className="px-3 py-3">Employee</th>
                  <th className="px-3 py-3">Designation</th>
                  <th className="px-3 py-3">Department</th>
                  <th className="px-3 py-3">Type</th>
                  <th className="px-3 py-3">Pay basis</th>
                  <th className="px-3 py-3">Overtime hours</th>
                  <th className="px-3 py-3">Allowances</th>
                  <th className="px-3 py-3">Bonus</th>
                  <th className="px-3 py-3">Deductions</th>
                  <th className="px-3 py-3">Action</th>
                  <th className="px-3 py-3">Availability</th>
                </tr>
              </thead>
              <tbody>
                {employees.map((employee) => (
                  <tr key={employee.employeeId} className="border-b border-slate-100">
                    <td className="px-3 py-3">{employee.employeeId}</td>
                    <td className="px-3 py-3 font-medium text-slate-900">{employee.name}</td>
                    <td className="px-3 py-3">{employee.designation}</td>
                    <td className="px-3 py-3">{employee.department?.departmentName}</td>
                    <td className="px-3 py-3">{employee.employeeType?.replace('_', ' ')}</td>
                    <td className="px-3 py-3">
                      {employee.employeeType === 'PART_TIME'
                        ? `${money(employee.hourlyRate)}/hr × ${employee.hoursWorked} hrs`
                        : employee.employeeType === 'CONTRACT'
                          ? money(employee.contractAmount)
                          : money(employee.basicSalary)}
                    </td>
                    <td className="px-3 py-3">{employee.overtimeHours || 0}</td>
                    <td className="px-3 py-3">{money(employee.allowances)}</td>
                    <td className="px-3 py-3">{money(employee.bonus)}</td>
                    <td className="px-3 py-3">{money(employee.deductions)}</td>
                    <td className="px-3 py-3">
                      <button onClick={() => handleRemoveEmployee(employee)} className="font-semibold text-red-700 hover:text-red-900">Remove</button>
                    </td>
                    <td className="px-3 py-3">
                      {availabilityByEmployee[employee.employeeId]?.status === 'AVAILABLE'
                        ? <span className="font-semibold text-emerald-700">Available</span>
                        : availabilityByEmployee[employee.employeeId]?.status === 'UNAVAILABLE'
                          ? <span className="font-semibold text-red-700">Unavailable</span>
                          : <span className="text-slate-500">Not set</span>}
                    </td>
                  </tr>
                ))}
                {employees.length === 0 && (
                  <tr><td colSpan="12" className="px-3 py-8 text-center text-slate-500">No employees found.</td></tr>
                )}
              </tbody>
            </table>
          </div>
        </section>
      </div>
    </div>
  );
}

export default AdminDashboard;
