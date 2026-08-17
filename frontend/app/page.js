'use client';

import { useState } from 'react';
import EntriesTab from './components/EntriesTab';
import ReportsTab from './components/ReportsTab';
import BudgetsTab from './components/BudgetsTab';
import RecurringTab from './components/RecurringTab';
import ProfileTab from './components/ProfileTab';

const tabs = [
  { id: 'entries', icon: '🧾', label: 'Expenses' },
  { id: 'reports', icon: '📊', label: 'Reports' },
  { id: 'budgets', icon: '🎯', label: 'Budgets' },
  { id: 'recurring', icon: '🔁', label: 'Recurring' },
  { id: 'profile', icon: '⚙️', label: 'Profile' },
];

export default function Home() {
  const [active, setActive] = useState('entries');

  return (
    <main className="page-container">
      <header className="app-header">
        <div className="brand-mark" aria-hidden="true">💰</div>
        <div>
          <p className="eyebrow">Your personal finance space</p>
          <h1>Money Tracking App</h1>
          <p>Track spending, set budgets, and understand where your money goes.</p>
        </div>
      </header>

      <nav className="tab-bar" aria-label="Main navigation">
        {tabs.map((tab) => (
          <button
            key={tab.id}
            type="button"
            className={`tab ${active === tab.id ? 'active' : ''}`}
            onClick={() => setActive(tab.id)}
            aria-current={active === tab.id ? 'page' : undefined}
          >
            <span aria-hidden="true">{tab.icon}</span>
            <span>{tab.label}</span>
          </button>
        ))}
      </nav>

      <section className="tab-content">
        {active === 'entries' && <EntriesTab />}
        {active === 'reports' && <ReportsTab />}
        {active === 'budgets' && <BudgetsTab />}
        {active === 'recurring' && <RecurringTab />}
        {active === 'profile' && <ProfileTab />}
      </section>
    </main>
  );
}
