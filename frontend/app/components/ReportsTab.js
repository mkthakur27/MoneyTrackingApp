'use client';

import useSWR from 'swr';
import { categoryLabel, fetcher, formatMoney, monthKey, monthLabel, useCurrencySymbol } from '../lib/api';

function BarRow({ label, value, max, symbol }) {
  const width = max > 0 ? Math.round((value / max) * 100) : 0;
  return (
    <div className="bar-row">
      <div className="bar-label">
        <span>{label}</span>
        <span className="amount">{formatMoney(symbol, value)}</span>
      </div>
      <div className="bar-track">
        <div className="bar-fill" style={{ width: `${width}%` }} />
      </div>
    </div>
  );
}

export default function ReportsTab() {
  const { data: entries = [], isLoading } = useSWR('/api/entries', fetcher, {
    refreshInterval: 3000,
  });
  const symbol = useCurrencySymbol();

  const byCategory = {};
  const byMonth = {};
  let total = 0;

  entries.forEach((entry) => {
    const amount = parseFloat(entry.amount || 0);
    total += amount;
    byCategory[entry.category] = (byCategory[entry.category] || 0) + amount;
    const key = monthKey(entry.date);
    byMonth[key] = (byMonth[key] || 0) + amount;
  });

  const categoryRows = Object.entries(byCategory).sort((a, b) => b[1] - a[1]);
  const monthRows = Object.entries(byMonth).sort((a, b) => (a[0] < b[0] ? 1 : -1));
  const maxCategory = Math.max(1, ...categoryRows.map((row) => row[1]));
  const maxMonth = Math.max(1, ...monthRows.map((row) => row[1]));

  if (isLoading) {
    return <div className="card"><p>Loading...</p></div>;
  }

  if (entries.length === 0) {
    return (
      <div className="card">
        <h2>📊 Reports</h2>
        <p className="muted">Add some entries to see reports.</p>
      </div>
    );
  }

  return (
    <div className="stack">
      <section className="card">
        <div className="section-header">
          <h2>📊 Spending by Category</h2>
          <span className="pill">Total: {formatMoney(symbol, total)}</span>
        </div>
        <div className="bars">
          {categoryRows.map(([category, value]) => (
            <BarRow
              key={category}
              label={categoryLabel(category)}
              value={value}
              max={maxCategory}
              symbol={symbol}
            />
          ))}
        </div>
      </section>

      <section className="card">
        <h2>📅 Spending by Month</h2>
        <div className="bars">
          {monthRows.map(([key, value]) => (
            <BarRow key={key} label={monthLabel(key)} value={value} max={maxMonth} symbol={symbol} />
          ))}
        </div>
      </section>
    </div>
  );
}
