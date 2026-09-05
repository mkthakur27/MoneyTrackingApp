import { fireEvent, render, screen } from '@testing-library/react';
import BudgetsTab from '../app/components/BudgetsTab';

jest.mock('swr', () => ({
  __esModule: true,
  default: (url) => {
    if (url === '/api/profile') {
      return { data: { currencySymbol: '₹' } };
    }
    if (url === '/api/budgets') {
      return {
        data: [{ id: 1, category: 'Food', amount: 1000, period: 'MONTHLY' }],
        mutate: jest.fn(),
      };
    }
    return {
      data: [
        { id: 1, category: 'Food', amount: 100, date: '2026-08-10' },
        { id: 2, category: 'Food', amount: 50, date: '2026-07-05' },
      ],
    };
  },
}));

describe('BudgetsTab', () => {
  test('shows spending for the selected month only', () => {
    render(<BudgetsTab />);

    const monthSelect = screen.getByLabelText('Budget month');
    fireEvent.change(monthSelect, { target: { value: '2026-08' } });

    expect(screen.getByText(/Showing August 2026/)).toBeInTheDocument();
    expect(screen.getByText('₹100.00 / ₹1000.00')).toBeInTheDocument();

    fireEvent.change(monthSelect, { target: { value: '2026-07' } });
    expect(screen.getByText(/Showing July 2026/)).toBeInTheDocument();
    expect(screen.getByText('₹50.00 / ₹1000.00')).toBeInTheDocument();
  });
});
