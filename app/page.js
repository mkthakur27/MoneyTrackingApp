'use client';

import { useState } from 'react';
import EntriesTab from './components/EntriesTab';
import ReportsTab from './components/ReportsTab';
import BudgetsTab from './components/BudgetsTab';
import RecurringTab from './components/RecurringTab';
import ProfileTab from './components/ProfileTab';

const tabs = [
  { id: 'entries', label: 'Entries' },
  { id: 'reports', label: 'Reports' },
  { id: 'budgets', label: 'Budgets' },
  { id: 'recurring', label: 'Recurring' },
  { id: 'profile', label: 'Profile' },
];

export default function Home() {
  const [active, setActive] = useState('entries');

  return (
    <main className="page-container">
      <header className="app-header">
        <h1>Money Tracking App</h1>
        <p>Track spending, set budgets, and review your reports.</p>
      </header>

      <nav className="tab-bar">
        {tabs.map((tab) => (
          <button
            key={tab.id}
            type="button"
            className={`tab ${active === tab.id ? 'active' : ''}`}
            onClick={() => setActive(tab.id)}
          >
            {tab.label}
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
