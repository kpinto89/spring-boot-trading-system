-- Add additional demo users and multi-symbol support

-- Add 3rd demo user
INSERT INTO users (id, email, password_hash, full_name, created_at)
VALUES
  ('550e8400-e29b-41d4-a716-446655440003', 'trader2@example.com', '$2a$10$slYQmyNdGzin7olVN3p9O.0jxjMLi0UM7CyAVaZ7.v5rIcWYzfr1S', 'Trader Two', NOW())
ON CONFLICT DO NOTHING;

-- Add 3rd wallet
INSERT INTO wallets (id, user_id, currency, balance, updated_at)
VALUES
  ('650e8400-e29b-41d4-a716-446655440003', '550e8400-e29b-41d4-a716-446655440003', 'USD', 100000.00, NOW())
ON CONFLICT DO NOTHING;

-- ====================================
-- GOOGL - Alphabet Inc. | Price: $139.25
-- ====================================
INSERT INTO orders (id, user_id, symbol, side, type, price, quantity, remaining_qty, status, created_at)
VALUES
  ('750e8400-e29b-41d4-a716-446655440011', '550e8400-e29b-41d4-a716-446655440001', 'GOOGL', 'BUY', 'LIMIT', 138.00, 50, 20, 'PARTIALLY_FILLED', NOW()),
  ('750e8400-e29b-41d4-a716-446655440012', '550e8400-e29b-41d4-a716-446655440002', 'GOOGL', 'SELL', 'LIMIT', 140.00, 50, 0, 'FILLED', NOW())
ON CONFLICT DO NOTHING;

INSERT INTO trades (id, symbol, buy_order_id, sell_order_id, price, quantity, executed_at)
VALUES
  ('850e8400-e29b-41d4-a716-446655440011', 'GOOGL', '750e8400-e29b-41d4-a716-446655440011', '750e8400-e29b-41d4-a716-446655440012', 139.25, 30, NOW())
ON CONFLICT DO NOTHING;

-- ====================================
-- MSFT - Microsoft Corporation | Price: $422.50
-- ====================================
INSERT INTO orders (id, user_id, symbol, side, type, price, quantity, remaining_qty, status, created_at)
VALUES
  ('750e8400-e29b-41d4-a716-446655440021', '550e8400-e29b-41d4-a716-446655440002', 'MSFT', 'BUY', 'LIMIT', 420.00, 75, 25, 'PARTIALLY_FILLED', NOW()),
  ('750e8400-e29b-41d4-a716-446655440022', '550e8400-e29b-41d4-a716-446655440003', 'MSFT', 'SELL', 'LIMIT', 425.00, 75, 0, 'FILLED', NOW())
ON CONFLICT DO NOTHING;

INSERT INTO trades (id, symbol, buy_order_id, sell_order_id, price, quantity, executed_at)
VALUES
  ('850e8400-e29b-41d4-a716-446655440021', 'MSFT', '750e8400-e29b-41d4-a716-446655440021', '750e8400-e29b-41d4-a716-446655440022', 422.50, 50, NOW())
ON CONFLICT DO NOTHING;

-- ====================================
-- AMZN - Amazon.com Inc. | Price: $172.50
-- ====================================
INSERT INTO orders (id, user_id, symbol, side, type, price, quantity, remaining_qty, status, created_at)
VALUES
  ('750e8400-e29b-41d4-a716-446655440031', '550e8400-e29b-41d4-a716-446655440003', 'AMZN', 'BUY', 'LIMIT', 170.00, 60, 35, 'PARTIALLY_FILLED', NOW()),
  ('750e8400-e29b-41d4-a716-446655440032', '550e8400-e29b-41d4-a716-446655440001', 'AMZN', 'SELL', 'LIMIT', 175.00, 60, 0, 'FILLED', NOW())
ON CONFLICT DO NOTHING;

INSERT INTO trades (id, symbol, buy_order_id, sell_order_id, price, quantity, executed_at)
VALUES
  ('850e8400-e29b-41d4-a716-446655440031', 'AMZN', '750e8400-e29b-41d4-a716-446655440031', '750e8400-e29b-41d4-a716-446655440032', 172.50, 25, NOW())
ON CONFLICT DO NOTHING;

-- ====================================
-- TSLA - Tesla Inc. | Price: $242.50
-- ====================================
INSERT INTO orders (id, user_id, symbol, side, type, price, quantity, remaining_qty, status, created_at)
VALUES
  ('750e8400-e29b-41d4-a716-446655440041', '550e8400-e29b-41d4-a716-446655440001', 'TSLA', 'BUY', 'LIMIT', 240.00, 40, 15, 'PARTIALLY_FILLED', NOW()),
  ('750e8400-e29b-41d4-a716-446655440042', '550e8400-e29b-41d4-a716-446655440003', 'TSLA', 'SELL', 'LIMIT', 245.00, 40, 0, 'FILLED', NOW())
ON CONFLICT DO NOTHING;

INSERT INTO trades (id, symbol, buy_order_id, sell_order_id, price, quantity, executed_at)
VALUES
  ('850e8400-e29b-41d4-a716-446655440041', 'TSLA', '750e8400-e29b-41d4-a716-446655440041', '750e8400-e29b-41d4-a716-446655440042', 242.50, 25, NOW())
ON CONFLICT DO NOTHING;

-- ====================================
-- NVDA - NVIDIA Corporation | Price: $875.00
-- ====================================
INSERT INTO orders (id, user_id, symbol, side, type, price, quantity, remaining_qty, status, created_at)
VALUES
  ('750e8400-e29b-41d4-a716-446655440051', '550e8400-e29b-41d4-a716-446655440002', 'NVDA', 'BUY', 'LIMIT', 870.00, 30, 10, 'PARTIALLY_FILLED', NOW()),
  ('750e8400-e29b-41d4-a716-446655440052', '550e8400-e29b-41d4-a716-446655440001', 'NVDA', 'SELL', 'LIMIT', 880.00, 30, 0, 'FILLED', NOW())
ON CONFLICT DO NOTHING;

INSERT INTO trades (id, symbol, buy_order_id, sell_order_id, price, quantity, executed_at)
VALUES
  ('850e8400-e29b-41d4-a716-446655440051', 'NVDA', '750e8400-e29b-41d4-a716-446655440051', '750e8400-e29b-41d4-a716-446655440052', 875.00, 20, NOW())
ON CONFLICT DO NOTHING;

-- ====================================
-- META - Meta Platforms Inc. | Price: $485.00
-- ====================================
INSERT INTO orders (id, user_id, symbol, side, type, price, quantity, remaining_qty, status, created_at)
VALUES
  ('750e8400-e29b-41d4-a716-446655440061', '550e8400-e29b-41d4-a716-446655440003', 'META', 'BUY', 'LIMIT', 480.00, 45, 20, 'PARTIALLY_FILLED', NOW()),
  ('750e8400-e29b-41d4-a716-446655440062', '550e8400-e29b-41d4-a716-446655440002', 'META', 'SELL', 'LIMIT', 490.00, 45, 0, 'FILLED', NOW())
ON CONFLICT DO NOTHING;

INSERT INTO trades (id, symbol, buy_order_id, sell_order_id, price, quantity, executed_at)
VALUES
  ('850e8400-e29b-41d4-a716-446655440061', 'META', '750e8400-e29b-41d4-a716-446655440061', '750e8400-e29b-41d4-a716-446655440062', 485.00, 25, NOW())
ON CONFLICT DO NOTHING;

