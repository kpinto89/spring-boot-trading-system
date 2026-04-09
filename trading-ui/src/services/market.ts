import apiClient from './apiClient';

export const marketService = {
  lastPrice: async (symbol: string): Promise<number> => {
    const response = await apiClient.get<number>('/market/last-price', {
      params: { symbol },
    });
    return Number(response.data ?? 0);
  },
};

