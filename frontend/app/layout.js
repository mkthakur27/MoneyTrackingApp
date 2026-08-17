import './globals.css';

export const metadata = {
  title: '💰 Money Tracking App',
  description: 'Track your spending with categories and quick summaries.',
};

export default function RootLayout({ children }) {
  return (
    <html lang="en">
      <body>{children}</body>
    </html>
  );
}
