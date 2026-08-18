'use client';

import useSWR from 'swr';
import { useEffect, useState } from 'react';
import { apiRequest, fetcher, currencyPresets } from '../lib/api';

export default function ProfileTab() {
  const { data: profile, mutate } = useSWR('/api/profile', fetcher);
  const [form, setForm] = useState({ currencyCode: 'USD', currencySymbol: '$' });
  const [message, setMessage] = useState('');

  useEffect(() => {
    if (profile) {
      setForm({
        currencyCode: profile.currencyCode || 'USD',
        currencySymbol: profile.currencySymbol || '$',
      });
    }
  }, [profile]);

  const applyPreset = (code) => {
    const preset = currencyPresets.find((item) => item.code === code);
    if (preset) {
      setForm({ currencyCode: preset.code, currencySymbol: preset.symbol });
    } else {
      setForm({ ...form, currencyCode: code });
    }
  };

  const save = async () => {
    setMessage('');
    await apiRequest('/api/profile', {
      method: 'PUT',
      body: JSON.stringify(form),
    });
    mutate();
    setMessage('Profile saved.');
  };

  return (
    <div className="stack">
      <section className="card">
        <h2>⚙️ Profile Settings</h2>
        <p className="muted">Set your currency denomination. It is used across the whole app.</p>
        <div className="form-grid">
          <label>
            Currency
            <select value={form.currencyCode} onChange={(event) => applyPreset(event.target.value)}>
              {currencyPresets.map((preset) => (
                <option key={preset.code} value={preset.code}>
                  {preset.code} ({preset.symbol})
                </option>
              ))}
              {!currencyPresets.some((preset) => preset.code === form.currencyCode) && (
                <option value={form.currencyCode}>{form.currencyCode}</option>
              )}
            </select>
          </label>
          <label>
            Currency Code
            <input
              value={form.currencyCode}
              onChange={(event) => setForm({ ...form, currencyCode: event.target.value })}
              placeholder="USD"
            />
          </label>
          <label>
            Currency Symbol
            <input
              value={form.currencySymbol}
              onChange={(event) => setForm({ ...form, currencySymbol: event.target.value })}
              placeholder="$"
            />
          </label>
        </div>
        <button type="button" onClick={save}>
          Save Profile
        </button>
        {message && <p className="success">{message}</p>}
      </section>

      <section className="card">
        <h2>👀 Currency Preview</h2>
        <p>
          Amounts will look like:{' '}
          <strong>
            {form.currencySymbol}
            1,250.00
          </strong>{' '}
          ({form.currencyCode})
        </p>
      </section>
    </div>
  );
}
