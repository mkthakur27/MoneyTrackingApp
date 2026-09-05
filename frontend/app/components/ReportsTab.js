'use client';

import useSWR from 'swr';
import { useMemo, useState } from 'react';
import {
  budgetMonthOptions,
  categoryColor,
  categoryLabel,
  fetcher,
  formatMoney,
  monthKey,
  monthLabel,
  today,
  useCurrencySymbol,
} from '../lib/api';

function buildMonthReports(entries) {
  const months = {};

  entries.forEach((entry) => {
    const key = monthKey(entry.date) || 'unknown';
    const amount = Number(entry.amount || 0);
    const category = entry.category || 'Other';

    if (!months[key]) {
      months[key] = { total: 0, count: 0, byCategory: {} };
    }

    months[key].total += amount;
    months[key].count += 1;
    months[key].byCategory[category] = (months[key].byCategory[category] || 0) + amount;
  });

  return Object.entries(months)
    .sort((a, b) => (a[0] < b[0] ? 1 : -1))
    .map(([key, data]) => ({
      key,
      label: monthLabel(key),
      total: data.total,
      count: data.count,
      slices: Object.entries(data.byCategory)
        .sort((a, b) => b[1] - a[1])
        .map(([category, value]) => ({
          category,
          value,
          percent: data.total > 0 ? (value / data.total) * 100 : 0,
        })),
    }));
}

function CategoryPie({ slices, label }) {
  let offset = 0;
  const drawn = slices.map((slice) => {
    const start = offset;
    offset += slice.percent;
    return { ...slice, start };
  });

  return (
    <svg className="pie-svg" viewBox="0 0 36 36" role="img" aria-label={`${label} spending by category`}>
      <circle cx="18" cy="18" r="15.5" fill="none" stroke="#e2e8f0" strokeWidth="5" />
      {drawn.map((slice) => (
        <circle
          key={slice.category}
          cx="18"
          cy="18"
          r="15.5"
          fill="none"
          stroke={categoryColor(slice.category)}
          strokeWidth="5"
          pathLength="100"
          strokeDasharray={`${slice.percent} ${100 - slice.percent}`}
          strokeDashoffset={-slice.start}
          transform="rotate(-90 18 18)"
        />
      ))}
    </svg>
  );
}

function MonthReportBody({ report, symbol }) {
  return (
    <>
      <p className="muted month-spend-summary">
        {report.count} {report.count === 1 ? 'entry' : 'entries'} · {formatMoney(symbol, report.total)}
      </p>
      <div className="month-report">
        <div className="pie-wrap">
          <CategoryPie slices={report.slices} label={report.label} />
          <div className="pie-center">
            <span className="pie-center-label">Total</span>
            <span className="pie-center-value">{formatMoney(symbol, report.total)}</span>
          </div>
        </div>

        <ul className="pie-legend">
          {report.slices.map((slice) => (
            <li key={slice.category}>
              <span className="pie-legend-label">
                <span className="swatch" style={{ background: categoryColor(slice.category) }} />
                {categoryLabel(slice.category)}
              </span>
              <span className="pie-legend-values">
                <span className="muted">{slice.percent.toFixed(1)}%</span>
                <span className="amount">{formatMoney(symbol, slice.value)}</span>
              </span>
            </li>
          ))}
        </ul>
      </div>
    </>
  );
}

export default function ReportsTab() {
  const { data: entries = [], isLoading } = useSWR('/api/entries', fetcher, {
    refreshInterval: 3000,
  });
  const symbol = useCurrencySymbol();
  const [selectedMonth, setSelectedMonth] = useState(() => monthKey(today()));
  const monthOptions = useMemo(
    () => budgetMonthOptions(entries, [selectedMonth]),
    [entries, selectedMonth]
  );
  const monthReports = buildMonthReports(entries);
  const report = monthReports.find((item) => item.key === selectedMonth);

  if (isLoading) {
    return (
      <div className="card">
        <p>Loading...</p>
      </div>
    );
  }

  if (entries.length === 0) {
    return (
      <div className="card">
        <h2>📊 Reports</h2>
        <p className="muted">Add some entries to see monthly reports.</p>
      </div>
    );
  }

  return (
    <section className="card month-report-card">
      <div className="section-header">
        <h2>📊 Reports</h2>
        <label className="month-picker">
          Month
          <select
            value={selectedMonth}
            onChange={(event) => setSelectedMonth(event.target.value)}
            aria-label="Report month"
          >
            {monthOptions.map((key) => (
              <option key={key} value={key}>
                {monthLabel(key)}
              </option>
            ))}
          </select>
        </label>
      </div>
      {report ? (
        <MonthReportBody report={report} symbol={symbol} />
      ) : (
        <p className="muted">No expenses recorded in {monthLabel(selectedMonth)}.</p>
      )}
    </section>
  );
}
