'use client';

import useSWR from 'swr';
import { useState } from 'react';
import { fetcher, categories, formatMoney, today, useCurrencySymbol } from '../lib/api';

const emptyForm = () => ({
  description: '',
  category: categories[0],
  amount: '',
  date: today(),
  note: '',
});

export default function EntriesTab() {
  const { data: entries = [], mutate, isLoading } = useSWR('/api/entries', fetcher, {
    refreshInterval: 3000,
  });
  const symbol = useCurrencySymbol();

  const [form, setForm] = useState(emptyForm());
  const [error, setError] = useState('');

  const change = (field) => (event) => setForm({ ...form, [field]: event.target.value });

  const createEntry = async () => {
    setError('');
    if (!form.description || !form.amount) {
      setError('Please enter description and amount.');
      return;
    }

    const payload = {
      ...form,
      amount: parseFloat(form.amount),
      date: form.date || today(),
    };

    await fetch('/api/entries', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload),
    });

    setForm(emptyForm());
    mutate();
  };

  const deleteEntry = async (id) => {
    await fetch(`/api/entries/${id}`, { method: 'DELETE' });
    mutate();
  };

  const total = entries.reduce((sum, entry) => sum + parseFloat(entry.amount || 0), 0);
  const sorted = [...entries].sort((a, b) => (a.date < b.date ? 1 : a.date > b.date ? -1 : 0));

  return (
    <div className="stack">
      <section className="card">
        <h2>Add a Spend Entry</h2>
        <div className="form-grid">
          <label>
            Description
            <input
              value={form.description}
              onChange={change('description')}
              placeholder="Enter what you spent"
            />
          </label>
          <label>
            Category
            <select value={form.category} onChange={change('category')}>
              {categories.map((category) => (
                <option key={category} value={category}>
                  {category}
                </option>
              ))}
            </select>
          </label>
          <label>
            Amount
            <input
              type="number"
              step="0.01"
              value={form.amount}
              onChange={change('amount')}
              placeholder="0.00"
            />
          </label>
          <label>
            Date
            <input type="date" value={form.date} onChange={change('date')} />
          </label>
          <label className="full-width">
            Note (optional)
            <textarea
              value={form.note}
              onChange={change('note')}
              placeholder="Add a note about this expense"
              rows={2}
            />
          </label>
        </div>
        <button type="button" onClick={createEntry}>
          Add Spend
        </button>
        {error && <p className="error">{error}</p>}
      </section>

      <section className="card">
        <div className="section-header">
          <h2>Spend History</h2>
          <span className="pill">Total: {formatMoney(symbol, total)}</span>
        </div>
        {isLoading ? (
          <p>Loading...</p>
        ) : sorted.length === 0 ? (
          <p className="muted">No spend entries yet.</p>
        ) : (
          <ul className="entry-list">
            {sorted.map((entry) => (
              <li key={entry.id} className="entry-item">
                <div className="entry-main">
                  <strong>{entry.description}</strong>
                  <p className="muted">
                    {entry.category} • {entry.date}
                  </p>
                  {entry.note && <p className="note">{entry.note}</p>}
                </div>
                <div className="entry-right">
                  <span className="amount">{formatMoney(symbol, entry.amount)}</span>
                  <button className="ghost" onClick={() => deleteEntry(entry.id)}>
                    Delete
                  </button>
                </div>
              </li>
            ))}
          </ul>
        )}
      </section>
    </div>
  );
}
