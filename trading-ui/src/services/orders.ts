import apiClient from './apiClient';
import { UUID } from 'crypto';

export interface Order {
  id: UUID;
  symbol: string;
  side: 'BUY' | 'SELL';
  type: 'LIMIT' | 'MARKET';
  price?: number;
  quantity: number;
  remainingQty: number;
  status: 'NEW' | 'PARTIALLY_FILLED' | 'FILLED';
  createdAt: string;
}

export interface PlaceOrderRequest {
  symbol: string;
  side: 'BUY' | 'SELL';
  type: 'LIMIT' | 'MARKET';
  price?: number;
  quantity: number;
}

export const ordersService = {
  placeOrder: async (data: PlaceOrderRequest): Promise<Order> => {
    const response = await apiClient.post('/orders', data);
    return response.data;
  },

  myOrders: async (): Promise<Order[]> => {
    const response = await apiClient.get('/orders/me');
    return response.data;
  },

  getOrderById: async (id: UUID): Promise<Order> => {
    const response = await apiClient.get(`/orders/${id}`);
    return response.data;
  },
};

