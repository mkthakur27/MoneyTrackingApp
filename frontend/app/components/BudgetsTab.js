'use client';

import useSWR from 'swr';
import { useMemo, useState } from 'react';
import {
  apiRequest,
  budgetMonthOptions,
  fetcher,
  categories,
  categoryLabel,
  formatMoney,
  isInMonth,
  monthKey,
  monthLabel,
  today,
  useCurrencySymbol,
} from '../lib/api';

const emptyForm = () => ({ category: categories[0], amount: '', period: 'MONTHLY' });

export default function BudgetsTab() {
  const { data: budgets = [], mutate } = useSWR('/api/budgets', fetcher, { refreshInterval: 3000 });
  const { data: entries = [] } = useSWR('/api/entries', fetcher, { refreshInterval: 3000 });
  const symbol = useCurrencySymbol();

  const [form, setForm] = useState(emptyForm());
  const [error, setError] = useState('');
  const [selectedMonth, setSelectedMonth] = useState(() => monthKey(today()));

  const monthOptions = useMemo(
    () => budgetMonthOptions(entries, [selectedMonth]),
    [entries, selectedMonth]
  );

  const change = (field) => (event) => setForm({ ...form, [field]: event.target.value });

  const createBudget = async () => {
    setError('');
    if (!form.amount) {
      setError('Please enter a budget amount.');
      return;
    }
    await apiRequest('/api/budgets', {
      method: 'POST',
      body: JSON.stringify({ ...form, amount: parseFloat(form.amount) }),
    });
    setForm(emptyForm());
    mutate();
  };

  const deleteBudget = async (id) => {
    await apiRequest(`/api/budgets/${id}`, { method: 'DELETE' });
    mutate();
  };

  const spentFor = (category) =>
    entries
      .filter((entry) => entry.category === category && isInMonth(entry.date, selectedMonth))
      .reduce((sum, entry) => sum + parseFloat(entry.amount || 0), 0);

  const monthSpendTotal = entries
    .filter((entry) => isInMonth(entry.date, selectedMonth))
    .reduce((sum, entry) => sum + parseFloat(entry.amount || 0), 0);

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
        <div className="section-header">
          <h2>📌 Your Budgets</h2>
          <label className="month-picker">
            Month
            <select
              value={selectedMonth}
              onChange={(event) => setSelectedMonth(event.target.value)}
              aria-label="Budget month"
            >
              {monthOptions.map((key) => (
                <option key={key} value={key}>
                  {monthLabel(key)}
                </option>
              ))}
            </select>
          </label>
        </div>
        {budgets.length === 0 ? (
          <p className="muted">No budgets set yet.</p>
        ) : (
          <>
            <p className="muted month-spend-summary">
              Showing {monthLabel(selectedMonth)}
              {monthSpendTotal > 0
                ? ` · spent ${formatMoney(symbol, monthSpendTotal)} across categories`
                : ' · no expenses recorded this month'}
            </p>
            <div className="bars">
              {budgets.map((budget) => {
                const spent = spentFor(budget.category);
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
          </>
        )}
      </section>
    </div>
  );
}
