'use client';

import useSWR from 'swr';
import { useState } from 'react';
import {
  apiRequest,
  categoryLabel,
  fetcher,
  categories,
  formatMoney,
  today,
  useCurrencySymbol,
} from '../lib/api';

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
  const [message, setMessage] = useState('');
  const [editingId, setEditingId] = useState(null);
  const [busy, setBusy] = useState(false);

  const change = (field) => (event) => setForm({ ...form, [field]: event.target.value });

  const resetForm = () => {
    setForm(emptyForm());
    setEditingId(null);
  };

  const startEdit = (entry) => {
    setEditingId(entry.id);
    setForm({
      description: entry.description || '',
      category: entry.category || categories[0],
      amount: String(entry.amount ?? ''),
      date: entry.date || today(),
      note: entry.note || '',
    });
    setError('');
    setMessage('');
    window.scrollTo({ top: 0, behavior: 'smooth' });
  };

  const saveEntry = async () => {
    setError('');
    setMessage('');
    const amount = Number(form.amount);

    if (!form.description.trim()) {
      setError('Please enter a description.');
      return;
    }
    if (!Number.isFinite(amount) || amount <= 0) {
      setError('Amount must be greater than zero.');
      return;
    }
    if (!form.date) {
      setError('Please select an expense date.');
      return;
    }

    const payload = {
      ...form,
      description: form.description.trim(),
      amount,
      date: form.date,
      note: form.note.trim(),
    };

    setBusy(true);
    try {
      await apiRequest(editingId ? `/api/entries/${editingId}` : '/api/entries', {
        method: editingId ? 'PUT' : 'POST',
        body: JSON.stringify(payload),
      });
      setMessage(editingId ? 'Expense updated successfully.' : 'Expense added successfully.');
      resetForm();
      await mutate();
    } catch (requestError) {
      setError(requestError.message);
    } finally {
      setBusy(false);
    }
  };

  const deleteEntry = async (entry) => {
    if (!window.confirm(`Delete "${entry.description}"? This cannot be undone.`)) {
      return;
    }

    setError('');
    setMessage('');
    setBusy(true);
    try {
      await apiRequest(`/api/entries/${entry.id}`, { method: 'DELETE' });
      if (editingId === entry.id) {
        resetForm();
      }
      setMessage('Expense deleted.');
      await mutate();
    } catch (requestError) {
      setError(requestError.message);
    } finally {
      setBusy(false);
    }
  };

  const total = entries.reduce((sum, entry) => sum + parseFloat(entry.amount || 0), 0);
  const sorted = [...entries].sort((a, b) => (a.date < b.date ? 1 : a.date > b.date ? -1 : 0));

  return (
    <div className="stack">
      <section className={`card ${editingId ? 'editing-card' : ''}`}>
        <div className="section-header">
          <div>
            <h2>{editingId ? '✏️ Edit Expense' : '➕ Add an Expense'}</h2>
            <p className="muted">
              {editingId ? 'Update the details and save your changes.' : 'Record a new expense in a few seconds.'}
            </p>
          </div>
          {editingId && <span className="pill">Editing</span>}
        </div>
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
        <div className="form-actions">
          <button type="button" onClick={saveEntry} disabled={busy}>
            {busy ? 'Saving…' : editingId ? 'Save Changes' : 'Add Expense'}
          </button>
          {editingId && (
            <button type="button" className="secondary" onClick={resetForm} disabled={busy}>
              Cancel
            </button>
          )}
        </div>
        {error && <p className="error">{error}</p>}
        {message && <p className="success">{message}</p>}
      </section>

      <section className="card">
        <div className="section-header">
          <h2>📋 Expense History</h2>
          <span className="pill">Total: {formatMoney(symbol, total)}</span>
        </div>
        {isLoading ? (
          <p>Loading...</p>
        ) : sorted.length === 0 ? (
          <p className="muted">No spend entries yet.</p>
        ) : (
          <ul className="entry-list">
            {sorted.map((entry) => (
              <li key={entry.id} className={`entry-item ${editingId === entry.id ? 'editing' : ''}`}>
                <div className="entry-main">
                  <strong>{entry.description}</strong>
                  <p className="muted">
                    <span className="category-badge">{categoryLabel(entry.category)}</span>
                    <span> • {entry.date}</span>
                  </p>
                  {entry.note && <p className="note">{entry.note}</p>}
                </div>
                <div className="entry-right">
                  <span className="amount">{formatMoney(symbol, entry.amount)}</span>
                  <button className="secondary compact" onClick={() => startEdit(entry)} disabled={busy}>
                    ✏️ Edit
                  </button>
                  <button className="danger compact" onClick={() => deleteEntry(entry)} disabled={busy}>
                    🗑️ Delete
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
