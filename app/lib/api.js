import useSWR from 'swr';

export const fetcher = (url) =>
  fetch(url).then((res) => {
    if (!res.ok) {
      throw new Error(`Request failed: ${res.status}`);
    }
    return res.json();
  });

export const categories = ['Food', 'Transport', 'Utilities', 'Shopping', 'Health', 'Other'];

export const currencyPresets = [
  { code: 'USD', symbol: '$' },
  { code: 'EUR', symbol: '€' },
  { code: 'GBP', symbol: '£' },
  { code: 'INR', symbol: '₹' },
  { code: 'JPY', symbol: '¥' },
  { code: 'AUD', symbol: 'A$' },
  { code: 'CAD', symbol: 'C$' },
];

export function useCurrencySymbol() {
  const { data } = useSWR('/api/profile', fetcher);
  return data?.currencySymbol || '$';
}

export function formatMoney(symbol, amount) {
  const value = Number(amount || 0);
  return `${symbol}${value.toFixed(2)}`;
}

export function today() {
  return new Date().toISOString().slice(0, 10);
}

export function monthKey(dateStr) {
  return (dateStr || '').slice(0, 7);
}

export function monthLabel(key) {
  if (!key) return 'Unknown';
  const [year, month] = key.split('-');
  const date = new Date(Number(year), Number(month) - 1, 1);
  return date.toLocaleString('default', { month: 'long', year: 'numeric' });
}

export function startOfWeek(date = new Date()) {
  const d = new Date(date);
  const day = (d.getDay() + 6) % 7; // Monday = 0
  d.setHours(0, 0, 0, 0);
  d.setDate(d.getDate() - day);
  return d;
}

export function isInCurrentMonth(dateStr) {
  return monthKey(dateStr) === monthKey(today());
}

export function isInCurrentWeek(dateStr) {
  if (!dateStr) return false;
  const entryDate = new Date(`${dateStr}T00:00:00`);
  const weekStart = startOfWeek();
  const weekEnd = new Date(weekStart);
  weekEnd.setDate(weekEnd.getDate() + 7);
  return entryDate >= weekStart && entryDate < weekEnd;
}
