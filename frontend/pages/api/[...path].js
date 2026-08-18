const backendUrl = process.env.BACKEND_URL || 'http://localhost:8080';

export default async function handler(req, res) {
  const { path = [] } = req.query;
  const queryIndex = req.url.indexOf('?');
  const search = queryIndex >= 0 ? req.url.slice(queryIndex) : '';
  const url = `${backendUrl}/api/${path.join('/')}${search}`;

  const headers = { 'Content-Type': 'application/json' };
  const authorization = req.headers.authorization;
  if (authorization) {
    headers.Authorization = authorization;
  }

  const init = {
    method: req.method,
    headers,
  };

  if (req.method !== 'GET' && req.method !== 'DELETE' && req.body) {
    init.body = JSON.stringify(req.body);
  }

  try {
    const response = await fetch(url, init);
    const data = await response.text();
    res.status(response.status);
    if (!data) {
      res.end();
      return;
    }
    try {
      res.json(JSON.parse(data));
    } catch {
      res.send(data);
    }
  } catch (error) {
    res.status(502).json({ message: `Backend request failed: ${error.message}` });
  }
}
