import { useQuery } from '@tanstack/react-query';
import { ordersService } from '@/services/orders';
import { useMarketStore } from '@/stores/marketStore';
import { OrderForm } from './OrderForm';
import { OrdersTable } from './OrdersTable';

export const Dashboard = () => {
  const { data: orders = [], isLoading } = useQuery({
    queryKey: ['orders'],
    queryFn: ordersService.myOrders,
    refetchInterval: 3000,
  });

  const prices = useMarketStore((state) => state.prices);

  return (
    <div className="min-h-screen bg-gray-900 p-8">
      <div className="max-w-7xl mx-auto">
        <h1 className="text-4xl font-bold mb-8 text-white">Trading Dashboard</h1>

        <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
          {/* Left: Order Form */}
          <div className="lg:col-span-1">
            <OrderForm />
          </div>

          {/* Right: Orders & Market Data */}
          <div className="lg:col-span-2 space-y-8">
            {/* Market Prices */}
            <div className="bg-gray-800 rounded-lg p-6 border border-gray-700">
              <h2 className="text-2xl font-bold mb-4 text-white">Market Prices</h2>
              <div className="grid grid-cols-2 gap-4">
                {Object.entries(prices).map(([symbol, data]) => (
                  <div key={symbol} className="bg-gray-700 p-4 rounded">
                    <p className="text-gray-400 text-sm">{symbol}</p>
                    <p className="text-2xl font-bold text-green-400">${data.price.toFixed(2)}</p>
                    <p className="text-gray-500 text-xs mt-2">{new Date(data.timestamp).toLocaleTimeString()}</p>
                  </div>
                ))}
              </div>
            </div>

            {/* Orders List */}
            <div className="bg-gray-800 rounded-lg p-6 border border-gray-700">
              <h2 className="text-2xl font-bold mb-4 text-white">Your Orders</h2>
              {isLoading ? (
                <p className="text-gray-400">Loading orders...</p>
              ) : (
                <OrdersTable orders={orders} />
              )}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

