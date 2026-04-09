import { Order } from '@/services/orders';

interface OrdersTableProps {
  orders: Order[];
}

const getStatusColor = (status: string): string => {
  switch (status) {
    case 'NEW':
      return 'bg-blue-900 text-blue-200';
    case 'PARTIALLY_FILLED':
      return 'bg-yellow-900 text-yellow-200';
    case 'FILLED':
      return 'bg-green-900 text-green-200';
    default:
      return 'bg-gray-700 text-gray-200';
  }
};

export const OrdersTable = ({ orders }: OrdersTableProps) => {
  if (orders.length === 0) {
    return <p className="text-gray-400 text-center py-8">No orders yet</p>;
  }

  return (
    <div className="overflow-x-auto">
      <table className="w-full text-left text-sm">
        <thead className="border-b border-gray-600">
          <tr className="text-gray-300">
            <th className="px-4 py-3">Symbol</th>
            <th className="px-4 py-3">Side</th>
            <th className="px-4 py-3">Type</th>
            <th className="px-4 py-3">Price</th>
            <th className="px-4 py-3">Qty / Filled</th>
            <th className="px-4 py-3">Status</th>
            <th className="px-4 py-3">Created</th>
          </tr>
        </thead>
        <tbody>
          {orders.map((order) => (
            <tr key={order.id} className="border-b border-gray-700 hover:bg-gray-700 transition">
              <td className="px-4 py-3 font-semibold">{order.symbol}</td>
              <td className="px-4 py-3">
                <span className={`px-2 py-1 rounded ${order.side === 'BUY' ? 'text-green-400' : 'text-red-400'}`}>
                  {order.side}
                </span>
              </td>
              <td className="px-4 py-3">{order.type}</td>
              <td className="px-4 py-3">${order.price?.toFixed(2) || 'Market'}</td>
              <td className="px-4 py-3">
                {order.quantity.toFixed(2)} / {order.remainingQty.toFixed(2)}
              </td>
              <td className="px-4 py-3">
                <span className={`px-2 py-1 rounded text-xs font-semibold ${getStatusColor(order.status)}`}>
                  {order.status}
                </span>
              </td>
              <td className="px-4 py-3 text-gray-400">{new Date(order.createdAt).toLocaleString()}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

