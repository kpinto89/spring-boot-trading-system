# Trading UI - React TypeScript Frontend

A modern React 18+ TypeScript SPA for the Spring Boot Trading System, built with **Vite**, **TanStack Query**, **Zustand**, and **Tailwind CSS**.

## Features

- ✅ **Authentication** — JWT-based login/register with token persistence
- ✅ **Real-time Market Data** — WebSocket integration (STOMP) for live price/trade updates
- ✅ **Order Management** — Place limit/market orders, view order history with status tracking
- ✅ **Responsive Dashboard** — Dark theme UI with market prices, wallet info, and order forms
- ✅ **Type-safe** — Full TypeScript support with strict mode
- ✅ **Fast Build** — Vite for instant HMR and optimized production builds

## Prerequisites

- **Node.js** 18+ (LTS recommended)
- **npm** 9+ or **yarn** 1.22+
- Spring Boot backend running on `http://localhost:8080`

## Quick Start

### 1. Install Dependencies

```bash
cd trading-ui
npm install
```

### 2. Configure Environment

Edit `.env.local` if your backend is on a different host:

```env
VITE_API_URL=http://localhost:8080/api
VITE_WS_URL=ws://localhost:8080/ws
```

### 3. Start Dev Server

```bash
npm run dev
```

The app will open at `http://localhost:3000`

### 4. Build for Production

```bash
npm run build
npm run preview
```

## Project Structure

```
trading-ui/
├── src/
│   ├── components/          # React components
│   │   ├── Login.tsx        # Login form
│   │   ├── Register.tsx     # Registration form
│   │   ├── Dashboard.tsx    # Main trading dashboard
│   │   ├── OrderForm.tsx    # Place order form
│   │   ├── OrdersTable.tsx  # Orders list
│   │   └── ProtectedRoute.tsx  # Auth guard
│   ├── services/            # API clients
│   │   ├── apiClient.ts     # Axios instance with auth interceptors
│   │   ├── auth.ts          # Auth endpoints
│   │   └── orders.ts        # Order endpoints
│   ├── stores/              # State management (Zustand)
│   │   ├── authStore.ts     # Auth state + token persistence
│   │   └── marketStore.ts   # Market prices & trades
│   ├── hooks/               # Custom React hooks
│   │   └── useWebSocket.ts  # WebSocket (STOMP) connection
│   ├── App.tsx              # Root component + routing
│   ├── main.tsx             # React DOM render
│   └── index.css            # Global styles (Tailwind)
├── index.html               # HTML entry point
├── vite.config.ts           # Vite configuration + proxy
├── tsconfig.json            # TypeScript config
├── tailwind.config.js       # Tailwind CSS config
├── postcss.config.js        # PostCSS config for Tailwind
├── .env.local               # Environment variables
├── .gitignore               # Git ignore
└── package.json             # Dependencies
```

## Key Technologies

| Package | Purpose |
|---------|---------|
| **React 18** | UI framework |
| **TypeScript** | Type safety |
| **Vite** | Build tool & dev server |
| **React Router v6** | Client-side routing |
| **Axios** | HTTP client |
| **TanStack Query (React Query)** | Server state management |
| **Zustand** | Client state management |
| **STOMP.js** | WebSocket client for real-time updates |
| **React Hook Form** | Form state management |
| **Tailwind CSS** | Utility-first CSS framework |

## API Integration

### Authentication Flow

1. User registers/logs in via `/api/auth/register` or `/api/auth/login`
2. Backend returns JWT token
3. Token stored in localStorage (via Zustand)
4. Axios interceptor adds `Authorization: Bearer <token>` to all requests
5. On 401 response, user is logged out and redirected to `/login`

### WebSocket Connection

- Connects to `ws://localhost:8080/ws` using STOMP protocol
- Subscribes to:
  - `/topic/price/{symbol}` — Real-time price updates
  - `/topic/trades/{symbol}` — Trade execution updates
- Updates stored in `marketStore` and reflected in UI

### Example API Calls

```typescript
// Login
const { token } = await authService.login({
  email: 'user@example.com',
  password: 'password'
});

// Place order
const order = await ordersService.placeOrder({
  symbol: 'AAPL',
  side: 'BUY',
  type: 'LIMIT',
  price: 150.00,
  quantity: 10
});

// Get my orders
const orders = await ordersService.myOrders();
```

## Development

### Hot Module Replacement (HMR)

Changes to `.tsx` files auto-refresh without full page reload:

```bash
npm run dev
```

### Type Checking

Run TypeScript compiler without emitting:

```bash
npm run type-check
```

### Linting

Check code style:

```bash
npm run lint
```

## Troubleshooting

### Backend Connection Failed

- Ensure Spring Boot backend is running on `http://localhost:8080`
- Check CORS config in backend
- Update `VITE_API_URL` in `.env.local` if backend is on different host

### WebSocket Connection Refused

- Backend must have `/ws` endpoint configured
- Check Spring Security allows `/ws/**` paths
- WebSocket proxy in `vite.config.ts` should match backend URL

### Authentication Errors (401)

- Token may be expired; re-login
- Check JWT secret in backend matches your token
- Verify token is being stored in localStorage

## Deployment

### Docker

```dockerfile
FROM node:18-alpine AS build
WORKDIR /app
COPY . .
RUN npm ci && npm run build

FROM node:18-alpine
WORKDIR /app
RUN npm install -g serve
COPY --from=build /app/dist ./dist
EXPOSE 3000
CMD ["serve", "-s", "dist", "-l", "3000"]
```

### Static Hosting (Vercel, Netlify)

Build output is in `dist/` folder. Update `VITE_API_URL` in build environment.

## Contributing

1. Create feature branch: `git checkout -b feature/amazing-feature`
2. Commit changes: `git commit -m 'Add amazing feature'`
3. Push to branch: `git push origin feature/amazing-feature`
4. Open Pull Request

## License

MIT

