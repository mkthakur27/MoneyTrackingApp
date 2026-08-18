'use client';

import useSWR from 'swr';
import { useState } from 'react';
import { apiRequest, fetcher, categories, formatMoney, today, useCurrencySymbol } from '../lib/api';

const emptyForm = () => ({
  description: '',
  category: categories[0],
  amount: '',
  period: 'MONTHLY',
  note: '',
});

export default function RecurringTab() {
  const { data: recurring = [], mutate } = useSWR('/api/recurring', fetcher, {
    refreshInterval: 3000,
  });
  const { mutate: mutateEntries } = useSWR('/api/entries', fetcher, { refreshInterval: 3000 });
  const symbol = useCurrencySymbol();

  const [form, setForm] = useState(emptyForm());
  const [error, setError] = useState('');
  const [message, setMessage] = useState('');

  const change = (field) => (event) => setForm({ ...form, [field]: event.target.value });

  const createRecurring = async () => {
    setError('');
    setMessage('');
    if (!form.description || !form.amount) {
      setError('Please enter description and amount.');
      return;
    }
    await apiRequest('/api/recurring', {
      method: 'POST',
      body: JSON.stringify({ ...form, amount: parseFloat(form.amount) }),
    });
    setForm(emptyForm());
    mutate();
  };

  const deleteRecurring = async (id) => {
    await apiRequest(`/api/recurring/${id}`, { method: 'DELETE' });
    mutate();
  };

  const logAsEntry = async (item) => {
    setMessage('');
    await apiRequest('/api/entries', {
      method: 'POST',
      body: JSON.stringify({
        description: item.description,
        category: item.category,
        amount: parseFloat(item.amount || 0),
        date: today(),
        note: item.note || '',
      }),
    });
    mutateEntries();
    setMessage(`Added "${item.description}" to entries.`);
  };

  return (
    <div className="stack">
      <section className="card">
        <h2>Add a Default / Recurring Expense</h2>
        <p className="muted">
          Set expenses that repeat every month or week, then log them to your entries with one click.
        </p>
        <div className="form-grid">
          <label>
            Description
            <input
              value={form.description}
              onChange={change('description')}
              placeholder="e.g. Rent, Subscription"
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
            Period
            <select value={form.period} onChange={change('period')}>
              <option value="MONTHLY">Monthly</option>
              <option value="WEEKLY">Weekly</option>
            </select>
          </label>
          <label className="full-width">
            Note (optional)
            <textarea
              value={form.note}
              onChange={change('note')}
              placeholder="Add a note"
              rows={2}
            />
          </label>
        </div>
        <button type="button" onClick={createRecurring}>
          Save Recurring Expense
        </button>
        {error && <p className="error">{error}</p>}
      </section>

      <section className="card">
        <h2>Recurring Expenses</h2>
        {message && <p className="success">{message}</p>}
        {recurring.length === 0 ? (
          <p className="muted">No recurring expenses yet.</p>
        ) : (
          <ul className="entry-list">
            {recurring.map((item) => (
              <li key={item.id} className="entry-item">
                <div className="entry-main">
                  <strong>{item.description}</strong>
                  <p className="muted">
                    {item.category} • {item.period.toLowerCase()}
                  </p>
                  {item.note && <p className="note">{item.note}</p>}
                </div>
                <div className="entry-right">
                  <span className="amount">{formatMoney(symbol, item.amount)}</span>
                  <button type="button" onClick={() => logAsEntry(item)}>
                    Log
                  </button>
                  <button className="ghost" onClick={() => deleteRecurring(item.id)}>
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
