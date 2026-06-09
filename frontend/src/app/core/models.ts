// API models mirroring the backend DTOs.

export interface LoginRequest {
  email: string;
  password: string;
}

export interface LoginResponse {
  token: string;
}

export interface RegistrationRequest {
  email: string;
  password: string;
  repeatPassword: string;
  firstName: string;
  lastName: string;
  shippingAddress?: string;
}

export type Role = 'USER' | 'ADMIN';

export interface CurrentUser {
  id: number;
  email: string;
  firstName: string;
  lastName: string;
  shippingAddress?: string;
  roles: Role[];
}

export interface Book {
  id: number;
  title: string;
  author: string;
  isbn: string;
  description?: string;
  coverImage?: string;
  price: number;
  categories?: number[];
}

export interface BookPayload {
  title: string;
  author: string;
  isbn: string;
  description?: string;
  coverImage?: string;
  price: number;
  categories?: number[];
}

export interface Category {
  id: number;
  name: string;
  description?: string;
}

export interface CategoryPayload {
  name: string;
  description?: string;
}

export interface CartItem {
  id: number;
  bookId: number;
  bookTitle: string;
  quantity: number;
}

export interface ShoppingCart {
  id: number;
  userId: number;
  cartItems: CartItem[];
}

export interface AddToCartRequest {
  bookId: number;
  quantity: number;
}

export interface OrderItem {
  id: number;
  bookId: number;
  quantity: number;
  price: number;
}

export interface Order {
  id: number;
  userId: number;
  status: string;
  total: number;
  orderDate: string;
  shippingAddress: string;
  orderItems: OrderItem[];
}

export interface CreateOrderRequest {
  shippingAddress: string;
}

// Spring Data Page<T> shape.
export interface Page<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
  first: boolean;
  last: boolean;
}
