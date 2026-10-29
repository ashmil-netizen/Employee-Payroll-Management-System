import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { loginUser } from '../services/api';

function Login() {
  const navigate = useNavigate();
  const [name, setName] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const fillDemoAccount = (accountName, accountPassword) => {
    setName(accountName);
    setPassword(accountPassword);
    setError('');
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    setLoading(true);
    setError('');

    try {
      const result = await loginUser(name, password);
      localStorage.setItem('payrollUser', JSON.stringify(result));

      if (result.role === 'ADMIN') {
        navigate('/admin');
      } else {
        navigate('/employee', { state: { employee: result } });
      }
    } catch (err) {
      setError(err.message || 'Login failed');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="flex min-h-screen items-center justify-center bg-slate-100 px-4">
      <div className="w-full max-w-md rounded-2xl bg-white p-8 shadow-lg ring-1 ring-slate-200">
        <div className="mb-6 text-center">
          <div className="mx-auto mb-4 flex h-12 w-12 items-center justify-center rounded-2xl bg-indigo-600 text-lg font-black text-white shadow-lg shadow-indigo-950/40">P</div>
          <p className="text-sm font-semibold uppercase tracking-[0.2em] text-indigo-600">People · Pay · Presence</p>
          <h1 className="mt-2 text-3xl font-bold text-slate-900">Login</h1>
          <p className="mt-2 text-sm text-slate-500">Your workforce, in focus.</p>
        </div>

        <form onSubmit={handleSubmit} className="space-y-5">
          <div>
            <label className="mb-2 block text-sm font-medium text-slate-700">Name</label>
            <input
              type="text"
              value={name}
              onChange={(e) => setName(e.target.value)}
              className="w-full rounded-xl border border-slate-300 bg-slate-50 px-3 py-2.5 outline-none ring-0 transition focus:border-indigo-500 focus:bg-white"
              placeholder="Enter admin or employee name"
              required
            />
          </div>

          <div>
            <label className="mb-2 block text-sm font-medium text-slate-700">Password</label>
            <input
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              className="w-full rounded-xl border border-slate-300 bg-slate-50 px-3 py-2.5 outline-none ring-0 transition focus:border-indigo-500 focus:bg-white"
              placeholder="Enter your password"
              required
            />
          </div>

          {error && (
            <div className="rounded-xl border border-red-200 bg-red-50 px-3 py-2 text-sm text-red-700">
              {error}
            </div>
          )}

          <button
            type="submit"
            disabled={loading}
            className="w-full rounded-xl bg-indigo-600 px-4 py-3 font-semibold text-white transition hover:bg-indigo-700 disabled:cursor-not-allowed disabled:bg-indigo-400"
          >
            {loading ? 'Logging in...' : 'Login'}
          </button>
        </form>

        <div className="mt-6 rounded-xl bg-slate-50 p-4 text-sm text-slate-600">
          <p className="font-semibold text-slate-800">Try a demo account</p>
          <div className="mt-3 flex flex-wrap gap-2">
            <button
              type="button"
              onClick={() => fillDemoAccount('admin', 'admin123')}
              className="rounded-lg border border-slate-300 bg-white px-3 py-2 font-medium hover:border-indigo-500"
            >
              Admin
            </button>
            <button
              type="button"
              onClick={() => fillDemoAccount('Ashmil', 'ashmil123')}
              className="rounded-lg border border-slate-300 bg-white px-3 py-2 font-medium hover:border-indigo-500"
            >
              Employee: Ashmil
            </button>
            <button
              type="button"
              onClick={() => fillDemoAccount('Rinosh', 'rinosh123')}
              className="rounded-lg border border-slate-300 bg-white px-3 py-2 font-medium hover:border-indigo-500"
            >
              Employee: Rinosh
            </button>
          </div>
          <p className="mt-3 text-xs">Admin: admin / admin123. Employees: Ashmil / ashmil123, Rinosh / rinosh123.</p>
        </div>
      </div>
    </div>
  );
}

export default Login;
