import { fireEvent, render, screen } from '@testing-library/react';
import ReportsTab from '../app/components/ReportsTab';

jest.mock('swr', () => ({
  __esModule: true,
  default: (url) => {
    if (url === '/api/profile') {
      return { data: { currencySymbol: '₹' } };
    }
    return {
      data: [
        { id: 1, category: 'Food', amount: 100, date: '2026-08-10' },
        { id: 2, category: 'Transport', amount: 50, date: '2026-08-12' },
        { id: 3, category: 'Food', amount: 20, date: '2026-07-05' },
      ],
      isLoading: false,
    };
  },
}));

describe('ReportsTab', () => {
  test('shows a pie report for the selected month only', () => {
    render(<ReportsTab />);

    const monthSelect = screen.getByLabelText('Report month');
    fireEvent.change(monthSelect, { target: { value: '2026-08' } });

    expect(screen.getByLabelText('August 2026 spending by category')).toBeInTheDocument();
    expect(screen.getByText('2 entries · ₹150.00')).toBeInTheDocument();
    expect(screen.queryByLabelText('July 2026 spending by category')).not.toBeInTheDocument();

    fireEvent.change(monthSelect, { target: { value: '2026-07' } });
    expect(screen.getByLabelText('July 2026 spending by category')).toBeInTheDocument();
    expect(screen.getByText('1 entry · ₹20.00')).toBeInTheDocument();
    expect(screen.queryByLabelText('August 2026 spending by category')).not.toBeInTheDocument();
  });
});
