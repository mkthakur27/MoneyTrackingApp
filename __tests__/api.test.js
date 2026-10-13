import {
  budgetMonthOptions,
  categoryColor,
  formatMoney,
  isInMonth,
  isValidEmail,
  monthKey,
  monthLabel,
} from '../app/lib/api';

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

  test('categoryColor returns a stable color for known categories', () => {
    expect(categoryColor('Food')).toBe('#f97316');
    expect(categoryColor('Unknown')).toBe('#0ea5e9');
  });

  test('isInMonth matches a YYYY-MM key', () => {
    expect(isInMonth('2026-08-17', '2026-08')).toBe(true);
    expect(isInMonth('2026-08-17', '2026-07')).toBe(false);
    expect(isInMonth('', '2026-08')).toBe(false);
  });

  test('budgetMonthOptions includes entry months and the current month', () => {
    const options = budgetMonthOptions(
      [{ date: '2026-07-04' }, { date: '2026-08-12' }],
      ['2026-06']
    );
    expect(options[0]).toMatch(/^\d{4}-\d{2}$/);
    expect(options).toEqual([...options].sort((a, b) => (a < b ? 1 : -1)));
    expect(options).toEqual(expect.arrayContaining(['2026-07', '2026-08', '2026-06']));
  });
});
