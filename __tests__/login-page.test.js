import { fireEvent, render, screen, waitFor } from '@testing-library/react';
import LoginPage from '../app/login/page';
import { AUTH_TOKEN_KEY } from '../app/lib/api';

const replace = jest.fn();

jest.mock('next/navigation', () => ({
  useRouter: () => ({ replace }),
}));

describe('LoginPage', () => {
  beforeEach(() => {
    replace.mockReset();
    sessionStorage.clear();
    global.fetch = jest.fn();
  });

  test('renders email and password fields', () => {
    render(<LoginPage />);
    expect(screen.getByLabelText(/email/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/password/i)).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /log in/i })).toBeInTheDocument();
  });

  test('shows a validation error for an invalid email', async () => {
    render(<LoginPage />);
    fireEvent.change(screen.getByLabelText(/email/i), { target: { value: 'not-an-email' } });
    fireEvent.change(screen.getByLabelText(/password/i), { target: { value: 'password1' } });
    fireEvent.click(screen.getByRole('button', { name: /log in/i }));

    expect(await screen.findByText(/valid email address/i)).toBeInTheDocument();
    expect(global.fetch).not.toHaveBeenCalled();
  });

  test('shows a validation error for a short password', async () => {
    render(<LoginPage />);
    fireEvent.change(screen.getByLabelText(/email/i), { target: { value: 'you@example.com' } });
    fireEvent.change(screen.getByLabelText(/password/i), { target: { value: 'short' } });
    fireEvent.click(screen.getByRole('button', { name: /log in/i }));

    expect(await screen.findByText(/at least 8 characters/i)).toBeInTheDocument();
    expect(global.fetch).not.toHaveBeenCalled();
  });

  test('stores the token after a successful login', async () => {
    global.fetch.mockResolvedValue({
      ok: true,
      json: async () => ({ token: 'jwt-token', email: 'you@example.com', userId: 1 }),
    });

    render(<LoginPage />);
    fireEvent.change(screen.getByLabelText(/email/i), { target: { value: 'you@example.com' } });
    fireEvent.change(screen.getByLabelText(/password/i), { target: { value: 'password1' } });
    fireEvent.click(screen.getByRole('button', { name: /log in/i }));

    await waitFor(() => {
      expect(sessionStorage.getItem(AUTH_TOKEN_KEY)).toBe('jwt-token');
      expect(replace).toHaveBeenCalledWith('/');
    });
  });
});
