import { useEffect, useRef } from 'react';
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { useMarketStore } from '@/stores/marketStore';

const WS_URL = import.meta.env.VITE_WS_URL || '/ws';

export const useWebSocket = (symbol: string) => {
  const clientRef = useRef<Client | null>(null);
  const updatePrice = useMarketStore((state) => state.updatePrice);
  const addTrade = useMarketStore((state) => state.addTrade);

  useEffect(() => {
    const client = new Client({
      webSocketFactory: () => new SockJS(WS_URL),
      onConnect: () => {
        // Subscribe to price updates
        client.subscribe(`/topic/price/${symbol}`, (message) => {
          try {
            const data = JSON.parse(message.body);
            updatePrice(data.symbol, data.price);
          } catch (error) {
            console.error('Error parsing price update:', error);
          }
        });

        // Subscribe to trade updates
        client.subscribe(`/topic/trades/${symbol}`, (message) => {
          try {
            const data = JSON.parse(message.body);
            addTrade(data.symbol, {
              symbol: data.symbol,
              price: data.price,
              quantity: data.quantity,
              timestamp: data.executedAt,
            });
          } catch (error) {
            console.error('Error parsing trade update:', error);
          }
        });
      },
      onDisconnect: () => {
        console.log('WebSocket disconnected');
      },
      onStompError: (frame) => {
        console.error('STOMP error:', frame);
      },
    });

    client.activate();
    clientRef.current = client;

    return () => {
      if (clientRef.current?.connected) {
        clientRef.current.deactivate();
      }
    };
  }, [symbol, updatePrice, addTrade]);

  return clientRef.current;
};
