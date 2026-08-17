'use client';

import useSWR from 'swr';
import { useState } from 'react';
import {
  fetcher,
  categories,
  categoryLabel,
  formatMoney,
  isInCurrentMonth,
  isInCurrentWeek,
  useCurrencySymbol,
} from '../lib/api';

const emptyForm = () => ({ category: categories[0], amount: '', period: 'MONTHLY' });

export default function BudgetsTab() {
  const { data: budgets = [], mutate } = useSWR('/api/budgets', fetcher, { refreshInterval: 3000 });
  const { data: entries = [] } = useSWR('/api/entries', fetcher, { refreshInterval: 3000 });
  const symbol = useCurrencySymbol();

  const [form, setForm] = useState(emptyForm());
  const [error, setError] = useState('');

  const change = (field) => (event) => setForm({ ...form, [field]: event.target.value });

  const createBudget = async () => {
    setError('');
    if (!form.amount) {
      setError('Please enter a budget amount.');
      return;
    }
    await fetch('/api/budgets', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ ...form, amount: parseFloat(form.amount) }),
    });
    setForm(emptyForm());
    mutate();
  };

  const deleteBudget = async (id) => {
    await fetch(`/api/budgets/${id}`, { method: 'DELETE' });
    mutate();
  };

  const spentFor = (category, period) => {
    const filter = period === 'WEEKLY' ? isInCurrentWeek : isInCurrentMonth;
    return entries
      .filter((entry) => entry.category === category && filter(entry.date))
      .reduce((sum, entry) => sum + parseFloat(entry.amount || 0), 0);
  };

  return (
    <div className="stack">
      <section className="card">
        <h2>🎯 Set a Category Budget</h2>
        <div className="form-grid">
          <label>
            Category
            <select value={form.category} onChange={change('category')}>
              {categories.map((category) => (
                <option key={category} value={category}>
                  {categoryLabel(category)}
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
        </div>
        <button type="button" onClick={createBudget}>
          Save Budget
        </button>
        {error && <p className="error">{error}</p>}
      </section>

      <section className="card">
        <h2>📌 Your Budgets</h2>
        {budgets.length === 0 ? (
          <p className="muted">No budgets set yet.</p>
        ) : (
          <div className="bars">
            {budgets.map((budget) => {
              const spent = spentFor(budget.category, budget.period);
              const limit = parseFloat(budget.amount || 0);
              const pct = limit > 0 ? Math.min(100, Math.round((spent / limit) * 100)) : 0;
              const over = spent > limit;
              return (
                <div key={budget.id} className="bar-row">
                  <div className="bar-label">
                    <span>
                      {categoryLabel(budget.category)}{' '}
                      <span className="muted">({budget.period.toLowerCase()})</span>
                    </span>
                    <span className={over ? 'amount over' : 'amount'}>
                      {formatMoney(symbol, spent)} / {formatMoney(symbol, limit)}
                    </span>
                  </div>
                  <div className="bar-track">
                    <div
                      className={`bar-fill ${over ? 'over' : ''}`}
                      style={{ width: `${pct}%` }}
                    />
                  </div>
                  <div className="row-actions">
                    <span className="muted">
                      {over
                        ? `Over by ${formatMoney(symbol, spent - limit)}`
                        : `${formatMoney(symbol, limit - spent)} left`}
                    </span>
                    <button className="danger compact" onClick={() => deleteBudget(budget.id)}>
                      🗑️ Delete
                    </button>
                  </div>
                </div>
              );
            })}
          </div>
        )}
      </section>
    </div>
  );
}
