'use client';

import { useState } from 'react';
import { useRouter } from 'next/navigation';
import { isValidEmail, setToken } from '../lib/api';

export default function LoginPage() {
  const router = useRouter();
  const [mode, setMode] = useState('login');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [busy, setBusy] = useState(false);

  const submit = async (event) => {
    event.preventDefault();
    setError('');

    if (!isValidEmail(email)) {
      setError('Please enter a valid email address.');
      return;
    }
    if (!password || password.length < 8) {
      setError('Password must be at least 8 characters.');
      return;
    }

    setBusy(true);
    try {
      const response = await fetch(`/api/auth/${mode === 'signup' ? 'signup' : 'login'}`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email, password }),
      });
      const data = await response.json().catch(() => ({}));
      if (!response.ok) {
        setError(data.message || 'Unable to continue. Please try again.');
        return;
      }
      setToken(data.token);
      router.replace('/');
    } catch {
      setError('Unable to continue. Please try again.');
    } finally {
      setBusy(false);
    }
  };

  return (
    <main className="page-container">
      <section className="card login-card">
        <h1>Money Tracking App</h1>
        <p className="muted">
          {mode === 'login' ? 'Log in with your email and password.' : 'Create an account with your email and password.'}
        </p>
        <form className="stack" onSubmit={submit} noValidate>
          <label>
            Email
            <input
              type="email"
              value={email}
              onChange={(event) => setEmail(event.target.value)}
              placeholder="you@example.com"
              autoComplete="email"
            />
          </label>
          <label>
            Password
            <input
              type="password"
              value={password}
              onChange={(event) => setPassword(event.target.value)}
              placeholder="At least 8 characters"
              autoComplete={mode === 'login' ? 'current-password' : 'new-password'}
            />
          </label>
          <button type="submit" disabled={busy}>
            {busy ? 'Please wait...' : mode === 'login' ? 'Log in' : 'Sign up'}
          </button>
          {error && <p className="error">{error}</p>}
        </form>
        <p className="muted login-switch">
          {mode === 'login' ? 'Need an account?' : 'Already have an account?'}{' '}
          <button
            type="button"
            className="link"
            onClick={() => {
              setMode(mode === 'login' ? 'signup' : 'login');
              setError('');
            }}
          >
            {mode === 'login' ? 'Sign up' : 'Log in'}
          </button>
        </p>
      </section>
    </main>
  );
}
