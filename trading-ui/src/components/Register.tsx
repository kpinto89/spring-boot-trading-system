import { useMutation } from '@tanstack/react-query';
import { useForm } from 'react-hook-form';
import { authService } from '@/services/auth';
import { useAuthStore } from '@/stores/authStore';
import { useNavigate } from 'react-router-dom';

export const Register = () => {
  const navigate = useNavigate();
  const setAuth = useAuthStore((state) => state.setAuth);
  const { register, handleSubmit, formState: { errors }, watch } = useForm({
    defaultValues: { email: '', password: '', fullName: '', confirmPassword: '' },
  });

  const password = watch('password');

  const { mutate: registerUser, isPending } = useMutation({
    mutationFn: authService.register,
    onSuccess: (data) => {
      setAuth(data.token, '');
      navigate('/dashboard');
    },
  });

  const onSubmit = handleSubmit((data) => {
    const { confirmPassword, ...registerData } = data;
    registerUser(registerData);
  });

  return (
    <div className="min-h-screen bg-gradient-to-br from-gray-900 to-gray-800 flex items-center justify-center p-4">
      <div className="w-full max-w-md bg-gray-800 rounded-lg shadow-lg p-8">
        <h1 className="text-3xl font-bold mb-6 text-center text-white">Create Account</h1>
        <form onSubmit={onSubmit} className="space-y-4">
          <div>
            <label className="block text-sm font-medium mb-2">Full Name</label>
            <input
              {...register('fullName', { required: 'Full name is required' })}
              type="text"
              className="w-full px-4 py-2"
              placeholder="John Doe"
            />
            {errors.fullName && <p className="text-red-500 text-sm mt-1">{errors.fullName.message}</p>}
          </div>
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
              {...register('password', {
                required: 'Password is required',
                minLength: { value: 8, message: 'Password must be at least 8 characters' },
              })}
              type="password"
              className="w-full px-4 py-2"
              placeholder="••••••••"
            />
            {errors.password && <p className="text-red-500 text-sm mt-1">{errors.password.message}</p>}
          </div>
          <div>
            <label className="block text-sm font-medium mb-2">Confirm Password</label>
            <input
              {...register('confirmPassword', {
                required: 'Please confirm your password',
                validate: (value) => value === password || 'Passwords do not match',
              })}
              type="password"
              className="w-full px-4 py-2"
              placeholder="••••••••"
            />
            {errors.confirmPassword && <p className="text-red-500 text-sm mt-1">{errors.confirmPassword.message}</p>}
          </div>
          <button
            type="submit"
            disabled={isPending}
            className="w-full bg-blue-600 hover:bg-blue-700 disabled:bg-gray-600 text-white font-bold py-2 px-4 rounded"
          >
            {isPending ? 'Registering...' : 'Register'}
          </button>
        </form>
        <p className="text-center mt-4 text-gray-400">
          Already have an account?{' '}
          <button
            onClick={() => navigate('/login')}
            className="text-blue-500 hover:text-blue-400 underline"
          >
            Login here
          </button>
        </p>
      </div>
    </div>
  );
};

