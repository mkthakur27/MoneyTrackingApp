import { fireEvent, render, screen, waitFor } from '@testing-library/react';
import EntriesTab from '../app/components/EntriesTab';
import { apiRequest } from '../app/lib/api';

jest.mock('../app/lib/api', () => {
  const actual = jest.requireActual('../app/lib/api');
  return {
    ...actual,
    apiRequest: jest.fn(),
    useCurrencySymbol: () => '₹',
  };
});

jest.mock('swr', () => {
  const mutate = jest.fn();
  return {
    __esModule: true,
    default: () => ({ data: [], mutate, isLoading: false }),
    mutate: jest.fn(),
  };
});

describe('EntriesTab', () => {
  beforeEach(() => {
    apiRequest.mockReset();
  });

  test('lets the user mark a new expense as recurring', async () => {
    apiRequest.mockResolvedValue({});
    render(<EntriesTab />);

    fireEvent.change(screen.getByPlaceholderText('Enter what you spent'), {
      target: { value: 'Gym membership' },
    });
    fireEvent.change(screen.getByPlaceholderText('0.00'), { target: { value: '1500' } });
    fireEvent.click(screen.getByRole('checkbox', { name: /save as a recurring expense/i }));
    fireEvent.change(screen.getByLabelText('Repeats'), { target: { value: 'MONTHLY' } });
    fireEvent.click(screen.getByRole('button', { name: /add expense/i }));

    await waitFor(() => expect(apiRequest).toHaveBeenCalledTimes(2));
    expect(apiRequest.mock.calls[0][0]).toBe('/api/entries');
    expect(JSON.parse(apiRequest.mock.calls[0][1].body)).toEqual(
      expect.objectContaining({
        description: 'Gym membership',
        amount: 1500,
      })
    );
    expect(JSON.parse(apiRequest.mock.calls[0][1].body).recurring).toBeUndefined();
    expect(apiRequest.mock.calls[1][0]).toBe('/api/recurring');
    expect(JSON.parse(apiRequest.mock.calls[1][1].body)).toEqual(
      expect.objectContaining({
        description: 'Gym membership',
        amount: 1500,
        period: 'MONTHLY',
      })
    );
    expect(
      await screen.findByText(/saved as a monthly recurring expense/i)
    ).toBeInTheDocument();
  });
});
