import { formatMoney, isValidEmail, monthKey, monthLabel } from '../app/lib/api';

describe('api helpers', () => {
  test('formatMoney uses the currency symbol and two decimals', () => {
    expect(formatMoney('$', 12.5)).toBe('$12.50');
    expect(formatMoney('₹', 0)).toBe('₹0.00');
  });

  test('isValidEmail accepts a normal address and rejects junk', () => {
    expect(isValidEmail('you@example.com')).toBe(true);
    expect(isValidEmail('not-an-email')).toBe(false);
    expect(isValidEmail('')).toBe(false);
  });

  test('month helpers parse a date string', () => {
    expect(monthKey('2026-08-17')).toBe('2026-08');
    expect(monthLabel('2026-08')).toMatch(/August 2026/);
  });
});
