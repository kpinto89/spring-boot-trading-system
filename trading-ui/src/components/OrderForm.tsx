import { useMutation, useQueryClient } from '@tanstack/react-query';
import { useForm } from 'react-hook-form';
import { ordersService, PlaceOrderRequest } from '@/services/orders';
import { useWebSocket } from '@/hooks/useWebSocket';

const SYMBOLS = [
  { value: 'AAPL', label: 'AAPL - Apple Inc.' },
  { value: 'GOOGL', label: 'GOOGL - Alphabet Inc.' },
  { value: 'MSFT', label: 'MSFT - Microsoft Corporation' },
  { value: 'AMZN', label: 'AMZN - Amazon.com Inc.' },
  { value: 'TSLA', label: 'TSLA - Tesla Inc.' },
  { value: 'NVDA', label: 'NVDA - NVIDIA Corporation' },
  { value: 'META', label: 'META - Meta Platforms Inc.' },
];

export const OrderForm = () => {
  const queryClient = useQueryClient();
  const { register, handleSubmit, formState: { errors }, watch } = useForm<PlaceOrderRequest>({
    defaultValues: {
      symbol: 'AAPL',
      side: 'BUY',
      type: 'LIMIT',
      quantity: 1,
    },
  });

  const symbol = watch('symbol');
  const type = watch('type');
  useWebSocket(symbol);

  const { mutate: placeOrder, isPending } = useMutation({
    mutationFn: ordersService.placeOrder,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['orders'] });
    },
  });

  const onSubmit = handleSubmit((data) => {
    placeOrder(data);
  });

  return (
    <div className="bg-gray-800 rounded-lg p-6 border border-gray-700">
      <h2 className="text-2xl font-bold mb-4 text-white">Place Order</h2>
      <form onSubmit={onSubmit} className="space-y-4">
        <div>
          <label className="block text-sm font-medium mb-2 text-gray-300">Symbol</label>
          <select
            {...register('symbol', { required: 'Symbol is required' })}
            className="w-full px-4 py-2 bg-gray-700 text-white border border-gray-600 rounded"
          >
            {SYMBOLS.map((symbol) => (
              <option key={symbol.value} value={symbol.value}>
                {symbol.label}
              </option>
            ))}
          </select>
          {errors.symbol && <p className="text-red-500 text-sm mt-1">{errors.symbol.message}</p>}
        </div>

        <div className="grid grid-cols-2 gap-4">
          <div>
            <label className="block text-sm font-medium mb-2 text-gray-300">Side</label>
            <select {...register('side')} className="w-full px-4 py-2">
              <option value="BUY">Buy</option>
              <option value="SELL">Sell</option>
            </select>
          </div>
          <div>
            <label className="block text-sm font-medium mb-2 text-gray-300">Type</label>
            <select {...register('type')} className="w-full px-4 py-2">
              <option value="LIMIT">Limit</option>
              <option value="MARKET">Market</option>
            </select>
          </div>
        </div>

        {type === 'LIMIT' && (
          <div>
            <label className="block text-sm font-medium mb-2 text-gray-300">Price</label>
            <input
              {...register('price', { required: 'Price is required for limit orders' })}
              type="number"
              step="0.01"
              className="w-full px-4 py-2"
              placeholder="0.00"
            />
            {errors.price && <p className="text-red-500 text-sm mt-1">{errors.price.message}</p>}
          </div>
        )}

        <div>
          <label className="block text-sm font-medium mb-2 text-gray-300">Quantity</label>
          <input
            {...register('quantity', { required: 'Quantity is required', min: { value: 1, message: 'Must be > 0' } })}
            type="number"
            step="1"
            className="w-full px-4 py-2"
            placeholder="1"
          />
          {errors.quantity && <p className="text-red-500 text-sm mt-1">{errors.quantity.message}</p>}
        </div>

        <button
          type="submit"
          disabled={isPending}
          className="w-full bg-green-600 hover:bg-green-700 disabled:bg-gray-600 text-white font-bold py-2 px-4 rounded"
        >
          {isPending ? 'Placing Order...' : 'Place Order'}
        </button>
      </form>
    </div>
  );
};

