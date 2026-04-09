import { useMutation } from '@tanstack/react-query';
import { useForm } from 'react-hook-form';
import { authService } from '@/services/auth';
import { useAuthStore } from '@/stores/authStore';
import { useNavigate } from 'react-router-dom';

export const Login = () => {
  const navigate = useNavigate();
  const setAuth = useAuthStore((state) => state.setAuth);
  const { register, handleSubmit, formState: { errors } } = useForm({
    defaultValues: { email: '', password: '' },
  });

  const { mutate: login, isPending } = useMutation({
    mutationFn: authService.login,
    onSuccess: (data) => {
      setAuth(data.token, '');
      navigate('/dashboard');
    },
  });

  const onSubmit = handleSubmit((data) => login(data));

  return (
    <div className="min-h-screen bg-gradient-to-br from-gray-900 to-gray-800 flex items-center justify-center p-4">
      <div className="w-full max-w-md bg-gray-800 rounded-lg shadow-lg p-8">
        <h1 className="text-3xl font-bold mb-6 text-center text-white">Trading System</h1>
        <form onSubmit={onSubmit} className="space-y-4">
          <div>
            <label className="block text-sm font-medium mb-2">Email</label>
            <input
              {...register('email', {
                required: 'Email is required',
                pattern: { value: /^\S+@\S+$/i, message: 'Invalid email' },
              })}
              type="email"
              className="w-full px-4 py-2"
              placeholder="your@email.com"
            />
            {errors.email && <p className="text-red-500 text-sm mt-1">{errors.email.message}</p>}
          </div>
          <div>
            <label className="block text-sm font-medium mb-2">Password</label>
            <input
              {...register('password', { required: 'Password is required' })}
              type="password"
              className="w-full px-4 py-2"
              placeholder="••••••••"
            />
            {errors.password && <p className="text-red-500 text-sm mt-1">{errors.password.message}</p>}
          </div>
          <button
            type="submit"
            disabled={isPending}
            className="w-full bg-blue-600 hover:bg-blue-700 disabled:bg-gray-600 text-white font-bold py-2 px-4 rounded"
          >
            {isPending ? 'Logging in...' : 'Login'}
          </button>
        </form>
        <p className="text-center mt-4 text-gray-400">
          Don't have an account?{' '}
          <button
            onClick={() => navigate('/register')}
            className="text-blue-500 hover:text-blue-400 underline"
          >
            Register here
          </button>
        </p>
      </div>
    </div>
  );
};

