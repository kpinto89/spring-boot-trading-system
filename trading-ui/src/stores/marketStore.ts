import { create } from 'zustand';

export interface PriceUpdate {
  symbol: string;
  price: number;
  timestamp: string;
}

export interface TradeUpdate {
  symbol: string;
  price: number;
  quantity: number;
  timestamp: string;
}

export interface MarketState {
  prices: Record<string, PriceUpdate>;
  trades: Record<string, TradeUpdate[]>;
  updatePrice: (symbol: string, price: number) => void;
  addTrade: (symbol: string, trade: TradeUpdate) => void;
  clearTrades: (symbol: string) => void;
  resetMarket: () => void;
}

export const useMarketStore = create<MarketState>((set) => ({
  prices: {},
  trades: {},
  updatePrice: (symbol: string, price: number) =>
    set((state) => ({
      prices: {
        ...state.prices,
        [symbol]: {
          symbol,
          price,
          timestamp: new Date().toISOString(),
        },
      },
    })),
  addTrade: (symbol: string, trade: TradeUpdate) =>
    set((state) => ({
      trades: {
        ...state.trades,
        [symbol]: [...(state.trades[symbol] || []), trade].slice(-50), // Keep last 50
      },
    })),
  clearTrades: (symbol: string) =>
    set((state) => ({
      trades: {
        ...state.trades,
        [symbol]: [],
      },
    })),
  resetMarket: () => set({ prices: {}, trades: {} }),
}));
