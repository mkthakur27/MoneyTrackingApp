'use client';

import { useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';
import EntriesTab from './components/EntriesTab';
import ReportsTab from './components/ReportsTab';
import BudgetsTab from './components/BudgetsTab';
import RecurringTab from './components/RecurringTab';
import ProfileTab from './components/ProfileTab';
import { clearToken, getToken } from './lib/api';

const tabs = [
  { id: 'entries', label: 'Entries' },
  { id: 'reports', label: 'Reports' },
  { id: 'budgets', label: 'Budgets' },
  { id: 'recurring', label: 'Recurring' },
  { id: 'profile', label: 'Profile' },
];

export default function Home() {
  const router = useRouter();
  const [active, setActive] = useState('entries');
  const [ready, setReady] = useState(false);

  useEffect(() => {
    if (!getToken()) {
      router.replace('/login');
      return;
    }
    setReady(true);
  }, [router]);

  const logout = () => {
    clearToken();
    router.replace('/login');
  };

  if (!ready) {
    return null;
  }

  return (
    <main className="page-container">
      <header className="app-header">
        <div className="header-row">
          <div>
            <h1>Money Tracking App</h1>
            <p>Track spending, set budgets, and review your reports.</p>
          </div>
          <button type="button" className="ghost" onClick={logout}>
            Log out
          </button>
        </div>
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
