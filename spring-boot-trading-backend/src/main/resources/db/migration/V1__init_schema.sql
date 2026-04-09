CREATE TABLE IF NOT EXISTS users (
  id            UUID PRIMARY KEY,
  email         VARCHAR(255) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  full_name     VARCHAR(255) NOT NULL,
  created_at    TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS wallets (
  id          UUID PRIMARY KEY,
  user_id     UUID NOT NULL UNIQUE REFERENCES users(id),
  currency    VARCHAR(10) NOT NULL,
  balance     NUMERIC(19, 4) NOT NULL,
  updated_at  TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS orders (
  id            UUID PRIMARY KEY,
  user_id       UUID NOT NULL REFERENCES users(id),
  symbol        VARCHAR(20) NOT NULL,
  side          VARCHAR(10) NOT NULL,
  type          VARCHAR(10) NOT NULL,
  price         NUMERIC(19, 6),
  quantity      NUMERIC(19, 6) NOT NULL,
  remaining_qty NUMERIC(19, 6) NOT NULL,
  status        VARCHAR(30) NOT NULL,
  created_at    TIMESTAMP NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_orders_symbol ON orders(symbol);
CREATE INDEX IF NOT EXISTS idx_orders_user ON orders(user_id);

CREATE TABLE IF NOT EXISTS trades (
  id            UUID PRIMARY KEY,
  symbol        VARCHAR(20) NOT NULL,
  buy_order_id  UUID NOT NULL REFERENCES orders(id),
  sell_order_id UUID NOT NULL REFERENCES orders(id),
  price         NUMERIC(19, 6) NOT NULL,
  quantity      NUMERIC(19, 6) NOT NULL,
  executed_at   TIMESTAMP NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_trades_symbol ON trades(symbol);
